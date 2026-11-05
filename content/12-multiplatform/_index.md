+++

title = "Multi-platform programming"
description = "Theory and practice of multi-platform programming"
outputs = ["Reveal"]

[reveal_hugo.custom_theme_options]
targetPath = "css/custom-theme.css"
enableSourceMap = true

+++

<style>
.reveal blockquote {
    font-family: 'Georgia';
}
.reveal blockquote::before{
    content: "";
}
.reveal blockquote::after{
    content: "";
}
.reveal .platform-code pre {
    font-size: .70em;
}
.reveal .platform-tree {
    font-size: .65em;
}
</style>

# Multi-platform programming

{{% import path="reusable/header-gc.md" %}}

---

## Outline

1. [Preliminaries](#/preliminaries): _software platforms, API and runtimes, platform choice, technological silos_
2. [Approaches](#/mp-approaches): _write once, build anywhere_ vs. _write first, wrap elsewhere_
3. [Write once, build anywhere with Kotlin](#/kotlin-mp): _targets, project structure, build configuration, `expect`/`actual`_
4. [Running example](#/running-example): _a multi-platform CSV library, for the JVM and JS_
5. [Calling Kotlin from other platforms](#/kotlin-interop): _Kotlin–Java and Kotlin–JS mappings_
6. [Multi-platform CI/CD](#/mp-cicd): _testing and releasing on several platforms_
7. [Write first, wrap elsewhere with JPype](#/jpype): _wrapping the JVM library into a Pythonic package_
8. [Wrap-up](#/wrap-up): _comparing the two approaches_

> Example code: [`unibo-spe/multiplatform-example`](https://github.com/unibo-spe/multiplatform-example)

---

{{< slide id="preliminaries" >}}

# Multi-platform programming

## Preliminaries

---

{{% section %}}

# What are software platforms?

- Fuzzy concept
  * often mentioned in courses / literature, yet not precisely defined

- Insight:
> The **substratum** upon which **software applications** _run_

---

# What are software platforms?
## Operating systems (?)

- Most software runs on top of an __operating system__ (OS)

- So yes, all operating systems are platforms

- Is there more?
  + e.g. [JabRef](https://www.jabref.org/): __Java__ application running on top of __JVM__
    * the JVM is an application for the OS
  + e.g. [Draw.io](https://app.diagrams.net/): __Web__ application running on top of __browsers__
    * the browser is an application for the OS

---

# What are software platforms?
## Operating systems + ???

- Programming languages?
  * not that simple
    + e.g. Scala apps can _call_ Java code

- Runtimes
  * i.e. the set of libraries and conventions backing programming languages

{{% /section %}}

---

# What are software platforms?
## Definition attempt

> **Software platform** $\stackrel{\Delta}{=}$ anything having an **API** enabling the writing of applications,
> and the **runtime** supporting the execution of those applications

- what are APIs and runtimes, then?

---

{{% section %}}

# What are APIs and runtimes?

> [**API**](https://en.wikipedia.org/wiki/API) $\equiv$ application programming interface(s) $\stackrel{\Delta}{=}$ a _formal_ specification of the set of **functionalities** provided by a software (sub-)system for _external_ usage, including their **inputs**, **outputs**, and _environmental_ **preconditions** and **effects**
- client-server metaphor is implicit

> [**Runtime**](https://en.wikipedia.org/wiki/Runtime_system) [system/environment] $\approx$ the set of computational resources backing the execution of a software (sub-)system
- we say that "runtimes _support_ API"

---

# What are APIs and runtimes?

## Examples of APIs

- all possible _public interfaces_ / classes / structures in an __OOP module__
  * and their public/protected methods / fields / properties / constructors
    + and their formal arguments, return types, throwable exceptions

- all possible _commands_ a __CLI application__ accepts as _input_
  * and their admissible sub-commands, options, and arguments
    + and the corresponding outputs, exit values, and side effects

- all possible _routes_ a __Web service__ may accept HTTP requests on
  * and their admissible HTTP methods
    + and their admissible query / path / body / header parameters
      - and the corresponding status codes, and response bodies

---

# What are APIs and runtimes?

## Examples of runtimes

- any _virtual machine_ or _interpreter_ (JVM, CLR, CPython, V8)
  * and their standard libraries
  * and their type system and internal conventions
    + e.g. primitive/reference types in the JVM, value/reference types in the CLR, global lock in CPython

- any _operating system_ (Win, Mac, Linux)
  * and their system calls, daemons, package managers, default commands, etc.
  * and their program memory, access control, file system models

- any _Web service_ 
  * and the protocols they leverage
  * and their URL structuring model
  * the data schema of their input/output objects
  * the authentication / authorization mechanisms they support

{{% /section %}}

---

# Notable platforms

- The __Java Virtual Machine__ (JVM)
  * supported languages: Java, Kotlin, Scala, Clojure, etc.

- .NET's [pronounced "dot NET"] __Common Language Runtime__ (CLR), cross-platform since .NET Core
  * supported languages: C#, VB.NET, F#, etc.

- __Python__ 3
  * supported language: Python

- __NodeJS__ (V8), and alternative JS runtimes such as [Deno](https://deno.com/) and [Bun](https://bun.sh/)
  * supported languages: JavaScript, TypeScript (via type stripping or transpilation), etc.

- Each __browser__ may be considered as a platform per se
  * supported languages: JavaScript, plus anything compiled to [WebAssembly](https://webassembly.org/) (Wasm)

- __Native__ code for each OS + CPU architecture (e.g. via LLVM), and __mobile__ platforms (Android, iOS)

- ...

---

# Practical features of platforms

- _standard libraries_
  * i.e. ready-made functionalities developers / users may exploit

- predefined _design decisions_ 
  * e.g. global interpreter lock in CPython, event loop in JavaScript, etc.

- organizational, stylistic, technical _conventions_
  * e.g. project structure, code linting, nomenclature, etc.

- _packaging_ conventions, _import mechanisms_, and _software repositories_
  * e.g. classpath for the JVM, npm for JS, pip + PyPI for Python, etc.

- user _communities_
  * e.g. many Data scientists use Python, many Web developers use JVM / JS

---

{{% section %}}

# Example: the JVM platform

---

## Standard and community libraries: JVM

{{< figure src="jvm-libraries.svg" width="92%" alt="A JVM application uses JDK collections, file and networking APIs, threads and numbers, plus separately installed Jackson, Apache Commons CSV and Spring libraries." >}}

- __JDK APIs__ are shared across JVM languages: Java, Kotlin, Scala, etc.
- __Community libraries__ add domain-specific abstractions (e.g. [Commons CSV](https://commons.apache.org/proper/commons-csv/)), resolved by Maven / Gradle

---

## Predefined design decisions: JVM

{{% multicol %}}{{% col class="col-5" %}}

- Every class extends [`Object`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html)
  * primitives are distinct; _boxing_ wraps them
- Every object has a __monitor__
  * `synchronized` instance methods lock the receiver
- Java instance methods are __virtual__ by default
  * `toString()` dispatches to the runtime class
  * Kotlin methods are `final` by default

Run the [full example](platform-examples/jvm/DesignDecisions.java):

`java -ea DesignDecisions.java`

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/jvm/DesignDecisions.java" from="1" to="21" %}}

{{% /col %}}{{% /multicol %}}

---

## Organizational, stylistic, technical conventions: JVM

{{% multicol %}}{{% col class="col-5" %}}

__Organization__: [Maven directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)

<pre class="platform-tree">project/
├── pom.xml (or build.gradle.kts)
└── src/
    ├── main/java/it/unibo/spe/
    │   └── CsvRow.java
    └── test/java/it/unibo/spe/
        └── CsvRowTest.java</pre>

- __Style__: `CsvRow`, `getValues`, `values`
- __Technical protocols__: `Iterable` enables the enhanced `for` loop
- Collections accept `Collection`; variadic `String...` is a `String[]`

[Full code](platform-examples/jvm/CsvRow.java), including a `main` and assertions

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/jvm/CsvRow.java" from="1" to="28" %}}

{{% /col %}}{{% /multicol %}}

---

## Packaging, imports, repositories: JVM

{{< figure src="jvm-packaging.svg" width="96%" alt="Maven Central supplies dependency coordinates to Gradle or Maven, which place JAR files on the class path so Java can resolve imported classes." >}}

{{% multicol %}}{{% col class="col-6" %}}

- __Package__ → namespace, e.g. `org.apache.commons.csv`
- __JAR__ → ZIP archive of compiled classes and resources
- __Dependency coordinates__ → `org.apache.commons:commons-csv:1.14.1`

{{% /col %}}{{% col class="col-6" %}}

- __Import__ → `import org.apache.commons.csv.CSVFormat;`
- __Class path__ → where the JVM finds classes (`java -cp ...`)
- __Module path__ → also available for [JPMS](https://openjdk.org/projects/jigsaw/spec/) modules since Java 9

{{% /col %}}{{% /multicol %}}

> Resolve __dependencies__ first; __import__ classes in source code

---

## User communities: JVM

{{< figure src="jvm-communities.svg" width="96%" alt="Four example JVM user communities, represented by phone, server, desktop and laboratory icons: Android developers, back-end Web developers, desktop application developers, and researchers working on Semantic Web or multi-agent systems." >}}

> The same JVM library can reach __several communities__ through their preferred JVM language

{{% /section %}}

---

{{% section %}}

# Example: the Python platform

---

## Standard and community libraries: Python

{{< figure src="python-libraries.svg" width="92%" alt="Python supplies JSON and CSV handling, file and concurrency APIs, complex numbers and SQLite; NumPy, pandas, scikit-learn, PyTorch, Django and FastAPI are separately installed community libraries." >}}

- The [__standard library__](https://docs.python.org/3.13/library/) already covers CSV / JSON, complex numbers, etc.
- __Community packages__ add arrays, tables, ML models and Web frameworks

---

## Predefined design decisions: Python

{{% multicol %}}{{% col class="col-5" %}}

- __Everything is an object__, including numbers
- __Magic methods__ connect objects to language features
  * `str(row)` → `row.__str__()`
  * `len(row)` → `row.__len__()`
- No signature-based method overloading
  * a later `def` with the same name replaces the earlier binding
- Default CPython builds have a __GIL__
  * optional [free-threaded builds](https://docs.python.org/3/howto/free-threading-python.html) exist since Python 3.13

Run the [full example](platform-examples/python/design_decisions.py):

`python3 design_decisions.py`

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/python/design_decisions.py" from="1" to="15" %}}

{{% /col %}}{{% /multicol %}}

---

## Organizational, stylistic, technical conventions: Python

{{% multicol %}}{{% col class="col-5" %}}

__Organization__: [src layout](https://packaging.python.org/en/latest/discussions/src-layout-vs-flat-layout/) (flat layout is also common)

<pre class="platform-tree">project/
├── pyproject.toml
├── src/csv_demo/
│   ├── __init__.py
│   └── csv_row.py
└── tests/
    └── test_csv_row.py</pre>

- __Style__: [PEP 8](https://peps.python.org/pep-0008/), `CsvRow`, `join_values`, four spaces
- __Duck typing__: same function accepts our object or a built-in list
- `*values` → tuple; `**metadata` → dictionary

[Full code](platform-examples/python/csv_row.py), including assertions

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/python/csv_row.py" from="1" to="16" %}}

{{% /col %}}{{% /multicol %}}

---

## Packaging, imports, repositories: Python

{{< figure src="python-packaging.svg" width="96%" alt="PyPI hosts distributions; pip installs the requests wheel into a virtual environment's site-packages directory; Python resolves import requests from its module search path." >}}

{{% multicol %}}{{% col class="col-6" %}}

- __Module__ → `.py` file; regular __package__ → directory with `__init__.py`
- __Wheel__ (`.whl`) → installable ZIP (sources, optional native extensions, metadata)
- __Source distribution__ (`.tar.gz`) → must first be built

{{% /col %}}{{% col class="col-6" %}}

- [__pip__](https://packaging.python.org/en/latest/guides/installing-using-pip-and-virtual-environments/) installs distributions from __PyPI__ into an environment
- `import` searches `sys.path`, including `site-packages`
- Distribution and import names can differ: `pip install scikit-learn` → `import sklearn`

{{% /col %}}{{% /multicol %}}

> __pip__ installs a distribution; __import__ loads a module

---

## User communities: Python

{{< figure src="python-communities.svg" width="96%" alt="Chart, server, desktop and gear icons represent Python communities in data science and machine learning, back-end Web, desktop applications, and system administration." >}}

> Scientific libraries reach users who expect __Python objects and protocols__, even when implemented in C / C++

{{% /section %}}

---

{{% section %}}

# Example: the NodeJS platform

---

## Standard and community libraries: NodeJS

{{< figure src="node-libraries.svg" width="92%" alt="NodeJS combines JavaScript built-ins with Node file, HTTP, process and crypto APIs; Express, csv-parse, React tooling and Electron come from the community ecosystem." >}}

- __JavaScript built-ins__ + [__Node APIs__](https://nodejs.org/api/) form the platform's standard facilities
- __Community packages__ add higher-level abstractions; browser DOM APIs are not part of Node

---

## Predefined design decisions: NodeJS

{{% multicol %}}{{% col class="col-5" %}}

- __Prototype-based__ object orientation
  * `row` inherits `toString` from its prototype
- JavaScript callbacks run on the [__event loop__](https://nodejs.org/en/learn/asynchronous-work/event-loop-timers-and-nexttick)
  * CPU-heavy work blocks other callbacks
  * worker threads / processes enable parallelism
- __Promises__ and `async` / `await`
  * `await` suspends this async function
  * the caller continues before it resumes

Run the [full example](platform-examples/node/design-decisions.mjs):

`node design-decisions.mjs`

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/node/design-decisions.mjs" from="3" to="19" %}}

{{% /col %}}{{% /multicol %}}

---

## Organizational, stylistic, technical conventions: NodeJS

{{% multicol %}}{{% col class="col-5" %}}

__Organization__: one possible layout

<pre class="platform-tree">project/
├── package.json
├── src/
│   └── csv-row.mjs
└── test/
    └── csv-row.test.mjs</pre>

- No mandatory source-directory layout
- __Style__: `CsvRow`, `joinValues`; teams choose a linter / formatter
- __Duck typing__ and the iterable protocol (`Symbol.iterator`)
- Rest parameters → arrays; `.mjs` explicitly selects ES modules

[Full code](platform-examples/node/csv-row.mjs), including assertions

{{% /col %}}{{% col class="col-7 platform-code" %}}

{{% code path="platform-examples/node/csv-row.mjs" from="3" to="19" %}}

{{% /col %}}{{% /multicol %}}

---

## Packaging, imports, repositories: NodeJS

{{< figure src="node-packaging.svg" width="96%" alt="The npm registry hosts packages; npm installs a tarball into node_modules; Node resolves ES module imports using the installed package and its package.json metadata." >}}

{{% multicol %}}{{% col class="col-6" %}}

- __Package__ → `package.json` + sources / assets, distributed as `.tgz`
- __npm registry__ → repository; `npm` → dependency tool (also `yarn`, `pnpm`)
- `npm install csv-parse` → local dependency in `node_modules`

{{% /col %}}{{% col class="col-6" %}}

- __ES modules__: `import { parse } from 'csv-parse';`
- __CommonJS__: `const { parse } = require('csv-parse');`
- [__Resolution__](https://nodejs.org/api/packages.html): package entry points via `exports` / `main`; relative ESM imports include the extension, e.g. `./csv-row.mjs`

{{% /col %}}{{% /multicol %}}

> __npm__ installs packages; `import` / `require` loads their exported modules

---

## User communities: NodeJS

{{< figure src="node-communities.svg" width="96%" alt="Browser, server, desktop and gear icons represent front-end developers using Node build tooling and server-side rendering, back-end developers, Electron desktop developers, and CLI or automation authors." >}}

> Front-end code runs in the __browser__; Node often runs its __build tools__ or server-side rendering

{{% /section %}}

---

{{% section %}}

# Platforms from software developers' perspective

The __choice__ of a _platform_ impacts developers during:

- the _design_ phase

- the _implementation_ phase

- the _testing_ phase

- the _release_ phase

---

## How the platform affects the **design** phase

- One may choose the platform which minimizes the __abstraction gap__ w.r.t. the problem at hand

> __Abstraction gap__ $\approx$ the space between the __problem__ and the pre-existing functionalities offered by a __platform__. 
> Intuitively, the bigger the space, the more _effort_ is required to build the solution 

{{< figure src="abstraction-gap.svg" width="70%" alt="The abstraction gap: the distance between the problem and the functionalities offered by the platform" >}}

---

## How the platform affects the **implementation** phase

- Developers _write_ solutions by leveraging the _API_ of the __platform__

- ... as well as the API of any __third-party library__ available for that platform

{{< figure src="third-party.svg" width="80%" alt="A software project built on top of third-party libraries, in turn built on top of the platform" >}}

---

## How the platform affects the **testing** phase

- _Test suites_ are a __"project in the project"__
  * so remarks are similar to the implementation phase

- One may test the system against _as many versions as possible_ of the underlying platforms

- One may test the system against _as many OSs_ as possible
  * virtual platforms may behave differently depending on the OS

- Both are typically automated via CI _build matrices_ (cf. [CI lecture](../07-ci/))

---

## How the platform affects the **release** phase

> __Release__ $\approx$ publishing some packaged software system onto a repository, hence enabling its import and exploitation

- _Packaging systems_ are platform-specific...

- _Repositories_ are platform-specific...

- ... _release_ is therefore platform specific (cf. [version selection](../05-version-selection/) and [CI](../07-ci/))

{{% /section %}}

---

## Platform choice for a new SW project

- Choice is commonly driven by design / technical decisions
  + which platform would ease developers' work the most?

- However, choosing the platform is a business decision as well
  + platforms have user communities
  + SW projects benefit from wide(r) user communities

- Business decision: which user communities to target?
  + what are the most relevant platforms for those communities?

- Coherence is key for success in platform selection
  1. coherently choose the target community w.r.t. the SW goal
  2. coherently choose the platform w.r.t. target community
  
---

{{% section %}}

## What about __research-oriented__ software? (pt. 1)

> Researchers may act as software developers

1. to elaborate data

2. to create in-silico experiments

3. to study software systems

4. to create software tools improving their research

5. to create software tools for the community
    * this is how many FOSS tools have been created

---

## What about __research-oriented__ software? (pt. 2)

> Research institutions are _not_ software houses

- Personnel can only dedicate a fraction of their time to development

- Most software artifacts are disposable (data elaboration, in-silico experiments, personal tools)

- Development efforts are discontinuous

- Development teams are small 

- Software is commonly a means, not an objective

- Commitment to software development is:
  * commonly on the individual 
  * sometimes on the project
  * rarely on the institution

---

## What about __research-oriented__ software? (pt. 3)

> Research-oriented software development should maximise audience and impact, while minimising development and maintenance effort

- Science requires reproducibility
  * a wide(r) user base is a facilitator for reproducibility

- The wider the community, the wider the impact of community-driven research software
  * more potential citations

- Minimising effort can be done by improving efficiency
  * by means of software engineering

---

## General benefits of coherence (in general, and __for researchers__)

> Choosing the right community / platform is strategic, especially for research-oriented software

+ The abstraction gap is likely lower
  * less development effort

+ More third party libraries are likely available
  * less development effort

+ The potential audience is wider
  * more value, more impact, more reproducibility

+ Easier to find support / help in case of issues
  * potentially, less development effort

+ More likely that third-party issues are fixed in a timely manner
  * potentially, less development effort

{{% /section %}}

---

{{% section %}}

## About technological silos

> __Silos__ (in IT) are software components / systems / ecosystems having poor external interoperability 
> (i.e. software from silo A hardly interoperates with software from silo B)

- Platforms are (pretty wide) software silos
  * making software from any two platforms interoperate is non-trivial
  * making software from the same platform interoperate is easier

- Examples:
  * intra-platform: Kotlin program calling Java library
  * inter-platform: Python program calling a JVM library (e.g. via JPype, see later)

--- 

## Open research communities vs. technological silos

- Research communities in CS / AI may overlap with platform (or language) communities:
  * e.g. neural networks researchers $\rightarrow$ Python
  * e.g. data science $\rightarrow$ Python | R
  * e.g. symbolic AI $\rightarrow$ JVM | Prolog
  * e.g. multi-agent systems $\rightarrow$ JVM
  * e.g. Semantic Web $\rightarrow$ JVM | Python

- What about inter-community research efforts?
  * they may need interoperability among different silos
  * without which, research is slowed down

> Multi-platform programming is an enabler for inter-community research

{{% /section %}}

--- 

## The need for multi-platform programming

- Ideal goal:
  > let the same software tool run on multiple platforms

- More reasonable goal:
  > create multiple artifacts, one for each supported platform, sharing the same design and functioning

- Practical goal:
  > design and write the software once, then port it to several platforms

---

{{% section %}}

{{< slide id="mp-approaches" >}}

## Approaches for multi-platform programming

1. Write once, build anywhere
    - software is developed using some sort of "super-language"
    - code from the "super-language" is automatically compiled for all platforms
    - e.g. [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform/), [Haxe](https://haxe.org/), [Scala.js](https://www.scala-js.org/) + [Scala Native](https://scala-native.org/)
    - not to be confused with Java's _"write once, run anywhere"_, where one artifact runs on one (virtual) platform, available on many OSs

2. Write first, wrap elsewhere
    - software is developed for some principal platform
    - implementations for all other platforms are wrappers of the first one

---

## Write once, build anywhere (concept)

{{< figure src="write-once-build-everywhere.svg" width="70%" alt="Write once, build anywhere: one code base in a super-language, compiled into one artifact per target platform" >}}

---

## Write once, build anywhere (explanation)

- Assumptions:
  * one "super-language" exists, having:
    1. a code-generator targeting multiple platforms
        + e.g. compiler, transpiler, etc.
    2. the same standard library implemented for all those platforms

- Workflow:
  1. Design, implement, and test most of the project via the super-language
     - this is the platform-agnostic (a.k.a. "common") part of the project
  2. Complete, refine, or optimise platform-specific aspects via
     - platform-specific code
     - platform-specific third-party libraries
  3. Build platform-specific artifacts, following platform-specific rules
  4. Upload platform-specific artifacts on platform-specific repositories

---

## Write once, build anywhere (analysis)

Let `N` be the number of supported platforms

- Platform-agnostic functionalities require effort which is independent of `N`

- Platform-specific functionalities require effort proportional to `N`

- Better to minimise platform-specific code
  * by maximising common code
  * this is true both for main code and for test code 

- Relevant questions: 
  * what to realise as common code? what as platform-specific code?
  * how to set the boundary of the common (resp. platform-specific) code?

---

### Takeaway

> The abstraction gap of the common code is at least as wide as the one of the platform having the widest abstraction gap
> (the common std-lib is roughly the _intersection_ of what all platforms offer)

{{< figure src="abstraction-gap-mp.svg" width="50%" alt="Abstraction gap of the common code, compared to the abstraction gaps of each target platform" >}}

---

## Write once, build anywhere (strategy)

Whenever a new functionality needs to be developed:

1. Try to realise it with common std-lib only

2. If not possible, try to maximise the portion of platform-agnostic code
   * make it possible to plug platform-specific aspects

3. For each functionality which cannot be realised as purely platform-agnostic:
   1. design a platform-agnostic interface
   2. implement the interface `N` times, one per target platform

---

## Write first, wrap elsewhere (concept)

{{< figure src="write-first-wrap-elsewhere.svg" width="70%" alt="Write first, wrap elsewhere: the software is implemented on a main platform, other platforms wrap it" >}}

---

## Write first, wrap elsewhere (explanation)

- Assumptions:
  * one "main" platform exists such that
    + code from the "main" platform can be called from other target platforms
  * the software has been designed in a platform-agnostic way

- Workflow:
  1. Fully implement, test, and deploy the software for the main platform

  2. For all other platforms:
     1. re-design and re-write platform-specific API code
     2. implement platform-specific API by calling the main platform's code
     3. re-write tests for API code
     4. build platform-specific packages, wrapping the main platform's package and runtime
  3. Upload platform-specific artifacts on platform-specific repositories

---

## Write first, wrap elsewhere (analysis)

Let `N` be the number of supported platforms

- Clear separation of API code from implementation code is quintessential

- The effort required for writing API code is virtually the same on all platforms

- Global effort is still linear in `N`, but with a much smaller constant
  * implementing API and wrapper code is a manual task
  * still less effort than implementing the same design `N` times

- The `i`-th platform's wrapper code will only call the main platform's API code

- Relevant questions: 
  * how to deal with platform-specific aspects?
  * how to create platform-agnostic design?

{{% /section %}}

---

{{< slide id="kotlin-mp" >}}

# Multi-platform programming

## Write once, build anywhere

---


## Write once, build anywhere with __Kotlin__

- JetBrains-made modern programming language
    * focused on "practical use" (whatever that means)

- Gaining momentum since Google adopted it as the *official Android language* (2017)
    * Android development has been _Kotlin-first_ since 2019

- Clearly inspired by a mixture of Java, C#, Scala, and Groovy
    * standard library clearly inspired by Java's

- Born in industry, for the industry
    * initially considered *a better Java*
    * focused on getting productive quickly and reducing programming errors

- Acts as the "super language" supporting the "write once, build anywhere" approach
    * Kotlin compilers are available for several platforms
    * standard library implemented for those platforms

--- 

## Kotlin: supported platforms (a.k.a. targets)

Reference: [Kotlin Multiplatform DSL reference](https://kotlinlang.org/docs/multiplatform/multiplatform-dsl-reference.html)

- JVM
    + first and best supported platform
    + the JDK to compile against should be selected explicitly, e.g. `kotlin { jvmToolchain(17) }`

- JavaScript (JS)
    + requires choosing among Browser support, Node support or both
    + the two sub-targets have different runtimes

- WebAssembly (Wasm)
    + `wasmJs` (browsers, Node) and `wasmWasi` (WASI runtimes)

- Android
    + Android libraries and apps, via the Android Gradle plugin

- Native
    + supporting native may imply dealing with 3+ targets (OS $\times$ CPU architecture)
    + any host can compile _libraries_ (`.klib`s) for any target, but final binaries for Apple targets require macOS

--- 

## Kotlin: supported platforms, Native sub-targets

Reference: [Kotlin/Native target support](https://kotlinlang.org/docs/native-target-support.html)

- Targets come in __tiers__, i.e. levels of testing on JetBrains' CI:
    + _Tier 1_ (compiled _and_ run on CI): `macosArm64`, `iosArm64`, `iosSimulatorArm64`
    + _Tier 2_ (compiled on CI): e.g. `linuxX64`, `linuxArm64`, `watchosArm64`, `tvosArm64`
    + _Tier 3_ (not guaranteed to be tested): e.g. `mingwX64` (Windows), `androidNative*`
    + some targets are deprecated (e.g. `macosX64` since Kotlin 2.3.20)

- Windows via [MinGW](https://www.mingw-w64.org/), Linux
    + must pay attention to architecture
    + MinGW is not exactly a "vanilla" Windows environment

- iOS, macOS, watchOS, tvOS
    + requires Apple facilities (e.g. Xcode), hence macOS machines

- Android Native (`androidNative*`)
    + native code for Android (via the NDK), as opposed to the JVM-based `android` target

---

## __Gradle__ as the tool for build automation

- Building a multi-platform Kotlin project is supported via [Gradle](https://gradle.org/) (cf. [build automation](../04-build-automation/))
    * JetBrains' [Amper](https://github.com/JetBrains/amper) is an experimental alternative

- Gradle is a build automation + dependency management system

- Gradle simultaneously enables & constrains the multi-platform workflow
    * pre-defined project structure
    * pre-defined development workflow
    * enforced code partitioning
    * ad-hoc tasks for platform specific/agnostic compilation, testing, deploy, etc.

---

## Kotlin multi-platform project structure (common)

Kotlin enforces strong segregation of the platform-agnostic and platform-specific parts

- __common *main* code__: platform-agnostic code which only depends on:
    * the Kotlin std-lib 
    * plus other third-party Kotlin multi-platform libraries

- __common *test* code__: platform-agnostic _test_ code which only depends on:
    * the common main code and all its dependencies
    * some Kotlin multi-platform testing library (e.g. [`kotlin.test`](https://kotlinlang.org/api/core/kotlin-test/) or [Kotest](https://kotest.io/))

---

## Kotlin multi-platform project structure (platform-specific)

- for each target platform `T` (e.g. `jvm`, `js`, etc.)
    * __`T`-specific *main* code__: main Kotlin code targeting the `T` platform, depending on:
        + the common main code and all its dependencies
        + `T`-specific standard library
        + `T`-specific third-party libraries

    * __`T`-specific *test* code__: test Kotlin code targeting the `T` platform, depending on:
        + `T`-specific main code
        + `T`-specific test libraries

---

## Kotlin multi-platform project structure (overview)

{{< figure src="source-sets.svg" width="75%" alt="Six source sets in a grid: jvmMain, commonMain, jsMain on the main row, and jvmTest, commonTest, jsTest on the test row. Platform-specific source sets depend on the common one of the same row, and each test source set sees the main source set of its column. Each box lists what the code may use, e.g. the JDK for jvmMain, Node.js APIs and npm packages for jsMain, only the common stdlib for commonMain, plus examples of what each kind of source set typically contains." >}}

---

## Kotlin multi-platform project structure (example)

```bash
<root>/
│
├── build.gradle.kts        # multi-platform build description 
├── settings.gradle.kts     # root project settings
│
└── src/
    ├── commonMain/
    │   └── kotlin/         # platform-agnostic Kotlin code here
    │
    ├── commonTest/
    │   └── kotlin/         # platform-agnostic test code written in Kotlin here
    │
    ├── jvmMain/
    │   ├── java/           # JVM-specific Java code here
    │   ├── kotlin/         # JVM-specific Kotlin code here
    │   └── resources/      # JVM-specific resources here
    ├── jvmTest/
    │   ├── java/           # JVM-specific test code written in Java here
    │   ├── kotlin/         # JVM-specific test code written in Kotlin here
    │   └── resources/      # JVM-specific test resources here
    │
    ├── jsMain/
    │   └── kotlin/         # JS-specific Kotlin code here
    ├── jsTest/
    │   └── kotlin/         # JS-specific test code written in Kotlin here
    │
    ├── <TARGET>Main/       
    │   └── kotlin/         # <TARGET>-specific Kotlin code here
    └── <TARGET>Test/
        └── kotlin/         # <TARGET>-specific test code written in Kotlin here
```

---

## Kotlin multi-platform build configuration (pt. 1)

Defines several aspects of the project:

- which version of the Kotlin compiler to adopt
    ```kotlin
    plugins {
        kotlin("multiplatform") version "2.4.21" // defines plugin and compiler version
    }
    ```
    * better: declare versions in a [version catalog](https://docs.gradle.org/current/userguide/version_catalogs.html) (`gradle/libs.versions.toml`)

- which repositories Gradle should use when looking for dependencies
    ```kotlin
    repositories { 
        mavenCentral() // use MCR for downloading dependencies (recommended)

        // other custom repositories here (discouraged)
    } 
    ```

---

## Kotlin multi-platform build configuration (pt. 2)

Defines several aspects of the project:

- which platforms to target (reference [here](https://kotlinlang.org/docs/multiplatform-dsl-reference.html#targets))
    ```kotlin
    kotlin {
        jvmToolchain(17) // JDK used to compile (and run tests on) the JVM target

        // declares JVM as target (Java sources in src/jvmMain/java are supported out of the box)
        jvm()

        // declares JavaScript as target
        js {
            useEsModules() // generate ES modules (or useCommonJs())
            binaries.library() // generate a reusable npm package (or binaries.executable())
            // the target will consist of a Node project (with NodeJS's stdlib)
            nodejs {
                testTask { /* configure Node's testing frameworks */ }
            }
            // alternatively, or additionally to nodejs:
            browser { /* ... */ }
        }

        // other targets here
    }
    ```

- other admissible targets:
    * `wasmJs`, `wasmWasi`, `android`
    * various native sub-targets (details [here](https://kotlinlang.org/docs/native-target-support.html))

---

## Kotlin multi-platform build configuration (pt. 3)

- which third-party libraries each target should depend upon
    ```kotlin
    kotlin {
        sourceSets {
            // the Kotlin std-lib is added automatically to all source sets
            commonMain.dependencies {
                implementation("group.of:multiplatform-library:X.Y.Z") // or api
            }
            commonTest.dependencies {
                // kotlin.test, automatically resolved to the right variant for each target
                implementation(kotlin("test"))
            }
            jvmMain.dependencies {
                implementation("group.of:jvm-library:X.Y.Z") // or api
            }
            jsMain.dependencies {
                implementation(npm("npm-module", "X.Y.Z")) // lookup on https://www.npmjs.com
            }
        }
    }
    ```
    * platform-specific test source sets (e.g. `jvmTest`, `jsTest`) can declare their own dependencies too

---

## Kotlin multi-platform build configuration (pt. 4)

- configure Kotlin compiler options, for all targets (or per target, e.g. `jvm { compilerOptions { ... } }`)
    ```kotlin
    kotlin {
        compilerOptions {
            // source compatibility with the specified version of Kotlin
            languageVersion = KotlinVersion.KOTLIN_2_3
            // allows using declarations only from the specified version of Kotlin bundled libraries
            apiVersion = KotlinVersion.KOTLIN_2_3
            // allows using the specified opt-in API
            optIn.add("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
            // enables progressive mode
            progressiveMode = true // false by default
            // turns warnings into errors
            allWarningsAsErrors = true
        }
    }
    ```

- details about:
    * compiler options [here](https://kotlinlang.org/docs/gradle-compiler-options.html)
    * opt-ins [here](https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-to-using-api)

---

## Build configuration: full example

{{% multicol %}}{{% col class="col-6" %}}
```kotlin
plugins {
    kotlin("multiplatform") version "2.4.21"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    jvm()
    js {
        useEsModules()
        binaries.library()
        nodejs()
    }
    compilerOptions {
        allWarningsAsErrors = true
    }
```
{{% /col %}}{{% col class="col-6" %}}
```kotlin
    sourceSets {
        commonMain.dependencies {
            implementation("group.of:multiplatform-library:X.Y.Z")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmMain.dependencies {
            implementation("group.of:jvm-library:X.Y.Z")
        }
        jsMain.dependencies {
            implementation(npm("npm-module", "X.Y.Z"))
        }
    }
}
```
{{% /col %}}{{% /multicol %}}

- The build file of the running example is [here](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/build.gradle.kts)

---

## Gradle tasks for multi-platform projects (pt. 1)

Let `T` denote the target name (e.g. `jvm`, `js`, etc.); recall Gradle's lifecycle tasks from the [build automation lecture](../04-build-automation/)

- `<T>MainClasses` compiles the main code for platform `T`
    + e.g. `jvmMainClasses`, `jsMainClasses`

- `<T>TestClasses` compiles the test code for platform `T`
    + e.g. `jvmTestClasses`, `jsTestClasses`

- `<T>Jar` compiles the main code for platform `T` and produces a JAR out of it
    + e.g. `jvmJar`, `jsJar`
    + depends on (hence implies) `<T>MainClasses`, but NOT `<T>TestClasses`

- `<T>Test` executes tests for platform `T`
    + e.g. `jvmTest`, `jsTest`
    + depends on (hence implies) `<T>MainClasses`, AND on `<T>TestClasses`
    + `jsTest` aggregates `jsNodeTest` and `jsBrowserTest` (depending on the enabled sub-targets)

---

## Gradle tasks for multi-platform projects (pt. 2)

- `jsProductionExecutableCompileSync` (resp. `jsProductionLibraryCompileSync`) compiles the JS main code into a Node project
    * requires the `js` target to be enabled
    * requires the `binaries.executable()` (resp. `binaries.library()`) configuration to be enabled

- `assemble` creates all JARs / klibs (hence compiling the main code for all platforms)
    + it also generates documentation and sources JARs
        * if publishing is configured

- `allTests` executes tests for all platforms, and creates an aggregated report

- `check` like `allTests` but it may also include other checks (e.g. style) if configured

- `build` $\approx$ `check` + `assemble`

---

## Multi-platform Kotlin sources (common)

- Ordinary Kotlin sources

- When in `common`:
    + only the platform-agnostic std-lib can be used
        * API reference [here](https://kotlinlang.org/api/core/kotlin-stdlib/) (filter by the `Common` platform)
    + one may use third-party libraries, as long as they are __multi-platform__ too
    + one may use the [`expect` keyword](https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual.html)
    + one may use platform-specific annotations 
        * e.g. `@JvmStatic`, `@JsName`, etc.

---

## Multi-platform Kotlin sources (platform-specific)

Let `T` denote some target platform

- When in `T`-specific source sets
    + one may use the `T`-specific std-lib
    + one may use the `T`-specific Kotlin libraries
    + one may use the `T`-specific libraries
    + one may use the [`actual` keyword](https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual.html)

- Each platform `T` may allow for specific keywords
    * e.g. the [`external` modifier](https://kotlinlang.org/docs/js-interop.html#external-modifier)

---

## The `expect`/`actual` mechanism for specialising common API

- Declaring an `expect`ed function / type __declaration__ in common code...

- ... enforces the existence of a corresponding `actual` __definition__ for _all_ platforms
    * compiler won't succeed until an actual definition is provided _for each target_

{{< figure src="expect-actual.svg" width="60%" alt="The expect declaration of a readText(path, encoding) function in commonMain (Files.kt), with its two actual definitions: in jvmMain (Files.jvm.kt) it reads the file via java.io.File, in jsMain (Files.js.kt) via Node's fs.readFileSync, declared as an external object. Adding a target without an actual definition is a compilation error." >}}

---

## Workflow (top-down)

1. Draw a platform-agnostic design for your domain entities
    + keep in mind that you can only rely on a very small std-lib / runtime
    + keep in mind that some APIs may be missing from the common std-lib:
        - e.g. file system APIs
    + try to reason in a platform-agnostic way
        - e.g. file system makes no sense for in-browser JS
    + separate interfaces from classes
    + rely on abstract classes with [template methods](https://en.wikipedia.org/wiki/Template_method_pattern) to maximise common code
    + use `expect` keyword to declare platform-agnostic factories

2. Assess the _abstraction gap_, __for each target platform__

3. For each target platform:
    + draw a platform-specific design extending the platform-agnostic one
    + ... and filling the abstraction gap for that target
    + implement interfaces via platform-specific classes
    + implement platform-specific concrete classes for common abstract classes
    + use `actual` keyword to implement platform-specific factories

---

{{% section %}}

{{< slide id="running-example" >}}

# Running example

## Multi-platform CSV parsing lib

- CSV (comma-separated values) files, e.g.:
    ```bash
    # HEADER_1, HEADER_2, HEADER_3, ...
    # character '#' denotes the beginning of a single-line comment
    # first line conventionally denotes column names (headers)

    field1, field2, field3, ...
    # character ',' acts as field separator
    
    "field with spaces", "another field, with comma", "yet another field", ...
    # character '"' acts as field delimiter

    # other characters may be used to denote comments, separators, or delimiters
    ```

- JVM and JS std-lib do not provide direct support for CSV

- Requirements:
    * library for parsing / manipulating CSV files
    * parsing = loading from file
    * manipulating = creating / editing / saving CSV files, programmatically
    * support for both JVM and JS

---

## About the example

- We'll follow a domain-driven approach:
    1. focus on designing / implementing the domain entities
    2. then, we'll add functionalities for manipulating them
    3. finally, we'll write unit and integration tests

- Domain entities (can be realised as common code):
    - `Table`: in-memory representation of a CSV file
    - `Row`: generic row in a CSV file
    - `Header`: special row containing the names of the columns
    - `Record`: special row containing the values of a line in a CSV file

- Main functionalities (some of which require platform-specific code):
    1. creating a `Table` programmatically
    2. reading columns, rows, and values from a `Table`
    3. parsing a CSV file into a `Table`
    4. saving a `Table` into a CSV file

- Full code: [`unibo-spe/multiplatform-example`](https://github.com/unibo-spe/multiplatform-example) (module `csv-core`)

---

## Domain entities

{{< plantuml width="50%" >}}
top to bottom direction

package "kotlin" {
    interface Iterable<T>
}

package "io github gciatto csv" {

    interface Row {
        + get(index: Int): String
        + size: Int
    }

    Row -up-|> Iterable: //T// = String

    interface Header {
        + columns: List<String>
        + contains(column: String): Boolean
        + indexOf(column: String): Int
    }

    Row <|-- Header

    interface Record {
        + header: Header
        + values: List<String>
        + contains(value: String): Boolean
        + get(column: String): String
    }

    Row <|-- Record

    Record "*" *-right- "1" Header

    interface Table {
        + header: Header
        + records: List<Record>
        + get(row: Int): Row
        + size: Int
    }
    
    Table "*" *-u- "1" Header
    Table "*" o-u- "*" Record
    Table -u----|> Iterable: //T// = Row
}
{{< /plantuml >}}

---

## The `Row` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Row.kt" from="6" to="16" >}}

- Please ignore the `@JsExport` and `@JsName` annotations for now, they are explained [later](#/kotlin-interop)

--- 

## The `Header` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Header.kt" from="8" to="22" >}}

---

## The `Record` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Record.kt" from="8" to="26" >}}

---

## The `Table` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Table.kt" from="8" to="26" >}}

---

## Common implementations of domain entities

{{% plantuml %}}
top to bottom direction

package "io github gciatto csv **impl**" {
    interface Row

    abstract class AbstractRow {
        - values: List<String>
        # constructor(values: List<String>)
        # toString(type: String?): String
    }

    Row <|-d- AbstractRow

    interface Header

    class DefaultHeader {
        - indexesByName: Map<String, Int>
        + constructor(columns: Iterable<String>)
    }

    Row <|-d- Header
    Header <|-d- DefaultHeader
    AbstractRow <|-d- DefaultHeader

    interface Record

    class DefaultRecord {
        + constructor(header: Header, values: Iterable<String>)
    }

    Row <|-d- Record
    Record <|-- DefaultRecord
    AbstractRow <|-d- DefaultRecord

    class DefaultTable {
        + constructor(header: Header, records: Iterable<Record>)
    }

    interface Table

    Table <|-- DefaultTable
}
{{% /plantuml %}}

---

## The `AbstractRow` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/AbstractRow.kt" from="5" to="34" >}}

- Base implementation for the `Row` interface: `toString(type)` renders rows as `<type>("field1", "field2", ...)`

---

## The `DefaultHeader` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/DefaultHeader.kt" from="5" to="20" >}}

- `indexesByName` is a lazily-computed cache of column indexes, for faster lookup

---

## The `DefaultRecord` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/DefaultRecord.kt" from="6" to="27" >}}

---

## The `DefaultTable` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/DefaultTable.kt" from="8" to="34" >}}

---

## Need for factory methods

- To enforce separation between API and implementation code, it's better:
    * to keep interfaces public, and classes internal
    * to provide factory methods for creating instances of the interfaces

- Convention in Kotlin is to create factory methods as package-level `fun`ctions
    * e.g. contained into the `io/github/gciatto/csv/Csv.kt` file

- Factory methods may be named after the concept they create: `<concept>Of(args...)`
    * e.g. `headerOf(columns...)`, `recordOf(header, values...)`, etc.

- Class diagram:
    
    {{< plantuml width="30%">}}
    @startuml
    skinparam monochrome false
    class Csv{
        + headerOf(columns): Header
        + anonymousHeader(size: Int): Header
        ..
        + recordOf(header, values): Record
        ..
        + tableOf(header, records): Table
        + tableOf(rows): Table
    }
    @enduml
    {{< /plantuml >}}

---

## The `Csv.kt` file (pt. 1)

Factories for headers (possibly _anonymous_, i.e. with columns named after their index), records, and tables:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Csv.kt" from="19" to="39" >}}

- Notice that each factory method is __overloaded__
    + to support `Iterable`, `Sequence`, and `vararg` arguments
    + this is convenient for Kotlin programmers who will use our library

---

## The `Csv.kt` file (pt. 2)

Table creation from rows (anonymous header if none is provided):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Csv.kt" from="41" to="58" >}}

---

## Time for unit testing (pt. 1)

Dummy instances object, in [Tables.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/Tables.kt):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/Tables.kt" from="3" to="20" >}}

---

## Time for unit testing (pt. 2)

Some basic tests in [TestCSV.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCSV.kt) (bad way of writing test methods, don't do this at home):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCSV.kt" from="19" to="34" >}}


- Run tests via Gradle task `allTests` (also try to run tests for specific platforms, e.g. `jvmTest` or `jsTest`)

--- 

## Summary of what we did so far

1. We designed a set of domain entities
    * `Row`, `Header`, `Record`, `Table`
    * we provided a set of factory methods for creating instances of those entities

2. We implemented the domain entities as common code

3. We wrote some unit tests for the domain entities
    * again as common code

4. Let's focus now on more complex functionalities
    * e.g. parsing a CSV file into a `Table`
    * e.g. saving a `Table` into a CSV file
    * all such features require I/O functionalities

> I/O is platform-specific, hence we need platform-specific code

---

{{< slide id="platform-specific-plan" >}}

## How much platform-specific code is needed?

- I/O functionalities are supported by fairly different APIs in the JVM and JS
    * e.g. JVM's [`java.io` package](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/package-summary.html) vs. JS' [`fs` module](https://nodejs.org/api/fs.html)
    * sadly, Kotlin std-lib does not provide a common API for I/O

- Do we need to rewrite the same business logic twice? (once for JVM, once for JS)
    * yes, but we can minimise the amount of code to be rewritten

- Let's try to decompose the problem as follows:
    * parsing CSV into `Table`: file $\rightarrow$ string(s) $\rightarrow$ `Table`
    * saving `Table` as CSV file: `Table` $\rightarrow$ string(s) $\rightarrow$ file

- Remarks:
    1. only the "file $\leftrightarrow$ string(s)" part is platform-specific
    2. conversely, the "string(s) $\leftrightarrow$ `Table`" part is platform-agnostic
        + let's realise this one first, as common code

---

## Three more entities to be modelled

- `Configuration`: set of characters to be used to parse / represent CSV files
    * e.g. separator, delimiter, comment, etc.

- `Formatter`: converts `Row`s into strings, according to some `Configuration`

- `Parser`: converts some source into a `Table`, according to some `Configuration`
    * source $\approx$ anything that can be interpreted as a string to be parsed (e.g. file, string, etc.)

{{< plantuml width="35%">}}
@startuml
skinparam monochrome false

package "io github gciatto csv" {

    class Configuration {
        + separator: Char
        + delimiter: Char
        + comment: Char
        --
        + isComment(string: String): Boolean
        + isRecord(string: String): Boolean
        + isHeader(string: String): Boolean
        ..
        + getComment(string: String): String?
        + getFields(string: String): List<String>
        + getColumnNames(string: String): List<String>
    }

    interface Formatter {
        + source: Iterable<Row>
        + configuration: Configuration
        + format(): Iterable<String>
    }

    interface Parser {
        + source: Any
        + configuration: Configuration
        + parse(): Iterable<Row>
    }

    Formatter "1" o-- "1" Configuration
    Parser "1" o-- "1" Configuration

}
@enduml
{{< /plantuml >}}

---

## The `Configuration` class

```kotlin
data class Configuration(val separator: Char, val delimiter: Char, val comment: Char) {
    // Checks whether the given string is a comment (i.e. starts with the comment character).
    fun isComment(string: String): Boolean = TODO()

    // Gets the content of the comment line (i.e. removes the initial comment character).
    fun getComment(string: String): String? = TODO()

    // Checks whether the given string is a record (i.e. it contains at least one field)
    fun isRecord(string: String): Boolean = TODO()

    // Retrieves the fields in the given record (i.e. splits the string at the separator character)
    fun getFields(string: String): List<String> = TODO()

    // Checks whether the given string is a header (i.e. a comment whose content is a record)
    fun isHeader(string: String): Boolean = TODO()

    // Retrieves the column names in the given header
    fun getColumnNames(string: String): List<String> = TODO()
}
```
* implementation of methods is long and boring, but straightforward
    - it relies on regular expressions, which are supported by Kotlin's common std-lib
    - details in [Configuration.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Configuration.kt)

---

## The `Formatter` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Formatter.kt" from="8" to="22" >}}

---

## The `Parser` interface

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Parser.kt" from="8" to="22" >}}

--- 

## Common implementation of novel entities

{{< plantuml >}}
@startuml
skinparam monochrome false

package "io github gciatto csv impl" {

    class Configuration
    interface Formatter
    interface Parser

    DefaultFormatter "1" o-- "1" Configuration
    AbstractParser "1" o-- "1" Configuration

    class DefaultFormatter {
        + constructor(source: Iterable<Row>, configuration: Configuration)
        - formatRow(row: Row): String
        - formatAsHeader(row: Row): String
        - formatAsRecord(row: Row): String
    }

    Formatter <|-- DefaultFormatter

    abstract class AbstractParser {
        # constructor(source: Any, configuration: Configuration)
        # beforeParsing()
        # afterParsing()
        # {abstract} sourceAsLines(): Sequence<String>
    }

    Parser <|-- AbstractParser

    class StringParser {
        + source: String
        + constructor(source: String, configuration: Configuration)
        + sourceAsLines(): Sequence<String>
    }

    AbstractParser <|-- StringParser
}
@enduml
{{< /plantuml >}}

---

## The `DefaultFormatter` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/DefaultFormatter.kt" from="8" to="26" >}}

- `format` _lazily_ converts each row into a string: headers are prefixed by the comment character, fields are wrapped by the delimiter character

---

## The `AbstractParser` class (pt. 1)

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/AbstractParser.kt" from="11" to="21" >}}

- `beforeParsing` / `afterParsing` are _hooks_, `sourceAsLines` is the _abstract step_ of a [template method](https://en.wikipedia.org/wiki/Template_method_pattern)

---

## The `AbstractParser` class (pt. 2)

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/AbstractParser.kt" from="24" to="56" >}}

- `sequence { ... yield(...) }` produces rows _lazily_, one at a time
- `finally` ensures `afterParsing()` is called upon errors too (not if the consumer stops iterating early)

---

## The `StringParser` class

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/impl/StringParser.kt" from="5" to="7" >}}

- it simply splits the source string into lines

--- 

## Providing functionalities via extension methods

Additions to the `Csv.kt` file:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Csv.kt" from="15" to="17" >}}

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Csv.kt" from="60" to="72" >}}

- `formatAsCSV` converts any container of rows into a CSV string, `parseAsCSV` parses a CSV string into a table

---

## Unit tests and usage examples (pt. 1)

Dummy instances for testing (other constants in [CsvStrings.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/CsvStrings.kt)):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/CsvStrings.kt" from="3" to="16" >}}
    
---

## Unit tests and usage examples (pt. 2)

{{% multicol %}}{{% col class="col-6" %}}

Tests involving parsing be like:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCSV.kt" from="50" to="57" >}}

{{% /col %}}{{% col class="col-6" %}}

Tests involving formatting be like:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCSV.kt" from="93" to="100" >}}

{{% /col %}}{{% /multicol %}}

--- 

## Time to go platform-specific

- Further additions to the `Csv.kt` file (reading and parsing a CSV file from the given path):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonMain/kotlin/io/github/gciatto/csv/Csv.kt" from="74" to="79" >}}

- notice the `expect` keyword, and the lack of function body
- we're just declaring the signature of a platform-specific function
- there is no type for representing paths in Kotlin's common std-lib
    - hence we're using `String` as a platform-agnostic representation of paths
        - this is a suboptimal choice

--- 

## Time to go platform-specific on the JVM (pt. 1)

- I/O (over textual files) is mainly supported by means of the following classes:
    * [`java.io.File`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/File.html)
    * [`java.io.BufferedReader`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/BufferedReader.html)
    * [`java.io.BufferedWriter`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/BufferedWriter.html)

- Buffered readers support reading a file __line-by-line__

- On Kotlin/JVM, Java's std-lib is available as Kotlin's std-lib
    * hence, we may use Java's std-lib directly
    * the abstraction gap is close to 0

- We may simply create a new sub-type of `AbstractParser`
    * using `File`s as sources
    * creating a `BufferedReader` behind the scenes to read files line-by-line

--- 

## Time to go platform-specific on the JVM (pt. 2)

In the `jvmMain` source set

A JVM-specific parser, [FileParser.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jvmMain/kotlin/io/github/gciatto/csv/FileParser.kt):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jvmMain/kotlin/io/github/gciatto/csv/FileParser.kt" from="8" to="20" >}}

- the source is now forced to be a `File`
- the `lateinit` reader is opened before parsing, and closed afterwards
- the file is read lazily, line-by-line

---

## Time to go platform-specific on the JVM (pt. 3)

The [Csv.jvm.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jvmMain/kotlin/io/github/gciatto/csv/Csv.jvm.kt) file:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jvmMain/kotlin/io/github/gciatto/csv/Csv.jvm.kt" from="1" to="13" >}}

- this is how `parseCsvFile` is implemented __on the JVM__
- notice the `actual` keyword, and the presence of a function body
- notice the usage of `FileParser` in the function body
    + `FileParser` is an `internal` class filling the abstraction gap on the JVM
- `@file:JvmName` is explained [later](#/kotlin-interop)


--- 

## Time to go platform-specific on the JS (pt. 1)

- I/O (over textual files) is mainly supported by means of the following things:
    * the [`fs` module](https://nodejs.org/api/fs.html)
    * the [`fs.readFileSync` function](https://nodejs.org/api/fs.html#fsreadfilesyncpath-options)
    * the [`fs.writeFileSync` function](https://nodejs.org/api/fs.html#fswritefilesyncfile-data-options)

- These functions support reading / writing a file __in one shot__
    * i.e. they return / generate the _whole_ content of the file as a string
    * quite inefficient if the file is big

- On Kotlin/JS, Node's std-lib is __not__ directly available
    * one must instruct the compiler about
        1. __where__ to look up std-lib functions (module name)
        2. __how__ to map JS functions to Kotlin functions (`external` declarations)
    * the abstraction gap is non-negligible

- To-do list for Kotlin/JS:
    1. create `external` declarations for the Node's std-lib functions to be used
    2. implement JS-specific code via the `StringParser` (after reading the whole file)

--- 

## Time to go platform-specific on the JS (pt. 2)

In the `jsMain` source set

First, let's create the [NodeFs.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jsMain/kotlin/io/github/gciatto/csv/NodeFs.kt) file (containing `external` declarations for the `fs` module):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jsMain/kotlin/io/github/gciatto/csv/NodeFs.kt" from="1" to="7" >}}

- Kotlin mappings for [`readFileSync`](https://nodejs.org/api/fs.html#fsreadfilesyncpath-options) and [`writeFileSync`](https://nodejs.org/api/fs.html#fswritefilesyncfile-data-options)
- the `@file:JsModule("fs")` annotation instructs the compiler that the declarations of this file come from the `fs` module
- `external` declarations are Kotlin signatures of JS functions
- `dynamic` is a special type that can be used to represent any JS object
    - it overrides Kotlin's type system, hence it should be used with care
    - it prevents developers from declaring too much `external` stuff
    - good to use when the JS API is not known in advance or uses union types
- `definedExternally` is stating that a parameter is optional (default value is defined in JS)

--- 

## Time to go platform-specific on the JS (pt. 3)

Then, let's create the [Csv.js.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jsMain/kotlin/io/github/gciatto/csv/Csv.js.kt) file containing:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jsMain/kotlin/io/github/gciatto/csv/Csv.js.kt" from="5" to="11" >}}

- this is how the `parseCsvFile` is implemented __on JS__
- notice the `actual` keyword, and the presence of a function body
- notice the usage of `readFileSync` to read a file as a string in one shot
- notice the exploitation of common code for parsing the string into a `Table` (namely, `parseAsCSV`)
- `@JsExport` makes the function callable from JS code (see [later](#/kotlin-interop))
- notice the `js("...")` magic function
    - it allows JS code to be written directly in Kotlin
    - the provided string should contain bare JS code, which the compiler will output as-is
    - it's a way to fill the abstraction gap on JS
    - we use it to create a JS object on the fly, to provide optional parameters to `readFileSync`

---

## How much platform-specific code did we write?

{{< figure src="platform-code.svg" width="70%" alt="Bar chart of the lines of Kotlin code per source set of csv-core: commonMain 288 (91% of main code), jvmMain 17, jsMain 13; commonTest 227 (93% of test code), jvmTest 6, jsTest 10, the latter two being the actual definitions of a test helper creating temporary files. Only the file-to-string I/O is platform-specific." >}}

- Recall the [plan](#/platform-specific-plan): only the "file $\leftrightarrow$ string(s)" part is platform-specific

---

## Platform-agnostic testing (strategy)

- We may test our CSV library in common-code
    + so as to avoid repeating testing code

- The test suite may:   
    1. create temporary files with known paths, containing known CSV data
    2. read those paths, and parse them as CSV
    3. assert that the parsed tables are as expected

- The hard part is __step 1__: two platform-specific steps
    1. discovering the temp directory of the current system
    2. writing a file

--- 

## Multi-platform testing (preliminaries, pt. 1)

- file [Utils.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/Utils.kt) in `commonTest` (creates a temporary file with the given name and content, and returns its path):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/Utils.kt" from="3" to="12" >}}

- notice the `expect`ed function

- file [Utils.jvm.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jvmTest/kotlin/io/github/gciatto/csv/Utils.jvm.kt) in `jvmTest`:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jvmTest/kotlin/io/github/gciatto/csv/Utils.jvm.kt" from="5" to="10" >}}

- cf. documentation of method [`File.createTempFile`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/File.html#createTempFile(java.lang.String,java.lang.String))

---

## Multi-platform testing (preliminaries, pt. 2)

- file [NodeOs.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jsMain/kotlin/io/github/gciatto/csv/NodeOs.kt) in `jsMain` (`external` declaration for the `os` module):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jsMain/kotlin/io/github/gciatto/csv/NodeOs.kt" from="1" to="5" >}}

- file [Utils.js.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/jsTest/kotlin/io/github/gciatto/csv/Utils.js.kt) in `jsTest`:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/jsTest/kotlin/io/github/gciatto/csv/Utils.js.kt" from="3" to="15" >}}

- cf. documentation of [`os.tmpdir`](https://nodejs.org/api/os.html#ostmpdir)
- `kotlin.random.Random` is part of the common std-lib, so no JS-specific code is needed for random numbers

---

## Multi-platform testing (actual tests)

- file [CsvFiles.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/CsvFiles.kt) in `commonTest` (paths of temporary files, lazily created upon first usage, containing the strings in `CsvStrings`):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/CsvFiles.kt" from="3" to="10" >}}

- tests for parsing (in [TestCsvFiles.kt](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCsvFiles.kt)) be like:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-core/src/commonTest/kotlin/io/github/gciatto/csv/TestCsvFiles.kt" from="8" to="13" >}}

{{% /section %}}

---

{{% section %}}

## Output of a multi-platform build

- Kotlin multi-platform libraries are published as a set of artifacts (one per target, plus _Gradle module metadata_)
    * enabling importing the project as a dependency in other multi-platform projects

- The `jvmMain` source set is compiled into a JVM-compliant JAR
    * enabling importing the project as a dependency in JVM projects
    * enabling the creation of runnable JARs
    * via the various `*Jar` or `assemble` tasks

- The `jsMain` source set is compiled into either
    * a Kotlin library (`.klib`), enabling importing the project as a dependency in Kotlin/JS projects
        + via the various `*Jar` or `assemble` tasks
    * a NodeJS project
        + via the `jsProductionExecutableCompileSync` (or `jsProductionLibraryCompileSync`) task

--- 

## Output of JVM compilation

- Task for JVM-only compilation of re-usable packages: `jvmJar`

- Effect: 
    1. compile the project as JVM bytecode 
    2. pack the bytecode into a JAR file
    3. move the JAR file into `$PROJ_DIR/build/libs/$PROJ_NAME-jvm-$PROJ_VERSION.jar`

- The JAR does not contain dependencies

- Ad-hoc Gradle plugins or code are needed to create a [fat JAR](https://www.baeldung.com/gradle-fat-jar)
    * e.g. the running example uses the [Shadow](https://gradleup.com/shadow/) plugin, cf. task `shadowJar`

--- 

## Output of JS compilation

- Task for JS-only compilation of re-usable packages: `jsProductionLibraryCompileSync` (with `binaries.library()`)
    * or `jsProductionExecutableCompileSync` (with `binaries.executable()`)

- Effect:
    1. Generate Node project into `$ROOT_PROJ_DIR/build/js/packages/$ROOT_PROJ_NAME-$PROJ_NAME`:
        - JS code (`.mjs` files, when using ES modules)
        - `package.json` file
    2. [Dead code elimination](https://kotlinlang.org/docs/js-ir-compiler.html) removing unused code
        - i.e. code which is neither exported nor (indirectly) called by `main`

- Support for packing as npm package via the [`npm-publish`](https://npm-publish.petuska.dev/) Gradle plugin

{{% /section %}}

---

## Output of a multi-platform build (overview)

{{< figure src="build-outputs.svg" width="75%" alt="Pipeline from source sets to repositories: commonMain plus each platform-specific source set (JS, JVM, Native, Wasm) goes through the shared K2 front-end, producing IR, then through one back-end per target. The JS back-end produces .klib or .mjs plus package.json, the JVM back-end a .jar, the Native back-end .klib or executables and libraries, the Wasm back-end .klib or .wasm plus a JS loader. Artifacts are published to Maven Central, tied together by Gradle module metadata, and JS packages optionally to npm." >}}

---

{{% section %}}

{{< slide id="kotlin-interop" >}}

## Calling Kotlin from other platforms

- Kotlin code can be called from the target platforms' main languages
    + e.g. Java, JavaScript, etc.

- Understanding the mapping between Kotlin and other languages is key
    + it impacts the usability of Kotlin libraries for ordinary platform users

> How are Kotlin's syntactical categories mapped to other platforms/languages?

+ the answer varies on a per-platform basis

---

## Kotlin--Java Mapping (pt. 1)

- Kotlin class $\equiv$ Java class

{{% multicol %}}
{{% col %}}
```kotlin
class MyClass {}
interface MyType {}
```
{{% /col %}}
{{% col %}}
```java
public class MyClass {}
public interface MyType {}
```
{{% /col %}}
{{% /multicol %}}

- No syntactical difference between primitive and reference types
    + `Int` $\leftrightarrow$ `int` / `Integer`, `Short` $\leftrightarrow$ `short` / `Short`, etc.

{{% multicol %}}
{{% col %}}
```kotlin
val x: Int = 0
val y: Int? = null
```
{{% /col %}}
{{% col %}}
```java
int x = 0;
Integer y = null;
```
{{% /col %}}
{{% /multicol %}}

- Some Kotlin types are replaced by Java types at compile time
    + e.g. `Any` $\rightarrow$ `Object`, `kotlin.collections.List` $\rightarrow$ `java.util.List`, etc.

{{% multicol %}}
{{% col %}}
```kotlin
val x: Any = "ciao"
val y: kotlin.collections.List<Int> = listOf(1, 2, 3)
val z: kotlin.collections.MutableList<String> = mutableListOf("a", "b", "c")
```
{{% /col %}}
{{% col %}}
```java
Object x = "ciao";
java.util.List<Integer> y = java.util.Arrays.asList(1, 2, 3);
java.util.List<String> z = java.util.List.of("a", "b", "c");
```
{{% /col %}}
{{% /multicol %}}

- Other Kotlin types are mapped to homonymous Java types

---

## Kotlin--Java Mapping (pt. 2)

- Kotlin properties are mapped to getter / setter methods

{{% multicol %}}
{{% col %}}
```kotlin
interface MyType {
    val x: Int
    var y: String
}
```
{{% /col %}}
{{% col %}}
```java
public interface MyType {
    int getX();
    String getY(); void setY(String y);
}
```
{{% /col %}}
{{% /multicol %}}

- ... unless the `JvmField` annotation is adopted

{{% multicol %}}
{{% col %}}
```kotlin
import kotlin.jvm.JvmField
class MyType {
    @JvmField
    val x: Int = 0
    @JvmField
    var y: String = ""
}
```
{{% /col %}}
{{% col %}}
```java
public class MyType {
    public final int x = 0;
    public String y = "";
}
```
{{% /col %}}
{{% /multicol %}}

- Kotlin's top-level functions in file `X.kt` are mapped to static methods of class `XKt`

{{% multicol %}}
{{% col %}}
```kotlin
// file MyFile.kt
fun f() {}
```
{{% /col %}}
{{% col %}}
```java
public final class MyFileKt {
    public static void f() {}
}
```
{{% /col %}}
{{% /multicol %}}

- ... unless the `JvmName` annotation is exploited (e.g. `@file:JvmName("Csv")` in our `Csv.kt`)

{{% multicol %}}
{{% col %}}
```kotlin
@file:JvmName("MyModule")
import kotlin.jvm.JvmName
fun f() {}
```
{{% /col %}}
{{% col %}}
```java
public final class MyModule {
    public static void f() {}
}
```
{{% /col %}}
{{% /multicol %}}

---

## Kotlin--Java Mapping (pt. 3)

- Kotlin's `object X` is mapped to Java class `X` with 
    * private constructor
    * public static final field named `INSTANCE` to access

{{% multicol %}}
{{% col %}}
```kotlin
object MySingleton {}
```
{{% /col %}}
{{% col %}}
```java
public final class MySingleton {
    private MySingleton() {}
    public static final MySingleton INSTANCE = new MySingleton();
}
```
{{% /col %}}
{{% /multicol %}}

- Class `X`'s companion object is mapped to public static final field named `Companion` on class `X`

{{% multicol %}}
{{% col %}}
```kotlin
class MyType private constructor() {
    companion object {
        fun of(): MyType = MyType()
    }
}

// usage:
val x: MyType = MyType.of()
```
{{% /col %}}
{{% col %}}
```java
public class MyType {
    private MyType() {}
    public static final class Companion {
        public MyType of() { return new MyType(); }
    }
    public static final Companion Companion = new Companion();
}

// usage:
MyType x = MyType.Companion.of();
```
{{% /col %}}
{{% /multicol %}}

---

## Kotlin--Java Mapping (pt. 4)

- Class `X`'s companion object's member `M` tagged with `@JvmStatic` is mapped to static member `M` on class `X`

{{% multicol %}}
{{% col %}}
```kotlin
import kotlin.jvm.JvmStatic
class MyType private constructor() {
    companion object {
        @JvmStatic
        fun of(): MyType = MyType()
    }
}

// usage:
val x: MyType = MyType.of()
```
{{% /col %}}
{{% col %}}
```java
public class MyType {
    private MyType() {}
    public static MyType of() { return new MyType(); }
}

// usage:
MyType x = MyType.of();
```
{{% /col %}}
{{% /multicol %}}

- Kotlin's variadic functions are mapped to Java's variadic methods

{{% multicol %}}
{{% col %}}
```kotlin
fun f(vararg xs: Int) {
    val ys: IntArray = xs
}
```
{{% /col %}}
{{% col %}}
```java
void f(int... xs) {
    int[] ys = xs;
}
```
{{% /col %}}
{{% /multicol %}}

- Kotlin's extension methods are mapped to static Java methods with one more argument (the receiver)

{{% multicol %}}
{{% col %}}
```kotlin
// file MyFile.kt
class MyType { }
fun MyType.myMethod() {}

// usage:
val x = MyType()
x.myMethod() // postfix
```
{{% /col %}}
{{% col %}}
```java
public final class MyFileKt {
    public static void myMethod(MyType $this$myMethod) {}
}

// usage:
MyType x = new MyType();
MyFileKt.myMethod(x); // prefix
```
{{% /col %}}
{{% /multicol %}}

---

## Kotlin--Java Mapping (pt. 5)

- practical consequence: usability of fluent chains, in Java, is suboptimal
    + e.g. [sequence operations](https://kotlinlang.org/docs/sequences.html) are implemented as extension methods

{{% multicol %}}
{{% col %}}
```kotlin
import kotlin.sequences.*

val x = (0 ..< 10).asSequence() // 0, 1, 2, ..., 9
    .filter { it % 2 == 0 } // 0, 2, 4, ..., 8
    .map { it + 1 } // 1, 3, 5, ..., 9
    .sum() // 25
```
{{% /col %}}
{{% col %}}
```java
import static kotlin.sequences.SequencesKt.*;

int x = sumOfInt(
            map(
                filter(
                    asSequence(
                            new IntRange(0, 9).iterator()
                    ),
                    it -> it % 2 == 0
                ),
                it -> it + 1
            )
        );
```
{{% /col %}}
{{% /multicol %}}

- optional arguments exist in Kotlin but they do not exist in Java

{{% multicol %}}
{{% col %}}
```kotlin
fun f(a: Int = 1, b: Int = 2, c: Int = 3) { /* ... */ }

// usage:
f(b = 5)
```
{{% /col %}}
{{% col %}}
```java
void f(int a, int b, int c) { /* ... */ }

// usage:
f(1, 5, 3);
```
{{% /col %}}
{{% /multicol %}}

---

## Kotlin--Java Mapping (pt. 6)

- practical consequence: 
    + optional arguments are handy in Kotlin, but...
    + ... they are painful to use in Java
    + mitigation strategy: use `@JvmOverloads` to generate overloaded methods for Java
        * by design, the annotation only generates overloads dropping _trailing_ parameters

{{% multicol %}}
{{% col %}}
```kotlin
import kotlin.jvm.JvmOverloads

@JvmOverloads
fun f(a: Int = 1, b: Int = 2, c: Int = 3) { /* ... */ }
```
{{% /col %}}
{{% col %}}
```java
void f(int a, int b, int c) { /* ... */ }
void f(int a, int b) { f(a, b, 3); }
void f(int a) { f(a, 2, 3); }
void f() { f(1, 2, 3); }

// overloads that are not generated (some would even clash, having the same JVM signature):
// void f(int a, int c) { f(a, 2, c); }
// void f(int b, int c) { f(1, b, c); }
// void f(int c) { f(1, 2, c); }
// void f(int b) { f(1, b, 3); }
```
{{% /col %}}
{{% /multicol %}}

---

## Kotlin--Java Mapping (pt. 7)

- Kotlin has no _checked_ exceptions: use `@Throws` to let Java callers catch them
    ```kotlin
    @Throws(IOException::class)
    fun readFile(path: String): String { /* ... */ }  // Java: String readFile(String path) throws IOException
    ```

- Kotlin's nullability is exposed to Java via annotations (`@NotNull`, `@Nullable`)
    + Java types are seen in Kotlin as _platform types_ (e.g. `String!`), whose nullability is unknown

- `internal` members are `public` in the bytecode, but their names are _mangled_
    + e.g. `internal fun f()` in module `csv-core` becomes `f$csv_core()`, which is discouraging to call from Java

- Reference: [calling Kotlin from Java](https://kotlinlang.org/docs/java-to-kotlin-interop.html)

---

## Kotlin--Java Mapping Example

- Compile our CSV lib and import it as a dependency in a novel Java library

- Alternatively, add Java sources to the `jvmTest` source set, and use the library

- Listen to the teacher presenting key points

--- 

## Kotlin--JavaScript Mapping (pt. 1)

> __Disclaimer__: the generated JS code is not really meant to be read by humans

- DCE will eliminate unused code
    + "unused" $\equiv$ "neither exported, nor (indirectly) used by exported code or `main`"
    + code is exported by means of the `@JsExport` annotation
        * to be used on API types and functions
        * requires the `kotlin.js.ExperimentalJsExport` __opt-in__
    + not all syntactical constructs or types are currently exportable
        * members that cannot be exported can be excluded via `@JsExport.Ignore`
        * as a last resort, the warning can be suppressed (as the running example does)

{{% multicol %}}
{{% col %}}
```kotlin
package my.pkg

import kotlin.js.JsExport

@JsExport
class MyType {
    @JsExport.Ignore // Sequence is not exportable
    val numbers: Sequence<Int> = sequenceOf(1, 2, 3)
}

@JsExport
fun myFunction() { /* ... */ }
```
{{% /col %}}
{{% col %}}
```js
// ES modules: packages are flattened
import { MyType, myFunction } from "project-name";

// CommonJS / UMD: packages are preserved
const pkg = require("project-name").my.pkg;
const { MyType, myFunction } = pkg;
```
{{% /col %}}
{{% /multicol %}}

--- 

## Kotlin--JavaScript Mapping (pt. 2)

- Kotlin supports __overloading__, JS does not
    + class / interface member names are __mangled__ to avoid clashes
    + the `@JsName` annotation can be used to control the name of a member 
        * to be used on API types and functions

{{% multicol %}}
{{% col %}}
```kotlin
package my.pkg

@JsName("sumNumbers")
fun sum(vararg numbers: Int): Int = numbers.sum()

@JsName("sumIterableOfNumbers")
fun sum(numbers: Iterable<Int>): Int = numbers.sum()

@JsName("sumSequenceOfNumbers")
fun sum(numbers: Sequence<Int>): Int = numbers.sum()
```
{{% /col %}}
{{% col %}}
```js
import {
    sumNumbers,
    sumIterableOfNumbers,
    sumSequenceOfNumbers
} from "project-name";
```
{{% /col %}}
{{% /multicol %}}

- Kotlin class $\equiv$ [JS prototype](https://developer.mozilla.org/en-US/docs/Learn_web_development/Extensions/Advanced_JavaScript_objects/Object_prototypes) (with the default ES5 target)

{{% multicol %}}
{{% col %}}
```kotlin
class MyClass(private val argument: String) {
    @JsName("method")
    fun method(): String = argument + "!"
}
```
{{% /col %}}
{{% col %}}
```js
function MyClass(argument) {
    this.randomName_1 = argument
}
MyClass.prototype.method = function () {
    return this.randomName_1 + "!"
}
```
{{% /col %}}
{{% /multicol %}}

--- 

## Kotlin--JavaScript Mapping (pt. 3)

- Primitive type mappings are non-trivial (cf. [documentation](https://kotlinlang.org/docs/js-to-kotlin-interop.html#kotlin-types-in-javascript))
    + Kotlin numeric types, except for `kotlin.Long`, are mapped to JavaScript `Number`
    
    + `kotlin.Char` is mapped to a JS `Number` representing the character code.

    + Kotlin preserves overflow semantics for `kotlin.Int`, `kotlin.Byte`, `kotlin.Short`, `kotlin.Char` and `kotlin.Long`

    + `kotlin.Long` is emulated by a Kotlin class by default
        * it can be mapped to JS `BigInt` (_experimental_), with the `es2020` target or the `-Xes-long-as-bigint` compiler option

    + `kotlin.String` is mapped to JS `String`

    + `kotlin.Any` is mapped to JS `Object` (`new Object()`, `{}`, and so on)

    + `kotlin.Array` is mapped to JS `Array`

    + Kotlin collections are mapped to `KtList`, `KtSet`, `KtMap` (and their mutable variants)
        * which expose JS views via, e.g., `asJsReadonlyArrayView()`

    + `kotlin.Throwable` is mapped to JS `Error`

--- 

## Kotlin--JavaScript Mapping (pt. 4)

- practical consequence: no way to distinguish numbers by type
    ```kotlin
    val x: Int = 23
    val y: Any = x
    println(y as Float) // fails on JVM, works on JS
    ```

- Kotlin's `dynamic` overrides the type system, and it is translated "1-to-1"

```kotlin
val string1: String = "some string"
string1.missingMethod() // compilation error

val string2: dynamic = "some string"
string2.missingMethod() // compilation ok, runtime error
```

- Kotlin's common std-lib is implemented in JS
    * it is bundled into the compiled output (e.g. `kotlin-kotlin-stdlib.mjs`), and dead code is eliminated

--- 

## Kotlin--JavaScript Mapping (pt. 5)

- Companion objects are treated similarly to Kotlin/JVM

- Extension methods are treated similarly to Kotlin/JVM

- Variadic functions are compiled to JS functions accepting an array
    + which are not really variadic in JS

{{% multicol %}}
{{% col %}}
```kotlin
fun f(vararg xs: String) { /* ... */ }

// usage:
f("a")
f("a", "b")
f("a", "b", "c")
```
{{% /col %}}
{{% col %}}
```js
// usage:
f(["a"])
f(["a", "b"])
f(["a", "b", "c"])
```
{{% /col %}}
{{% /multicol %}}

{{% /section %}}

---

{{< slide id="mp-cicd" >}}

## Multi-platform CI/CD

Conceptual workflow (cf. build matrices in the [CI lecture](../07-ci/)):
1. Check style (e.g. via [KtLint](https://ktlint.github.io/)) of all Kotlin sources
2. Automatic bug detection (e.g. via [Detekt](https://github.com/detekt/detekt))
3. For each operating system `O` (e.g. Win, Mac, Linux):
    - for each target platform `T`:
        + for each relevant version `V` of the platform `T` (e.g. LTS releases + latest)
            1. ensure main code compiles (e.g. via Gradle task `<T>MainClasses`)
            2. ensure test code compiles (e.g. via Gradle task `<T>TestClasses`)
            3. ensure test passes (e.g. via Gradle task `<T>Test`)
    - Apple targets require macOS runners
4. If a release is needed (e.g. commit on the default branch)
    - for each target platform `T` $\cup$ `kotlin-multiplatform`:
        1. assemble compiled code into archive
        2. push archive on main repository `R` of platform `T`

- The running example's [workflows](https://github.com/unibo-spe/multiplatform-example/tree/master/.github/workflows) test the JVM and JS targets on 3 OSs $\times$ several JDK / Node versions

--- 

## About the practical CI/CD

- Even more complicated, because release on Maven Central is complex
    + since June 2025, via the [Central Publisher Portal](https://central.sonatype.org/publish/publish-portal-gradle/) (the legacy OSSRH service has been shut down)

- Setting up a CI/CD pipeline of this kind requires a lot of work
    + and Gradle / GitHub Actions boilerplate

- Recommendations:
    1. Use templates, such as [`template-kt-mpp-project`](https://github.com/gciatto/template-kt-mpp-project/)
    2. Use Gradle plugins, such as [`kt-mpp`](https://github.com/gciatto/kt-mpp) or [`vanniktech/gradle-maven-publish-plugin`](https://github.com/vanniktech/gradle-maven-publish-plugin)
    3. Use existing projects as examples, e.g. [`2p-kt`](https://github.com/tuProlog/2p-kt)

---

## Repositories by platform (pt. 1)

> __Takeaway__: each platform has some preferred main repository where users expect to find packages

- `JVM` $\rightarrow$ [Maven Central Repository](https://central.sonatype.com) (MCR)
    + other Maven repositories exist, but they are more fragile

- Kotlin / Multiplatform $\rightarrow$ MCR

- `JS` $\rightarrow$ [npm registry](https://www.npmjs.com/)

- `Android` $\rightarrow$ libraries: MCR or [Google's Maven repository](https://maven.google.com/); apps: [Google Play](https://play.google.com/store)

- `Mac` / `iOS` $\rightarrow$ libraries: [Swift Package Manager](https://www.swift.org/documentation/package-manager/) or [CocoaPods](https://cocoapods.org/); apps: [App Store](https://www.apple.com/app-store/)
    * or [Homebrew](https://brew.sh/), for command-line tools

---

## Repositories by platform (pt. 2)

- `Windows` $\rightarrow$ [Microsoft Store](https://apps.microsoft.com/)
    * or [Chocolatey](https://chocolatey.org/)
    * or [Scoop](https://scoop.sh/)

- `Linux` $\rightarrow$ depends on the distro   
    * e.g. [Arch Linux](https://archlinux.org/) $\rightarrow$ [AUR](https://aur.archlinux.org/)
    * inter-distro: [Flatpak](https://flatpak.org/) $\rightarrow$ [Flathub](https://flathub.org/)

- `Python` $\rightarrow$ [PyPI](https://pypi.org/)

- `.NET` $\rightarrow$ [NuGet](https://www.nuget.org/)

---

{{< slide id="jpype" >}}

# Multi-platform programming

## Write first, wrap elsewhere

---

## Write first, wrap elsewhere: the case of [JPype](https://jpype.readthedocs.io/)

- At the end of the day, the Python interpreter is C software

- Most commonly, efficient Python code is written in C and then wrapped into Python

- Put simply, the JVM too is C++ software

- Can Python libraries be written in Java (or Kotlin/JVM) and then wrapped into Python?

- Apparently yes, via several ad-hoc bridging technologies:
    * [JPype](https://jpype.readthedocs.io/) a bridge for calling JVM code from Python as a native library
    * [Jython](https://www.jython.org/) a JVM-based Python interpreter (supporting Python 2.7 only)
    * [GraalPy](https://www.graalvm.org/python/) a Python 3 implementation on GraalVM, interoperating with Java
    * [Py4J](https://www.py4j.org/) an RPC based bridge for calling Java from Python
    * [Pyjnius](https://pyjnius.readthedocs.io/) similar to JPype, but based on [Cython](https://cython.org)
    * and many others...

- Why exactly JPype?
    * still maintained, works with vanilla CPython, good documentation, good interoperability with JVM types

---

## JPype overview

1. Ensure you have a JVM installed on your system
    + with the `JAVA_HOME` environment variable set to the JVM installation directory

2. Ensure your compiled Java code is available as a `.jar` file
    + say, in `/path/to/my.jar`

3. Install the JPype package via `pip install JPype1`

4. You first need JPype to start a JVM instance in your Python process
    + by default, the JVM location is inferred from the `JAVA_HOME` environment variable

    ```python
    import jpype

    # start the JVM
    jpype.startJVM(classpath=["/path/to/my.jar"])
    ```

5. Once the JVM is started, one can import Java classes and call their methods as if they were Python objects

    ```python
    import jpype.imports # this is necessary to import Java classes

    from java.lang import System # import the java.lang.System class

    System.out.println("Hello World!")
    ```
    + `jpype.imports` works for well-known top-level domains (e.g. `java`, `javax`, `com`, `org`)
        * for other ones (e.g. `io.github.*`) use `jpype.JClass("...")` / `jpype.JPackage("...")`, or `jpype.imports.registerDomain("io")`
    + by default (`convertStrings=False`), Java methods return Java strings, to be converted via `str(...)` when needed

---

## JPype at runtime

{{< figure src="jpype-runtime.svg" width="75%" alt="One OS process containing the Python interpreter, the _jpype native module and the JVM. For instance, a Python script creates a java.util.ArrayList and adds an item to it. A Python call on a proxy object goes through the native module (overload selection, argument conversion, JNI call) to the JVM; the result is converted back or wrapped in a new proxy. The Python proxy holds a JNI global reference to the Java object in the JVM heap. There is no inter-process communication, and the JVM can be started only once per process." >}}

---

## JPype's Bridging Model (pt. 1)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#classes-objects)

- Java __classes__ are __presented__ as similarly to Python classes as possible

- the only major difference is that __Java classes__ and objects are _closed_ and __cannot be modified__
    + except via _customisers_ (see [later](#/jpype-customisers))

<div style="font-size: 0.70em">

| Description | Java | Python |
|---|---|---|
| Import a class | `import org.pkg.MyClass` | `from org.pkg import MyClass` |
| Import a class without a well-known TLD | `import zippy.NonStandard` | `NonStandard = JClass('zippy.NonStandard')` |
| Construct an object | `MyClass o = new MyClass(1);` | `o = MyClass(1)` |
| Get / set a static field | `MyClass.staticField = 2;` | `MyClass.staticField = 2` |
| Call a static / member method | `MyClass.callStatic(1); o.callMember(1);` | `MyClass.callStatic(1); o.callMember(1)` |
| Member with Python naming conflict | `o.pass()` | `o.pass_()` |
| Check inheritance | `o instanceof MyClass` | `isinstance(o, MyClass)` |
| Cast to a specific type | `(BaseClass) o` | `BaseClass @ o` |

</div>

---

## JPype's Bridging Model (pt. 2)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#exceptions)

- Java exceptions extend from Python exceptions

- Java exceptions can be dealt with in the same way as Python native exceptions
    * i.e. via `try`-`except` blocks

- `JException` serves as the base class for all Java exceptions

<div style="font-size: 0.65em">

| Description | Java | Python |
|---|---|---|
| Catch an exception | `try { o.throwsException(); } catch (java.lang.Exception ex) { ... }` | `try: o.throwsException()`<br>`except java.lang.Exception as ex: ...` |
| Throw an exception to Java | `throw new java.lang.Exception("Problem");` | `raise java.lang.Exception("Problem")` |
| Check if an object is a Java exception | | `isinstance(obj, JException)` |
| Closeable items | `try (InputStream is = Files.newInputStream(file)) { ... }` | `with Files.newInputStream(file) as is: ...` |

</div>

---

## JPype's Bridging Model (pt. 3)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#primitives)

<div class="multiCol"><div class="col" style="width: 38%">

- most Python primitives __directly map__ into Java primitives

- however, Python does _not_ have the _same_ primitive types...

- ... hence, __explicit casts__ may be needed in some cases

- each primitive Java type is __exposed__ in JPype (`jpype.JBoolean`, `.JByte`, `.JChar`, `.JShort`, `.JInt`, `.JLong`, `.JFloat`, `.JDouble`).

</div><div class="col">

<div style="font-size: 0.6em">

| Description | Java | Python |
|---|---|---|
| Cast to hit an overload | `o.call((int) v);` | `o.call(JInt(v))` |
| Create a primitive array | `int[] array = new int[5];` | `array = JInt[5]` |
| Create a rectangular primitive array | `int[][] array = new int[5][10];` | `array = JInt[5, 10]` |
| Create an array of arrays | `int[][] array = new int[5][];` | `array = JInt[5, :]` |
| Create an initialised primitive array | `int[] array = new int[]{1, 2, 3};` | `array = JInt[:]([1, 2, 3])` |
| Create an initialised boxed array | `Integer[] array = new Integer[]{1, 2, 3};` | `array = java.lang.Integer[:]([1, 2, 3])` |
| Put a specific primitive type on a list | `List<Integer> l = new ArrayList<>();`<br>`l.add(1);` | `l = ArrayList()`<br>`l.add(JInt(1))` |
| Box a primitive | `Integer boxed = 1;` | `boxed = JObject(JInt(1))` |

</div>

</div></div>

---

## JPype's Bridging Model (pt. 4)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#strings)

<div class="multiCol"><div class="col" style="width: 38%">

- Java strings are __similar__ to Python strings

- they are both __immutable__ and produce a new string when altered

- most operations can use Java strings __in place__ of Python strings
    + with minor exceptions, as Python strings are not completely _duck typed_

- when comparing or using strings as dictionary keys, all `JString` objects should be converted to Python

</div><div class="col">

<div style="font-size: 0.6em">

| Description | Java | Python |
|---|---|---|
| Create a Java string | `String s = new String("foo");` | `s = JString("foo")` |
| Create a Java string from bytes | `byte[] b;`<br>`String s = new String(b, "UTF-8");` | `b = b'foo'`<br>`s = JString(b, "UTF-8")` |
| Convert a Java string | | `str(javaStr)` |
| Compare Python and Java strings | | `str(javaStr) == pyString` |
| Compare Java strings | `javaStr.equals("foo")` | `javaStr == "foo"` |
| Check if an object is a Java string | | `isinstance(obj, JString)` |

</div>

</div></div>

---

## JPype's Bridging Model (pt. 5)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#arrays)

<div class="multiCol"><div class="col" style="width: 38%">

- Java arrays are __mapped__ to Python __lists__

- more precisely, they __operate like__ Python _lists_, but they are __fixed in size__

- reading a __slice__ from a Java array returns a _view_ of the array, __not a copy__

- passing a __slice__ of a Python list to Java will create a __copy__ of the sub-list

</div><div class="col">

<div style="font-size: 0.6em">

| Description | Java | Python |
|---|---|---|
| Create a single-dimension array | `MyClass[] array = new MyClass[5];` | `array = MyClass[5]` |
| Create a multi-dimension array | `MyClass[][] array2 = new MyClass[5][];` | `array2 = MyClass[5, :]` |
| Access an element | `array[0] = new MyClass();` | `array[0] = MyClass()` |
| Size of an array | `array.length` | `len(array)` |
| Get last element | `MyClass a = array[array.length - 1];` | `a = array[-1]` |
| Slice an array | | `a = array[2:5]` |
| Clone an array | `MyClass[] a = array.clone();` | `a = array.clone()` |
| Convert to Python list | | `pylist = list(array)` |
| Iterate elements | `for (MyClass element : array) { ... }` | `for element in array: ...` |
| Check if an object is a Java array | | `isinstance(obj, JArray)` |

</div>

</div></div>

---

## JPype's Bridging Model (pt. 6)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#collections)

<div class="multiCol"><div class="col" style="width: 38%">

- Java collections are __overloaded__ with Python __syntax__ where possible
    + to operate similarly to Python collections

- Java's `Iterable`s are __mapped__ to Python _iterables_ by overriding the `__iter__` method

- Java's `Collection`s are __mapped__ to Python containers by overriding `__len__`

- Java's `Map`s support Python's __dictionary__ syntax by overriding `__getitem__` and `__setitem__`

- Java's `List`s support Python's __list__ syntax by overriding `__getitem__` and `__setitem__`

</div><div class="col">

<div style="font-size: 0.6em">

| Description | Java | Python |
|---|---|---|
| Import list type | `import java.util.ArrayList;` | `from java.util import ArrayList` |
| Construct a list | `List<Integer> l = new ArrayList<>();` | `l = ArrayList()` |
| Get length of list | `int sz = l.size();` | `sz = len(l)` |
| Get list item | `Integer i = l.get(0);` | `i = l[0]` |
| Set list item | `l.set(0, 1);` | `l[0] = JInt(1)` |
| Iterate list elements | `for (Integer e : l) { ... }` | `for e in l: ...` |
| Import map type | `import java.util.HashMap;` | `from java.util import HashMap` |
| Construct a map | `Map<String, Integer> m = new HashMap<>();` | `m = HashMap()` |
| Get length of map | `int sz = m.size();` | `sz = len(m)` |
| Get map item | `Integer i = m.get("foo");` | `i = m["foo"]` |
| Set map item | `m.put("foo", 1);` | `m["foo"] = JInt(1)` |
| Iterate map entries | `for (Map.Entry<String, Integer> e : m.entrySet()) { ... }` | `for e in m.entrySet(): ...` |

</div>

</div></div>

---

## JPype's Bridging Model (pt. 7)

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/quickguide.html#implements-and-extension)

- Java _interfaces_ can be __implemented__ in Python, via JPype's `@JImplements` and `@JOverride` __decorators__

- No Java class (not even abstract ones) __can be extended__ in Python

- Python __lambda expressions__ can be cast to Java's __functional interfaces__

<div style="font-size: 0.65em">

| Description | Java | Python |
|---|---|---|
| Implement an interface | <code>public class PyImpl implements MyInterface {<br>&nbsp;&nbsp;public void call() { &#46;&#46;&#46; }<br>}</code> | <code>@JImplements(MyInterface)<br>class PyImpl:<br>&nbsp;&nbsp;@JOverride<br>&nbsp;&nbsp;def call(self): &#46;&#46;&#46;</code> |
| Extend a class | `public class PyImpl extends MyClass { ... }` | _not supported_ |
| Lambdas | `DoubleUnaryOperator u = (p -> p * 2);` | `u = DoubleUnaryOperator @ (lambda x: x * 2)` |
| Add methods to an existing Java class | | <code>@JImplementationFor(&quot;java.util.Map&quot;)<br>class _JMap:<br>&nbsp;&nbsp;def \_\_len\_\_(self): return self.size()</code> |

</div>

---

## JPype type conversion model

Overview on the [official documentation](https://jpype.readthedocs.io/en/latest/userguide.html#type-matching)

<div class="multiCol"><div class="col" style="width: 70%">

<div style="font-size: 0.5em">

| Python \ Java | `byte` | `short` | `int` | `long` | `float` | `double` | `boolean` | `char` | `String` | Array | Object | `j.l.Object` | `j.l.Class` |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| `int` | I<sup>1</sup> | I<sup>1</sup> | X | I | I<sup>3</sup> | I<sup>3</sup> | X<sup>8</sup> | | | | | I<sup>11</sup> | |
| `float` | | | | | I<sup>1</sup> | X | | | | | | I<sup>11</sup> | |
| `str` | | | | | | | | I<sup>2</sup> | X | | | I | |
| `JByte` | X | | | | | | | | | | | I<sup>9</sup> | |
| `JShort` | | X | | | | | | | | | | I<sup>9</sup> | |
| `JInt` | | | X | | | | | | | | | I<sup>9</sup> | |
| `JLong` | | | | X | | | | | | | | I<sup>9</sup> | |
| `JFloat` | | | | | X | | | | | | | I<sup>9</sup> | |
| `JDouble` | | | | | | X | | | | | | I<sup>9</sup> | |
| `JBoolean` | | | | | | | X | | | | | I<sup>9</sup> | |
| `JChar` | | | | | | | | X | | | | I<sup>9</sup> | |
| `JString` | | | | | | | | | X | | | I | |
| `JArray` | | | | | | | | | | I/X<sup>4</sup> | | I | |
| `JObject` | | | | | | | | | | I/X<sup>6</sup> | I/X<sup>7</sup> | I/X<sup>7</sup> | |
| `JClass` | | | | | | | | | | | | I | X |
| boxed<sup>10</sup> | I | I | I | I | I | I | I | | | | | I | |

</div>

</div><div class="col">

<div style="font-size: 0.6em">

__Legend__ (`j.l` = `java.lang`):

- _empty_: there is no way to convert
- __explicit__ (_E_): only via casting
- __implicit__ (_I_): JPype will convert as needed
- __exact__ (_X_): like implicit, but takes _priority_ in __overload selection__

__Notes__:
- <sup>1</sup> only if the value fits the Java type
- <sup>2</sup> only strings of length 1
- <sup>3</sup> may be lossy, as in Java
- <sup>4</sup> dimensions and types must match
- <sup>6</sup> only for compatible array classes
- <sup>7</sup> exact for the object's own class, implicit otherwise
- <sup>8</sup> only `True` and `False`
- <sup>9</sup> boxed as per Java rules
- <sup>10</sup> Java boxed types: always implicit, to disambiguate from primitives
- <sup>11</sup> boxed to `java.lang.Number`

</div>

</div></div>

---

## Overload selection

- Consider the following example of Python code with JPype:
    ```python
    import jpype.imports
    from java.lang import System

    System.out.println(1)
    System.out.println(2.0)
    System.out.println('A')
    ```

- Which overload of `System.out.println` is called among the [many admissible ones](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/PrintStream.html)?
    * Python's `2.0` is convertible to Java's `float` and `double`
        + but Java's `double` is the __exact__ match
    * Python's `'A'` is convertible to Java's `String` and `char`
        + but Java's `String` is the __exact__ match
    * Python's `1` is _implicitly_ convertible to many types (e.g. `int`, `long`, `char`, `boolean`, `Object`)
        + as of JPype 1.7, this call raises `TypeError: Ambiguous overloads found for java.io.PrintStream.println(int)`
        + an explicit cast solves the issue: `System.out.println(jpype.JInt(1))`

---

## Ambiguous overload selection

- Consider the following example of Python code with JPype:
    ```python
    import jpype

    csv = jpype.JClass("io.github.gciatto.csv.Csv")

    csv.headerOf(["field", "another field"])
    ```

- This would raise the following __error__:
    ```
    TypeError: Ambiguous overloads found for io.github.gciatto.csv.Csv.headerOf(list) between:
        public static final io.github.gciatto.csv.Header io.github.gciatto.csv.Csv.headerOf(java.lang.Iterable)
        public static final io.github.gciatto.csv.Header io.github.gciatto.csv.Csv.headerOf(java.lang.String[])
    ```

    * because Python's `list` is convertible to both Java's `Iterable` and `String[]`
        + but neither is the __exact__ match

- To solve this issue, one can __explicitly__ cast the Python `list` to the desired Java type:

    ```python
    import jpype
    import jpype.imports
    from java.lang import Iterable as JIterable

    csv = jpype.JClass("io.github.gciatto.csv.Csv")

    csv.headerOf(JIterable@["field", "another field"])
    # returns Header("field", "another field")
    ```

---

## Overload selection in a nutshell

{{< figure src="jpype-overloads.svg" width="75%" alt="Flowchart of JPype's method dispatch: collect all methods with the called name, rate each argument as none, explicit, implicit or exact, discard overloads with none or explicit matches; if none is left raise No matching overloads, if exactly one is best call it, otherwise raise Ambiguous overloads, to be fixed with a cast. Side table of System.out.println outcomes with JPype 1.7.1: 2.0 calls println(double), 'A' println(String), True println(boolean), 1 is ambiguous among boolean, char and Object, JInt(1) calls println(int)." >}}

---

{{< slide id="jpype-customisers" >}}

## Customising Java types in Python (pt. 1)

- One may customise the behaviour of Java types in Python by __providing custom implementations__ for them
    + by means of the `@JImplementationFor` decorator

- In that case the special method `__jclass_init__` is called on the custom implementation, _just once_, to configure the class

- In _type hierarchies_, implementations provided for superclasses are __inherited__ by subclasses

---

## Customising Java types in Python (pt. 2)

Consider for instance the following customisations, allowing _Java collections_ to be used with __Python syntax__

{{% multicol %}}{{% col class="col-6" %}}
```python
from collections.abc import Sequence


@jpype.JImplementationFor("java.lang.Iterable")
class _JIterable:
    def __iter__(self):
        return self.iterator()  # supports "for x in coll" syntax


@jpype.JImplementationFor("java.util.Collection")
class _JCollection:
    def __len__(self):
        return self.size()       # supports "len(coll)" syntax

    def __contains__(self, i):
        return self.contains(i)  # supports "i in coll" syntax

    # __iter__ is inherited from _JIterable
    # because in Java: Collection extends Iterable
```
{{% /col %}}{{% col class="col-6" %}}
```python
@jpype.JImplementationFor("java.util.List")
class _JList:
    def __jclass_init__(self):
        # makes isinstance(x, Sequence) true for Java lists
        Sequence.register(self)

    def __getitem__(self, ndx):
        return self.get(ndx)   # supports "list[i]" syntax

    def append(self, obj):
        return self.add(obj)   # supports "list.append(obj)" syntax

    # __len__, __contains__, __iter__ are inherited
    # from _JCollection
```
{{% /col %}}{{% /multicol %}}

simplified from [JPype's codebase](https://github.com/jpype-project/jpype/blob/master/jpype/_jcollection.py)

---

## Making wrapped code Pythonic (pt. 1)

- The code wrapped via JPype is __not__ Pythonic by default
    + it works in principle, but it is very _hard to use_ for the average Python developer
        * we cannot assume the _average_ Python developer is _familiar_ with _Java_...
        * nor with JPype
        * and the developer should know both aspects to use the wrapped code _as is_

- It is important to make the wrapped code as _Pythonic_ as possible
    + __factory methods__ for building instances of types
    + simplified __package structure__
        * e.g. `io.github.gciatto.csv.Csv` $\rightarrow$ `jcsv.Csv`
    + __properties__ instead of _getters_ and _setters_
    + `snake_case` instead of `camelCase`
    + __magic methods__ implemented whenever possible
        * e.g. `__len__` for `java.util.Collection`
        * e.g. `__getitem__` for `java.util.List`
    + __optional parameters__ in methods instead of _overloads_

- All such refinements can be done in JPype via __customisations__ of the Java types
    + _unit tests_ should be written to ensure the customisations are _not broken_ by future changes

---

## Making wrapped code Pythonic (pt. 2)

### Workflow

For all __public types__ in the wrapped _Java library_:
- decide their corresponding __Python package__
- provide Pythonic __factory methods__
- __customise__ the Python class to make it _Pythonic_ (possibly exploiting __type hierarchies__ to save time)
    + add __properties__ calling _getters/setters_
    + __override__ Java methods to make them _Pythonic_
        * e.g. use _magic methods_ where possible
        * e.g. use _optional parameters_ where possible, removing the need for _overloads_
- write __unit tests__ for _Pythonic API_

---


## Example: the `jcsv` package (pt. 1)

- The `jcsv` package ([module `csv-python`](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/)) is a Pythonic wrapper for our JVM-based `io.github.gciatto.csv` library

- Java's type definitions are brought to Python in [jcsv/__init__.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/jcsv/__init__.py):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="1" to="22" >}}

- importing `jcsv.jvm` _starts the JVM_ (see [later](#/jpype-packaging)), hence it must come before any access to Java types
- `Csv` and `CsvJvm` are the Java classes generated from `Csv.kt` and `Csv.jvm.kt`, thanks to `@file:JvmName`
- this makes it possible to write `from jcsv import Table, Record, Header` on the user side

---

## Example: the `jcsv` package (pt. 2)

__Parsing__ and **formatting** operations are mapped _straightforwardly_ to Python functions (with _optional parameters_ replacing Kotlin's default arguments):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="54" to="78" >}}

---

## Example: the `jcsv` package (pt. 3)

{{% multicol %}}{{% col class="col-6" %}}

Ad-hoc _factory method_ for building `Header` instances:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="25" to="28" >}}

making it possible to write the following code on the user side:

```python
import jcsv

header1 = jcsv.header("column1", "column2", "column3")
header2 = jcsv.header(3) # anonymous header with 3 columns
columns = (f"column{i}" for i in range(1, 4)) # generator
header3 = jcsv.header(columns) # same as header1
```

{{% /col %}}{{% col class="col-6" %}}

Function `iterable_or_varargs` (in [jcsv/python.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/jcsv/python.py)) _simulates_ **multiple overloads**:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/python.py" from="8" to="22" >}}

- strings are iterable too, but `header("col")` must not split `"col"` into characters

{{% /col %}}{{% /multicol %}}

---

## Example: the `jcsv` package (pt. 4)

- How can `jvm.Iterable @ map(str, xs)` work, if Python's `map` is not convertible to Java's `Iterable`?

- A _custom conversion_ is registered in [jcsv/jvm/__jvmutils.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/jcsv/jvm/__jvmutils.py), via the `@JConversion` decorator:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/jvm/__jvmutils.py" from="116" to="123" >}}

- where `_IterableAdapter` is a Python class _implementing_ Java's `Iterable` interface:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/jvm/__jvmutils.py" from="66" to="74" >}}

- `_IteratorAdapter` similarly implements `java.util.Iterator` on top of Python iterators

---

## Example: the `jcsv` package (pt. 5)

Ad-hoc _factory methods_ for building `Record` and `Table` instances:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="31" to="51" >}}

- `table` accepts headers and records either as Java objects or as Python iterables, e.g.:
    ```python
    jcsv.table(["a", "b"], [1, 2], [3, 4])
    ```

---

## Example: the `jcsv` package (pt. 6)

The `Row` class is customised to make it more Pythonic:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="96" to="111" >}}

- supporting the syntax `len(row)` instead of `row.getSize()`
- supporting the syntax `row[i]` instead of `row.get(i)`
- supporting the syntax `row[-i]` instead of `row.get(row.getSize() - i)`
- letting `IndexError` be raised instead of `IndexOutOfBoundsException`
- supporting the syntax `row.size` instead of `row.getSize()`

---

## Example: the `jcsv` package (pt. 7)

`Header` inherits all customisations of `Row` (since `Header extends Row`), plus the following ones:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="128" to="138" >}}

- supporting the syntax `header.columns` instead of `header.getColumns()`
- supporting the syntax `column in header` instead of `header.contains(column)`
- supporting the syntax `header.index_of(column)` instead of `header.indexOf(column)`

---

## Example: the `jcsv` package (pt. 8)

`Record` inherits all customisations of `Row` (since `Record extends Row`), plus the following ones:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="114" to="125" >}}

- supporting the syntax `record.header` instead of `record.getHeader()`
- supporting the syntax `record.values` instead of `record.getValues()`
- supporting the syntax `value in record` instead of `record.contains(value)`

---

## Example: the `jcsv` package (pt. 9)

The `Table` class is customised too, to make it more Pythonic:

{{% multicol %}}{{% col class="col-8" %}}

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/__init__.py" from="141" to="164" >}}

{{% /col %}}{{% col class="col-4" %}}

- supporting the syntax `table.header` instead of `table.getHeader()`
- supporting the syntax `len(table)` instead of `table.getSize()`
- supporting the syntax `table[i]` instead of `table.get(i)`
- supporting the syntax `table[-i]` instead of `table.get(table.getSize() - i)`
- supporting the syntax `table.records` instead of `table.getRecords()`

{{% /col %}}{{% /multicol %}}

---

{{< slide id="jpype-packaging" >}}

## Including `.jar`s in JPype projects (pt. 1)

```bash
csv-python/
├── build.gradle.kts            # Gradle build: generation of csv.jar + Python tasks
├── pyproject.toml              # Python project description (generated from Gradle, tracked)
├── uv.lock                     # pinned versions of Python dependencies
├── .python-version             # Python version used for development
├── jcsv/
│   ├── __init__.py
│   ├── __main__.py             # CLI, installed as the `jcsv` command
│   ├── jvm/
│   │   ├── __init__.py         # this is where the JVM is started via JPype
│   │   ├── __jvmutils.py       # Java <-> Python conversions
│   │   └── csv.jar             # the fat JAR of the JVM-based library (generated, not tracked)
│   └── python.py
└── tests/
    ├── __init__.py
    ├── test_parsing.py
    └── test_python_api.py
```

1. We need to __ensure__ that the _JVM-based library_ is available on the system where `jcsv` is installed
    + why not include it in the Python package?

2. The `build.gradle.kts` file automates the generation of the `csv.jar` file
    + it is a _fat JAR_ containing all the dependencies of the JVM-based library
    + such JAR is placed in the `jcsv/jvm` directory, i.e. _inside_ the Python package
    + so it is included in the wheel built by `uv build`, and distributed with the Python library

3. The `jcsv/jvm/__init__.py` file starts the JVM, with `csv.jar` on the class path

---

## Including `.jar`s in JPype projects (pt. 2)

The [build.gradle.kts](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/build.gradle.kts) of `csv-python` relies on the [`gradle-python-uv`](https://github.com/gciatto/gradle-python-uv) plugin, mapping Gradle's lifecycle tasks (`test`, `check`, `assemble`, ...) onto [uv](https://docs.astral.sh/uv/) commands:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/build.gradle.kts" from="3" to="31" >}}

- `pyproject.toml` is generated from the `python { pyproject { ... } }` block (`./gradlew generatePyproject`)
- `createCoreJar` copies the fat JAR of `csv-core` (created by the [Shadow](https://gradleup.com/shadow/) plugin) into the Python package

---

## Including the JVM in JPype projects

- We need to __ensure__ that some __JVM is available__ on the system where `jcsv` is installed

- Notice that the JVM is available as a __Python dependency__ too: [jdk4py](https://pypi.org/project/jdk4py/)
    + a minimal JDK runtime, shipped as platform-specific wheels (Linux, macOS, Windows; x86_64 and arm64)

- So, the JVM can be __automatically downloaded__ and __installed__ as a dependency of `jcsv` (see `dependency("jdk4py>=25.0.2.1")` in the build file)

- Then, one may simply configure JPype to use _that_ JVM, in [jcsv/jvm/__init__.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/jcsv/jvm/__init__.py):

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/jcsv/jvm/__init__.py" from="1" to="17" >}}

- Try it: `./gradlew :csv-python:assemble`, then `pip install csv-python/build/python/dist/*.whl` in a clean virtual environment

---

## About unit testing

- Unit tests are __essential__ to ensure the _correctness_ of the Pythonic API
    + they prevent _corruption_ of the Pythonic API when the JAR is __updated__

- It is important to __test__ all the _customisations_ and _factory methods_
    + because these are __not covered__ by the unit tests of the _JVM-based library_

- Consider for instance tests in [tests/test_parsing.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/tests/test_parsing.py) and [tests/test_python_api.py](https://github.com/unibo-spe/multiplatform-example/blob/dfd35b4ad7b929239309bfe39f55b5149aca22bb/csv-python/tests/test_python_api.py), e.g.:

{{< github repo="multiplatform-example" branch="dfd35b4ad7b929239309bfe39f55b5149aca22bb" path="csv-python/tests/test_python_api.py" from="99" to="110" >}}

- Run them via `./gradlew :csv-python:test` (i.e. `uv run poe test`, i.e. `pytest`)
    + they depend on `createCoreJar`, so the JAR is always up to date

---

## Example: the `jcsv` package (recap)

{{< figure src="jcsv-layers.svg" width="75%" alt="Layers of the jcsv package, from top to bottom: user code; the Pythonic API in jcsv/__init__.py and python.py; Java utilities in jcsv/jvm/__jvmutils.py; the JVM bootstrap in jcsv/jvm/__init__.py; csv.jar; and the JPype1 and jdk4py dependencies from PyPI. The four middle layers form the wheel. On the right, Gradle tasks: csv-core:shadowJar feeds createCoreJar, which copies csv.jar; uvSync installs the dependencies; uvBuild packages the wheel; Poe tasks run ruff and pytest." >}}

---

{{< slide id="wrap-up" >}}

## Wrap-up

<div style="font-size: 0.80em">

| | Write once, build anywhere (e.g. Kotlin MP) | Write first, wrap elsewhere (e.g. JPype) |
|---|---|---|
| Code base | one, in a "super-language", plus platform-specific parts | one main implementation, plus one wrapper per platform |
| Effort vs. `N` platforms | common code written once, platform-specific code `N` times | API / wrapper code (and its tests) written `N` times |
| Runtime | native runtime of each platform | the main platform's runtime is shipped along (e.g. JVM inside a Python wheel) |
| Idiomatic API | partially (e.g. `@JvmName`, `@JsExport`, `@JsName`) | fully customisable in each wrapper |
| Limitations | small common std-lib, platform-specific build/release tooling | bridging overhead, type-conversion ambiguities, larger packages |

</div>

> Both approaches require __clear separation__ between API and implementation, and __tests on every platform__

---

{{% import path="reusable/back.md" %}}
