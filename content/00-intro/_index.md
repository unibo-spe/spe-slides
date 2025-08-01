
+++

title = "Introduction to Software Process Engineering"
description = "Description of the course"
outputs = ["Reveal"]

[reveal_hugo.custom_theme_options]
targetPath = "css/custom-theme.css"

+++

# Introduction to the course

{{% import path="reusable/header.md" %}}

---

# Professors

### Danilo Pianini
  * email: [`danilo.pianini@unibo.it`](mailto:danilo.pianini@unibo.it)
  * homepage: [`https://www.unibo.it/sitoweb/danilo.pianini/en`](https://www.unibo.it/sitoweb/danilo.pianini/en)

### Giovanni Ciatto
  * email: [`giovanni.ciatto@unibo.it`](mailto:giovanni.ciatto@unibo.it)
  * homepage: [`https://www.unibo.it/sitoweb/giovanni.ciatto/en`](https://www.unibo.it/sitoweb/giovanni.ciatto/en)

---

# Contacts

#### Prioritize the forum
#### [`https://virtuale.unibo.it/mod/forum/view.php?id=1342533`](https://virtuale.unibo.it/mod/forum/view.php?id=1342533)
  * All technical question
  * Any other non-personal question

<p>

#### When using the email
  * Include *both* teachers, **always**

<p>

#### Office hours
* Danilo Pianini $\Rightarrow$ check [the teacher webpage](https://www.unibo.it/sitoweb/danilo.pianini/en)
* Giovanni Ciatto $\Rightarrow$ check [the teacher webpage](https://www.unibo.it/sitoweb/giovanni.ciatto/en)

---

# Pages of the course

- [Virtual Learning Environment ("Virtuale")](https://virtuale.unibo.it/course/view.php?id=50031)
  + please enroll if you didn't already

- [These slides](https://unibo-spe.github.io)

---

# Resources

* These *slides* should contain everything you need
* code examples produced during the lecture will be available right after
* most code is already on GitHub

Slides will be produced with a *rolling release* model.

## Books

No mandatory books, but there are both:
* Recommended readings
* Additional useful books

On [the course webpage]({{< teaching_page >}})

---

# Organization

Lectures in lab with immediate hands-on.

## [Timetable]({{< teaching_page >}}/orariolezioni)

* **Thursday 11:00--14:00** (2h) --- Lab 3.1
* **Friday 11:00--14:00** (3h) --- Lab 4.2

Changes will be published on the forum

---

# Goals

1. Learn how to design software systems, following a *domain*-, *model*-, and/or *test*-driven approach
2. Zero-overhead from *domain definition* to *executable code*
3. *Agile* development practices, *DevOps* philosophy
4. *High automation* + *technical excellence*
5. Understand analogies and differences among *programming platforms*

---

# Prerequisites

* Knowledge of *Java* and *Scala*
* Minimal ability with `git`
  * initializing and managing the repository options
  * committing
  * branching and merging
  * fetching and pushing
* A *curious mindset*
  * Never stop when it works, stop when you know *why* it does

---

# Exam

### **Discussion** of a **group project**

* Must feature:
  * *Domain-driven* design
  * Clear development process and *DevOps* practices
  * Full-scale *automation*
    * Including *continuous integration* and delivery
  * Deploy automation via *containerization* and/or *orchestration*
  * Technically involve _2+ **target** platforms_
    * e.g., JVM, NodeJS, Python, C, C++, Rust, Go, etc.
    * two targets are different if they run on a different runtime (e.g., native + JVM)
    * two targets are *likely* different if they use different build systems (but there are exceptions: Scala/sbt + Java/Gradle are not considered different targets)

* Can be a **joint effort** with other courses
  * We care about the *domain modeling* and the application of *DevOps techniques*
  * You can pick a project of another course, apply them there, and it is fine for SPE

* Can be a project created for SPE alone
  * If you are short on ideas, we can help :)

---

# Software

## Required
* A working internet connection
* A working JDK installation
  * Consider using [Jabba](https://github.com/shyiko/jabba)
* Kotlin (kotlinc REPL working)
* Gradle
* Docker

## Recommended
* IntelliJ Idea
* Visual Studio Code
* A decent Unix terminal

---

# Course Container

### Feeling lazy?
We prepared a container with all the course's software:
  * [https://hub.docker.com/repository/docker/danysk/linux-didattica](https://hub.docker.com/repository/docker/danysk/linux-didattica)

Follow the instructions at [https://github.com/DanySK/docker-linux-didattica](https://github.com/DanySK/docker-linux-didattica)

### Feeling Windows-y?

The container can be converted into a WSL2 Linux distribution.

(Instructions available in the same repository as above)

---

# Software in lab

The PCs are equipped with the WSL2 image

* There should be a link on the Desktop
* Double-clicking it should pop up a `zsh` shell
  * Wait for the first terminal to show before starting others

---

# Introduction to the course

{{% import path="reusable/header.md" %}}
