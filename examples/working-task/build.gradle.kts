tasks.register("helloWorld") {
    doLast { // This method takes as argument a Task.() -> Unit
        println("Hello, World!")
    }
}
