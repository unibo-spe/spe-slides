import org.gradle.internal.jvm.Jvm

val compilationDestination: String = layout.buildDirectory.dir("bin").get().asFile.absolutePath

tasks.compileJava {
    dependsOn(project(":lib").tasks.named("compileJava"))
}

tasks.register<Exec>("runJava") {
    inputs.dir(compilationDestination)
    dependsOn(tasks.compileJava)
    executable(Jvm.current().javaExecutable.absolutePath)
    doFirst {
        args(
            "-cp", "${compilationDestination}${File.pathSeparator}${configurations["runtimeClasspath"].asPath}", // Fragile!
            "HelloMath",
        )
    }
}
