import org.gradle.internal.jvm.Jvm

// DECLARATIVE (what)

val compilationDestination = layout.buildDirectory.dir("bin").get().asFile
val compileClasspath: Configuration by configurations.creating
val runtimeClasspath: Configuration by configurations.creating {
    extendsFrom(compileClasspath)
}

dependencies { // built-in in Gradle
    allFiles.inFolder("libs").withExtension("jar").forEach { // Not Gradle: defined below
        compileClasspath(files(it)) // The Configuration class overrides the invoke operator
    }
    runtimeClasspath(files(compilationDestination))
}

tasks.register<JavaCompile>("compileJava") {
    classpath = compileClasspath
    destinationDir = compilationDestination
    sources = files(project.allFiles.inFolder("src").withExtension("java"))
}

tasks.register<JavaRun>("runJava") {
    classpath = runtimeClasspath
    mainClass = "HelloMath"
    dependsOn(tasks.named("compileJava"))
}

// IMPERATIVE (how)

// Minimal file access DSL
object AllFiles
val Project.allFiles: AllFiles get() = AllFiles // We need this to prevent "Object AllFiles captures the script class instance" error
data class Finder(val path: File) {
    fun withExtension(extension: String): List<File> =
        path.walkTopDown().filter { it.isFile && it.extension == extension }.toList()
}
fun AllFiles.inFolder(path: String) = Finder(projectDir.resolve(path))

interface TaskWithClasspath : Task {
    val classpath: Property<FileCollection>
}

interface JavaCompileTask : TaskWithClasspath {
    val sources: Property<FileCollection>
    val destinationDir: DirectoryProperty
}

interface JavaRunTask : TaskWithClasspath {
    val mainClass: Property<String>
}

abstract class AbstractJvmExec : TaskWithClasspath, Exec() {
    @Classpath
    override val classpath: Property<FileCollection> = project.objects.property<FileCollection>()

    init {
        executable(Jvm.current().jvmExecutableForTask().absolutePath)
    }

    protected abstract fun Jvm.jvmExecutableForTask(): File

}

open class JavaRun() : JavaRunTask, AbstractJvmExec() {
    @Classpath
    override val classpath: Property<FileCollection> = project.objects.property<FileCollection>()

    @Input
    override val mainClass: Property<String> = project.objects.property<String>()

    override fun Jvm.jvmExecutableForTask(): File = javaExecutable

    @TaskAction
    override fun exec() {
        args(
            "-cp", classpath.get().asPath,
            mainClass.get(),
        )
        super.exec()
    }
}

open class JavaCompile: JavaCompileTask, AbstractJvmExec() {

    @Input
    override val sources: Property<FileCollection> = project.objects.property<FileCollection>()
    @OutputDirectory
    override val destinationDir: DirectoryProperty = project.objects.directoryProperty()
    @Classpath
    override val classpath: Property<FileCollection> = project.objects.property<FileCollection>()

    override fun Jvm.jvmExecutableForTask(): File = javacExecutable

    @TaskAction
    override fun exec() {
        when {
            sources.get().isEmpty -> {
                println("No source files found, skipping compilation.")
                args("-version")
            }
            else -> args(
                // destination folder: the output directory of Gradle, inside "bin"
                "-d", destinationDir.get().asFile.absolutePath,
                // classpath from the configuration
                "-cp", "${File.pathSeparator}${classpath.get().asPath}",
                *sources.get().files.toTypedArray(),
            )
        }
        super.exec()
    }
}
