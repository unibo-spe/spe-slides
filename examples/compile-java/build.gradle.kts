import org.gradle.internal.jvm.Jvm

tasks.register<Exec>("compileJava") { // This is a Kotlin lambda with receiver!
    val sourceDir = projectDir.resolve("src")
    inputs.dir(sourceDir)
    val outputDir = layout.buildDirectory.dir("bin").get().asFile.absolutePath
    outputs.dir(outputDir)
    val javacExecutable = Jvm.current().javacExecutable.absolutePath // Use the current JVM's javac
    executable(javacExecutable)
    doFirst { // We need to compute the sources and classpath as late as possible
        val sources = sourceDir.walkTopDown().filter { it.isFile && it.extension == "java" }.toList()
        println(sources)
        when {
            sources.isEmpty() -> {
                println("No source files found, skipping compilation.")
                args("-version")
            }
            else -> args(
                // destination folder: the output directory of Gradle, inside "bin"
                "-d", outputDir,
                *sources.toTypedArray(),
            )
        }
    }
}