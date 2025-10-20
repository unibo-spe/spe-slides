val compilationDestination = project.layout.buildDirectory.dir("bin").get().asFile
val compileClasspath: Configuration by configurations.creating
val runtimeClasspath: Configuration by configurations.creating {
    extendsFrom(compileClasspath)
}

dependencies { // built-in in Gradle
    allFilesIn("libs").withExtension("jar").forEach { // Not Gradle: defined below
        compileClasspath(files(it)) // The Configuration class overrides the invoke operator
    }
    runtimeClasspath(files(compilationDestination))
}

tasks.register<JavaCompile>("compileJava") {
    classpath = compileClasspath
    destinationDir = compilationDestination
    sources = files(allFilesIn("src").withExtension("java"))
}

tasks.register<JavaRun>("runJava") {
    classpath = runtimeClasspath
    mainClass = "HelloMath"
    dependsOn(tasks.named("compileJava"))
}
