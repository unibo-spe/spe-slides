val helloWorld = tasks.register("helloWorld") {
    doLast { // This method takes as argument a Task.() -> Unit
        println("Hello, World!")
    }
}

helloWorld.configure { // Note the `configure` method: late configuration of the task
    doFirst {
        println("About to say hello...")
    }
}

tasks.withType<Task>().configureEach {
    println("Configuring task: ${this.name}")
    doLast {
        println("Finished task: ${this.name}")
    }
    doFirst {
        println("Starting task: ${this.name}")
    }
}

