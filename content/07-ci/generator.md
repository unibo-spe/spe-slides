
+++

title = "Continuous Integration"
description = "Make things work, keep them working, move fast"
outputs = ["Reveal"]

[reveal_hugo]
transition = "slide"
transition_speed = "fast"
custom_theme = "custom-theme.scss"
custom_theme_compile = true

[reveal_hugo.custom_theme_options]
targetPath = "css/custom-theme.css"
enableSourceMap = true

+++

# Continuous Integration

<!-- write-here "reusable/header.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/ci/intro.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/ci/core-concepts.md" -->

<!-- end-write -->

---


<!-- write-here "shared-slides/ci/gha.md" -->

<!-- end-write -->

---

## In-memory signatures

Signing in CI is easier if the key can be *stored in memory*
<br>
the alternative is to *install a private key in the runner*

To do so we need to:
1. Find a way to write our key in memory
2. Export it safely into the CI environment (via a secret)
3. Configure the build system to use the key in-memory when a CI environment is detected

---

### Step 1: exporting GPG *private* keys

`gpg --armor --export-secret-key <key id>`

Exports a [base64](https://en.wikipedia.org/wiki/Base64)-encoded version of your *binary* key, with a header and a footer.

```sh
-----BEGIN PGP PRIVATE KEY BLOCK-----

M4ATvaZBpT5QjAvOUm09rKsvouXYQE1AFlmMfJQTUlmOA_R6b-SolgYFOx_cKAAL
Vz1BIv8nvzg9vFkAFhB7N7QGwYfzbKVAKhS0IDQutDISutMTS3ujJlvKuQRdoE2z
...
WjEW1UmgYOXLawcaXE2xaDxoXz1FLVxxqZx-LZg_Y/0tsB==
=IN7o
-----END PGP PRIVATE KEY BLOCK-----

```

Note: *armoring* is **not** encryption
* it is meant for readability and processing with text-based tools

---

### Step 2: export the key as a CI secret

* In most CI systems, secrets allow **enough space** for an armored GPG private keys to fit in
    * It is the case for GHA
* In this case, *just export the armored version as a secret*

* Otherwise:
    * Encrypt your secret with a (much shorter) *symmetric* key into a file
    * Store the *key as a secret* (it will fit as it is much smaller than an asymmetric key)
    * *Track the encrypted file* in your repo
    * Before signing (and *only if needed*), unencrypt the file and load it in-memory
        * then delete the unencrypted version
        * you want to reduce the probability that the file gets delivered...

---

### Step 3: tell the build system to use in-memory keys when in CI

How to tell if you are in CI or not?
* The `CI` environment variable is automatically set to `"true"` on most CI environments
    * Including GitHub Actions

#### In Gradle

```kotlin
if (System.getenv("CI") == true.toString()) {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}
```
* The `signingKey` and `signingPassword` properties must get *passed* to Gradle
    * One way is to pass them on the command line:
        * `./gradlew -PsignigngKey=... -PsigningPassword=... <tasks>`
    * Alternatively, they can be stored into environment variables
        * Gradle auto-imports properties named `ORG_GRADLE_PROJECT_<variableName>`
        * So, in GitHub actions:

```yaml
env:
    ORG_GRADLE_PROJECT_signingKey: ${{ secrets.SIGNING_KEY }}
    ORG_GRADLE_PROJECT_signingPassword: ${{ secrets.SIGNING_PASSWORD }}
```

---

## DRY with GitHub Actions

Imperative behaviour in GitHub Actions is encapsulated into *actions*

**Actions** are executed as a single logical step, with **inputs** and **outputs**

Their metadata is written in a `actions.yml` file on the repository root

GitHub actions stored on GitHub are usable without further deployment steps
* By using `owner/repo@<tree-ish>` as reference

---

## GitHub Actions' metadata

```yaml
name: 'A string with the action name'
description: 'A long description explaining what the action does'
inputs:
  input-name:  # id of input
    description: 'Input description'
    required: true # whether it should be mandatorily specified
    default: 'default value' # Default value, if not specified by the caller
outputs:
  # Outputs will be set by the action when running
  output-name: # id of output
    description: 'Description of the output'
runs: # Content depends on the action type
```

---

## Composite actions: structure

Composite actions allow the *execution of multiple steps* that can be *scripts* or *other actions*.

```yaml
runs:
    using: composite
    steps: [ <list of steps> ]
```

---

## Composite actions: example

The action is contained in its metadata descriptor `action.yml`root, e.g.:

{{< github owner="DanySK" repo="action-checkout" path="action.yml" >}}

From: [https://github.com/DanySK/action-checkout](https://github.com/DanySK/action-checkout)

---

## Composite actions: usage

It can be used with:

{{< github path=".github/workflows/build-and-deploy.yml" from=21 to=24 >}}

---

## Composite actions: limitations

* No support for secrets, they must be passed *as inputs*

For instance this way:

```yaml
name: 'Composite action with a secret'
description: 'For teaching purposes'
inputs:
  token:  # github token
    description: 'Github token for deployment. Skips deployment otherwise.'
    required: true
runs:
  using: "composite"
  steps:
    - run: '[[ -n "${{ inputs.token }}" ]] || false'
      name: Fail if the toke is unset
```

* ~~No conditional steps (ouch...)~~
    * ~~They can be somewhat emulated inside the script~~
* [Conditional steps introduced November 9th 2021](https://github.blog/changelog/2021-11-09-github-actions-conditional-execution-of-steps-in-actions/)

---

## Docker container actions

#### Wait, what is a container?

* We might need to deviate for a moment: >> [**click here!**](../09-containerization) <<

#### How to

* Configure the `Dockerfile` of the container you want to use
* Prepare the main script and declare it as `ENTRYPOINT`
* Define inputs and outputs in `action.yml`
* In the `runs` section set `using: docker` and the arguments order
    * The order is relevant, they will be passed to the entrypoint script in order

```yaml
runs:
  using: 'docker'
  image: 'Dockerfile' # Alternatively, the name of an existing image
  args:
    - ${{ inputs.some-input-name }}
```

* Docker container actions *work only on Linux*

---

## JavaScript actions

The most flexible way of writing actions
* Portable across OSs

```yaml
runs:
  using: 'node12'
  main: 'index.js'
```

* Initialize a new NPM project: `npm init -y`
* Install the toolkits you will use: `npm install @actions/<toolkitname>`
    * See: [https://github.com/actions/toolkit](https://github.com/actions/toolkit)
* write your code

```javascript
const core = require('@actions/core');
try {
  const foo = core.getInput('some-input');
  console.log(`Hello ${foo}!`);
} catch (error) {
  core.setFailed(error.message);
}
```

---

## Reusable workflows

GitHub actions also allows to configure *reusable workflows*
* Similar in concept to *composite actions*, but capture larger operations
    * They can preconfigure *matrices*
    * *Conditional steps* are supported
* Limitation: can't be used in `workflow_dispatch` if they have more than 10 parameters
* Limitation: can't be used *recursively*
* Limitation: *changes to the environment* variables (`env` context object) of the caller are *not propagated* to the callee
* Limitation: the callee has *no implicit access* to the caller's *secrets*
    * But they can be passed down

Still, the mechanism enables to some extent the creation of [libraries of reusable workflows](https://github.com/DanySK/workflows)

---

## Defining Reusable workflows

```yaml
name: ...
on:
  workflow_call: # Trigger when someone calls the workflow
    inputs:
      input-name:
        description: optional description
        default: optional default (otherwise, it is assigned to a previous)
        required: true # Or false, mandatory
        type: string # Mandatory: string, boolean, or number
    secrets: # Secrets are listed separately
      token:
        required: true
jobs:
  Job-1:
    ... # It can use a matrix, different OSs, and
  Job-2:
    ... # Multiple jobs are okay!
```

---

## Reusing a workflow

Similar to using an action!
* `uses` is applied to the entire *job*
* further `steps` cannot be defined

```yaml
name: ...
on:
  push:
  pull_request:
  #any other event
jobs:
  Build:
    uses: owner/repository/.github/workflows/build-and-deploy-gradle-project.yml@<tree-ish>
    with: # Pass values for the expected inputs
      deploy-command: ./deploy.sh
    secrets: # Pass down the secrets if needed
      github-token: ${{ secrets.GITHUB_TOKEN }}
```

$\Rightarrow$ *write __workflows__*, *use __jobs__*

---

# Stale builds

1. Stuff *works*
2. *Nobody touches it* for months
3. Untouched stuff is now *borked*!

Ever happenend?

* Connected to the issue of **build reproducibility**
    * The higher the build *reproducibility*, the higher its *robustness*
* The default runner configuration may change
* Some tools may become unavailable
* Some dependencies may get unavailable

**The sooner the issue is known, the better**

$\Rightarrow$ *Automatically run the build every some time* even if nobody touches the project
* How often? Depends on the project...
* **Warning**: GitHub Actions disables `cron` CI jobs if there is no action on the repository, which makes the mechanism less useful

---

## Additional checks and reportings

There exist a number of recommended services that provide additional QA and reports.

Non exhaustive list:
* [Codecov.io](https://codecov.io/)
    * Code coverage
    * Supports Jacoco XML reports
    * Nice data reporting system
* [Sonarcloud](https://sonarcloud.io/)
    * Multiple measures, covering reliability, security, maintainability, duplication, complexity...
* [Codacy](https://www.codacy.com/)
    * Automated software QA for several languages
* [Code Factor](https://www.codefactor.io/)
    * Automated software QA

---

## High quality FLOSS checklist

The [Linux Foundation](https://www.linuxfoundation.org/) [Core Infrastructure Initiative](https://www.coreinfrastructure.org/) created a checklist for high quality FLOSS.

**[CII Best Practices Badge Program https://bestpractices.coreinfrastructure.org/en](https://bestpractices.coreinfrastructure.org/en)**


* *Self-certification*: no need for bureaucracy
* Provides a nice *TODO list* for a high quality product
* Releases a *badge* that can be added e.g. to the project homepage

---

## Automated evolution

A full-fledged CI system allows reasonably safe *automated evolution of software*
<br>
At least, in terms of *dependency updates*

Assuming that you can *effectively intercept issues*,
here is a possible workflow for **automatic** dependency updates:

1. *Check* if there are new updates
2. *Apply* the update in a new branch
3. *Open* a pull request
4. *Verify* if changes break anything
    * If they do, manual intervention is required
5. *Merge* (or rebase, or squash)

---

## Automated evolution

**Bots** performing the aforementioned process for a variety of build systems exist.

They are usually integrated with the repository hosting provider

* Whitesource Renovate (Multiple)
    * Also updates github actions and Gradle Catalogs
* Dependabot (Multiple)
* Gemnasium (Ruby)
* Greenkeeper (NPM)

---

# Issue and PR templating

---

## Helping humans helping you helping them

Some tasks *do* require humans:
* Reporting bugs
* Explaining the contents of a human-made pull request

However, we may still want these documents to follow a **template**

* Remind contributors to enter all the information needed to tackle the issue correctly
    * for instance, instructions on how to reproduce a bug
* Pre-fill common information
* Enforce or propose a structure
    * For instance, semantic PR titles

Most Git hosting services allow to specify a **template**.

---

## Repo templates in GitHub

Templates in GitHub are special files found in the `.github` folder,
written in *YAML* or *Markdown*, and *stored on the default branch*.

The descriptor generates a form that users must fill.

They are available for both **issues** and **pull requests**,
and share most of the syntax.

---

## Location of templates in GitHub

```text
.github
├── PULL_REQUEST_TEMPLATE
│    ├── config.yml
│    ├── example.yml
│    └── another-example.md
└── ISSUE_TEMPLATE
     ├── config.yml
     ├── example.yml
     └── another-example.md
```

Any `md` or `yml` file located in `.github/ISSUE_TEMPLATE`
is considered as a template for *issues*

Any `md` or `yml` file located in `.github/PULL_REQUEST_TEMPLATE`
is considered as a template for *pull requests*

If a single template is necessary,
a single `.github/ISSUE_TEMPLATE.md` or `.github/PULL_REQUEST_TEMPLATE.md` file
replaces the content of the whole directory

---

## Forms and templates in GitHub

* Plain markdown documents are used to pre-populate the content of the PR message (*templates*)
    * A YAML *front-matter* can be used to specify options

```markdown
---
name: 🐞 Bug
about: File a bug/issue
title: '[BUG] <title>'
labels: bug, to-triage
assignees: someone, someoneelse

---

### Current Behavior:
<!-- A concise description of what you're experiencing. -->

### Expected Behavior:
<!-- A concise description of what you expected to happen. -->

### Steps to reproduce:
1. first do...
2. and then...
```

* YAML documents are used to build richer documents as *forms*
    * YAML documents can be used to build the same forms as markdown ones,
    with a pre-populated text area
    * They can also be used to build *forms* with checkboxes and some forms of validation
    * Reference syntax: https://bit.ly/3yvrXqE
