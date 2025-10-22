plugins {
    id("java-convention")
}

tasks.runJava.configure { // We must set the main class here
    mainClass = "HelloMath"
}
