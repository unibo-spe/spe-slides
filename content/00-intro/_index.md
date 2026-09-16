
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

- [Virtual Learning Environment ("Virtuale")](https://virtuale.unibo.it/course/view.php?id=83089)
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

## [Timetable]({{< teaching_page >}}/orariolezioni)

* **Thursday 10:00--13:00** (3h) --- Room 2.5
* **Friday 9:00--12:00** (3h) --- Lab 4.2

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

* Knowledge of *Java*, *Scala* is a nice to have
* Minimal ability with `git`
  * initializing and managing the repository options
  * committing
  * branching and merging
  * fetching and pushing
* A *curious mindset*
  * Never stop when it works, stop when you know *why* it does
    * This is especially true in the LLM era

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

* Can be a project that covers SPE + thesis

---

# "Project work"

The exam's project can be developed a "project work", namely,
a project whose requirements are provided by a real-world company,
which acts as a commissioner.

* The students will interact with the company to apply Domain-Driven Design on a real piece of software.
* The project will be open source
* All the features required for a "normal" exam project still hold

It is an opportunity to *learn by doing* in a context closer to the industry.

Available project works will be posted on the course site on https://virtuale.unibo.it/

---

# Final project quality checklist

It is warmly recommended to check out https://www.bestpractices.dev/en, applying first all the relevant items there.

{{% multicol %}}
{{% col %}}

### DVCS

- Adoption of a DVCS
- Consistently following some commit convention (e.g. [Conventional Commits](https://www.conventionalcommits.org/))
- Consistently following some branching/forking convention
- The branching convention is adequate to the team size and type
- Commits are consistent and coherent, i.e. only what should be committed together has been committed together
- reasonable, justified merging strategy

{{% /col %}}
{{% col %}}

### Versioning

- Web API specifications (e.g. OpenAPI, [Swagger](https://swagger.io/)), if any, are versioned
- Web servers routes (if any) are versioned
- Releases are versioned
- Version numbers are computed automatically
- Multiple releases exist

{{% /col %}}
{{% /multicol %}}

---

# Final project quality checklist

{{% multicol %}}
{{% col %}}

### Project structure
- The project dependencies are formally declared (incl. locking where reasonable)
- The project dependencies are updated automatically where possible
- The project directory includes build automation
- The build automation technology of choice is adequate for all the target platforms
- All repositories have a clear `README.md` file describing their purpose and content

### Licensing

- The project directory includes a LICENSE file, unless it is proprietary
- The choice of the licence is adequately motivated in the report

{{% /col %}}
{{% col %}}

### QA

- The code includes automatic tests
- The code includes unit tests
- The code includes integration tests
- The code includes end-to-end / system tests
- Integration or system tests exploit containerization (if applicable)
- Test code supports parallel runs (runs do not conflict with each other)
- Each test suite can be executed (in principle) even without CI/CD pipelines
- A procedure for the computation of test coverage is in place, and the final coverage is reported and commented
- Static analysis is in place

{{% /col %}}
{{% /multicol %}}

---

# Final project quality checklist

{{% multicol %}}
{{% col %}}

### DevOps

- CI/CD pipelines in place
- CI/CD matrices in place
- The entire test suite is executed in CI/CD
- Automatic release of software artifacts on target repositories (eg GitHub releases + artifact repositories for the target platforms)
- If the project accepts contributions from third parties, correct pipeline configuration for pull requests coming from external owners

{{% /col %}}
{{% col %}}

### Domain-driven Design (DDD)

- __Domain__ and __bounded contexts__ have been clearly identified
- __Entities__, __value-objects,__ and __aggregate roots__ have been adequately modelled according to DDD
- __Repositories__ have been adequately modelled according to DDD
- __Services__ have been adequately modelled according to DDD
- __Factories__ have been adequately modelled according to DDD
- Exploitation of __model-integrity__ patterns is adequately motivated
- The overall design of the system reflects the [hexagonal architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) (if applicable)

{{% /col %}}
{{% /multicol %}}

---

# Final project quality checklist

{{% multicol %}}
{{% col %}}

### Requirements

- _Requirements_ are clearly captured, and categories (e.g. functional vs. non-functional)
- _Scenarios_ are clearly described via __user stories__
- Definition of 'done' for each requirement

### Multi-platform
- The project involves >= 2 target platforms
- The most dominant platform cannot be used for more than 75% of the overall project size
- The project is organized in such a way that code targeting different platforms is clearly partitioned
- Core domain entities spanning across multiple platforms are defined so to minimize code duplication, while enforcing design coherence


{{% /col %}}
{{% col %}}

### AI exploitation
- The report clearly declares what GenAI has been used for and how (or that it was not used at all)
- Each repository has an AI-DECLARATION.md file compliant to the convention (https://ai-declaration.md/)
- Each repository where GenAI was exploited via Agents as an AGENTS.md file compliant to the convention (https://agents.md/)
- Skills possibly exploited by your agents are documented and motivated in the report

{{% /col %}}
{{% /multicol %}}

---

# Software

## Required
* A working internet connection
* A working JDK installation
  * Consider using [Jabba](https://github.com/shyiko/jabba)
* Docker

## Recommended
* Kotlin
* Gradle
* IntelliJ Idea
* Visual Studio Code
* A decent Unix terminal
    * I recommend a well-configured `zsh` shell
* ki-shell ([Kotlin Interactive Shell](https://github.com/Kotlin/kotlin-interactive-shell))

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
