import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.jvm.Jvm
import java.io.File

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
