import org.gradle.internal.jvm.Jvm

val compileClasspath: Configuration by configurations.creating // Delegation!

dependencies { // built-in in Gradle
    AllFiles.inFolder("libs").withExtension("jar").forEach { // Not Gradle: defined below
        compileClasspath(files(it)) // The Configuration class overrides the invoke operator
    }
}

tasks.register<Exec>("compileJava") { // This is a Kotlin lambda with receiver!
    inputs.dir(projectDir.resolve("src"))
    val outputDir = layout.buildDirectory.dir("bin").get().asFile.absolutePath
    outputs.dir(outputDir)
    val javacExecutable = Jvm.current().javacExecutable.absolutePath // Use the current JVM's javac
    executable(javacExecutable)
    doFirst { // We need to compute the sources and classpath as late as possible
        val sources = AllFiles.inFolder("src").withExtension("java")
        println(sources)
        when {
            sources.isEmpty() -> {
                println("No source files found, skipping compilation.")
                args("-version")
            }
            else -> args(
                // destination folder: the output directory of Gradle, inside "bin"
                "-d", outputDir,
                // classpath from the configuration
                "-cp", "${File.pathSeparator}${compileClasspath.asPath}",
                *sources.toTypedArray(),
            )
        }
    }
}

// Minimal file access DSL
object AllFiles
val Project.allFiles: AllFiles get() = AllFiles
data class Finder(val path: File) {
    fun withExtension(extension: String): List<File> =
        path.walkTopDown().filter { it.isFile && it.extension == extension }.toList()
}
fun AllFiles.inFolder(path: String) = Finder(projectDir.resolve(path))
