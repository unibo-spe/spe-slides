val compilationDestination = project.layout.buildDirectory.dir("bin").get().asFile
val compileClasspath: Configuration by configurations.creating
val runtimeClasspath: Configuration by configurations.creating {
    extendsFrom(compileClasspath)
}

dependencies {
    allFilesIn("libs").withExtension("jar").forEach {
        compileClasspath(files(it))
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
    dependsOn(tasks.named("compileJava"))
}
