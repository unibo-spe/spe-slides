+++

title = "Containerisation and Orchestration"
description = "Practical introduction to Containerisation and Orchestration with Docker"
outputs = ["Reveal"]

[reveal_hugo.custom_theme_options]
targetPath = "css/custom-theme.css"
enableSourceMap = true

+++

<style>
.reveal blockquote {
    font-family: 'Georgia';
}
</style>

# Containerisation and Orchestration

{{% import path="reusable/header-gc.md" %}}

---

## Preliminaries

> \[Software\] __Deployment__ $:=$ all of the activities that make a software system available for use
- [*Software Engineering: A Practitioner's Approach*, Roger S. Pressman](https://www.mlsu.ac.in/econtents/16_EBOOK-7th_ed_software_engineering_a_practitioners_approach_by_roger_s._pressman_.pdf)

<br/>

### General need in Software Engineering

To govern the _deployment_ of __software systems__ onto some __infrastructure__
- __software system__: multiple interacting software _components_, each one having its _computational requirements_
    * component $\approx$ process

- __infrastructure__: the available _hardware_ and _software_ __substratum__ supporting the execution of software components
    * hardware $\approx$ CPU, memory, storage, network
    * software $\approx$ operating system, drivers, runtimes, libraries

- __deployment__: orderly _instantiating_ software components onto the infrastructure, while 
    * _satisfying_ the computational _requirements_ of each component
    * _optimising_ the _exploitation_ of the infrastructural facilities

---

## What to deploy? (pt. 1)

- __Short-lived tasks__ (a.k.a. __jobs__): tasks which eventually terminate
    * e.g. data processing or generation tasks
    * e.g. simulations

- __Long-lived tasks__ (a.k.a. __services__) tasks which run indefinitely
    * e.g. web servers
    * e.g. databases

---

## What to deploy? (pt. 2)

### Short-lived tasks

> __Short-lived__ tasks are bare computational activities aimed at __processing/generating__ some data

- Pure __algorithms__ _accepting data_ as input and/or _producing data_ as output. 

- They will eventually reach their natural __termination__ 
    + _without_ requiring external _intervention_
    
- Common workflow:
    1. users _provide_ the _code_ and the _input_ data, 
    2. they _start_ the task, 
    3. they are interested in _getting the output_ data

- __Parallelism__ may be exploited to _speed up_ the computations

---

## What to deploy? (pt. 3)

### Long-lived tasks

> __Long-lived__ are computational activities which are __not meant to terminate__ automatically (unless _explicitly stopped_)

- Infinite _loops_, just _waiting_ for _requests_, and _serving_ them as soon as possible

- __Interactivity__ is a key aspect: 
    + __clients__ _interact_ with the service, 
    + the service __reacts__ to the clients' requests
    + clients may be _humans_ or _other software_ components

- Common workflow:
    + users _provide_ the _code_ and some _deployment-automation_ script
    + they _start_ the task,
    + they are interested in _interacting_ with the service
        * e.g. via the shell
        * e.g. via some graphical user interface
        * e.g. via some Web client

- __Parallelism__ may be exploited to support __replication__
    + e.g. for __fault tolerance__
    + e.g. for __load balancing__
    + e.g. for __scaling__

---

## Execution context of a computational task

Regardless of their life-span, computational tasks may require __computational resources__ of various sorts:

- specific __hardware capabilities__
    + e.g. quick / multi-core CPUs for computation intensive tasks
    + e.g. GPUs for tasks requiring parallelism
    + e.g. primary memory (RAM)
    + e.g. large / quick storage (SSD, HDD, etc.)
    + e.g. quick networks connection (e.g. optical fiber)

- specific __operative systems__ (Linux, MacOS, Windows, etc.) or specific __architectures__ (x86, ARM, etc.)

- specific __runtime platforms__ (e.g. JVM, Python, .NET etc.)

- specific __infrastructural components__ (e.g. database, reverse proxy, load-balancer, broker, etc.) 

- specific __libraries__ (e.g. CUDA, `numpy`, etc.)

---

## The need for encapsulation (pt. 1)

<!-- > __Encapsulation__ is the __hiding__ of the __implementation details__ of a __component__ from its __clients__.
> Encapsulated components have a clear __interface__ which is the only way to interact with them. -->

> Why can't we simply configure bare-metal machines to host our tasks?

- Need for __multi-tenancy__ (i.e. sharing) on computational resources
    1. one may have _$N$ machines_ and _$M$ applications_...
        + ... and be willing to __decouple__ applications from machines
    2. different software components may have __incompatible__ HW / SW requirements

- Need for __isolation__
    + preventing _spurious interactions_ among different tenants
    + a common requirement in multi-tenant scenarios

- Need to __formalize / control / reproduce__ the _computational environment_ of applications
    + formalisation is a _pre-requisite_ for __automation__
    + automation _enables_ __control__ and __scale__

- Need to __automate deployment__ of applications into __production__ / __testing__ environments
    + this is the immediate _benefit for engineers_

- Need for __flexibility__ (in _re-deployment_), and __scalability__
    + _effort minimisation_ for application __startup__ or __transfer__

---

## The need for encapsulation (pt. 2)

Some abstraction is needed to encapsulate applications, and their computational environment, into a single unit 

> __Encapsulation__ is the __hiding__ of the __implementation details__ of a __component__ from its __clients__.
> Encapsulated components have a clear __interface__ which is the only way to interact with them. 

---

## How to achieve encapsulation? (pt. 1)

* __Virtual machines__ (VM) on top of VM _hypervisors_ (VMH)

* __Containers__ on top of container _engines_ (CE)

---

## How to achieve encapsulation? (pt. 2)

### Virtual machines and hypervisors

- VMH run on/as the OS of a bare-metal machine, abstracting HW and SW peculiarities away
    * VM have virtualised HW and SW resources, different from the host's ones

- VMH may instantiate multiple VM on the same machine
    * partitioning actual resources ahead of time

- each VM runs its own OS, and may host multiple applications
    * in the eyes of the user, the VM is undistinguishable from a bare-metal machine

- VM may be paused, snapshot (into file), resumed, and possibly migrated
    * snapshots are files containing the whole file-system of the VM

- VM are coarse grained encapsulation units
    * they are heavy-weight (GBs)
    * they are slow to start, run, migrate, snapshot
    * VM commonly encapsulate multiple application-level components
        + database, server, plus all their runtimes and libraries, etc.

- Many industry-ready technologies:
    * VMWare, VirtualBox, KVM, Xen, Hyper-V, QEMU, Proxmox, etc.

---

## How to achieve encapsulation? (pt. 3)

### Containers and container engines (CE)

- CE run on/as the OS of a bare-metal/virtual machine, abstracting the runtime, storage, and network environment away
    + the CPU, memory, OS kernel, drivers, and hardware are __not virtualised__

- one may instantiate multiple containers on the same machine
    + sharing the actual resources dynamically (overbooking support)

- each container shares the OS kernel of the host, yet having its own runtime(s), storage, and network facilities
    + in the eyes of the user, the container is a process running on top of a minimal OS

- each container is instance of an image, i.e. a read-only template containing deployment instructions 
    + differences w.r.t. that image constitute the state of the container
        * these can be snapshot into file of minimal size

- containers are fine-grained encapsulation units
    + they are light-weight (MBs)
    + they are fast to start, run, snapshot
    + they are commonly used to encapsulate single application-level components
        * e.g. a database instance, a web-server instance, etc.
        * the final application should consist of several, inter-communicating containers

- Many industry-ready technologies:
    + Docker, LXC, LXD, Podman, etc.

---

<!-- this file includes generated content. Do not edit. Edit content/09-containerization/_generator.md, instead. -->

## Bare-metal vs. VMs vs. Containers

{{< gravizo >}}
digraph structs {
rankdir=LR
graph [fontname="helvetica", layout=neato]
edge [fontname="helvetica"]
node [fontname="helvetica", shape=record, style="rounded, filled", fontcolor=white, fixedsize=true, width=3];
"Bare metal" [shape=plaintext, style="", fontcolor=black fontsize=20, pos="0,0!", layout=neato]
Physical [fillcolor=black, pos="0,0.5!"]
"Operating System" [fillcolor=gray, pos="0,1.1!"]
Runtime [fillcolor=green, pos="0,1.7!"]
App1 [fillcolor=red, width=0.95, pos="-1.025,2.3!"]
App2 [fillcolor=red, width=0.95, pos="0,2.3!"]
App3 [fillcolor=red, width=0.95, pos="1.025,2.3!"]

    "Virtual Machine" [shape=plaintext, style="", fontcolor=black fontsize=20, pos="3.5,0!"]
    VM_Phy [label=Physical, fillcolor=black, pos="3.5,0.5!"]
    "Host Operating System" [fillcolor=gray, pos="3.5,1.1!"]
    Hypervisor [fillcolor=orange, pos="3.5,1.7!", fontcolor=black]
    GuestOS1 [fillcolor=gray, width=0.95, pos="2.475,2.3!"]
    GuestOS2 [fillcolor=gray, width=0.95, pos="3.5,2.3!"]
    GuestOS3 [fillcolor=gray, width=0.95, pos="4.525,2.3!"]
    Runtime1 [fillcolor=green, width=0.95, pos="2.475,2.9!"]
    Runtime2 [fillcolor=green, width=0.95, pos="3.5,2.9!"]
    Runtime3 [fillcolor=green, width=0.95, pos="4.525,2.9!"]
    VM_App1 [label=App1, fillcolor=red, width=0.95, pos="2.475,3.5!"]
    VM_App2 [label=App2, fillcolor=red, width=0.95, pos="3.5,3.5!"]
    VM_App3 [label=App3, fillcolor=red, width=0.95, pos="4.525,3.5!"]

    "Container" [shape=plaintext, style="", fontcolor=black fontsize=20, pos="7,0!"]
    C_Phy [label=Physical, fillcolor=black, pos="7,0.5!"]
    C_K [label="Operating System", fillcolor=gray, pos="7,1.1!"]
    "Container Service" [fillcolor=blue, pos="7,1.7!"]
    C_Rt1 [label=Runtime1, fillcolor=green, width=0.95, pos="5.975,2.3!"]
    C_Rt2 [label=Runtime2, fillcolor=green, width=0.95, pos="7,2.3!"]
    C_Rt3 [label=Runtime3, fillcolor=green, width=0.95, pos="8.025,2.3!"]
    C_App1 [label=App1, fillcolor=red, width=0.95, pos="5.975,2.9!"]
    C_App2 [label=App2, fillcolor=red, width=0.95, pos="7,2.9!"]
    C_App3 [label=App3, fillcolor=red, width=0.95, pos="8.025,2.9!"]
}
{{< /gravizo >}}

Containers provide **runtime isolation** _without_ operating **system replication**

---

## Lightweight virtual machines?

![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/lightweight-container.jpg)

---

## Closer to confined processes

![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/chroot.jpeg)

---

## Scaling up to the cluster level

> __Cluster__ $:=$ a set of _similarly configured_ __machines__ (a.k.a. __nodes__) __interconnected__ via a __network__ in order to let users exploit their __joint__ computational power

- Users may want to deploy their applications on the cluster
    + by means of a single access point
        + e.g. a Web dashboard, or a CLI

- Deployment shall allocate tasks on the cluster in an efficient way
    + meeting the tasks' computational requirements
    + balancing the load among the nodes
    + matching requirements on actual capabilities of nodes

- Infrastructure-as-a-service (IaaS) cloud technologies support deploying tasks on clusters, as VM
    + e.g. OpenStack, VSphere, etc.

- Container orchestrator support deploying tasks on clusters, as containers
    + e.g. Kubernetes, Docker Swarm, etc.

---

## Why containers?

{{< multicol >}}
{{% col %}}
![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/works-on-my-machine.jpeg)
{{% /col %}}
{{% col %}}
![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/docker-born.jpeg)
{{% /col %}}
{{< /multicol >}}

- more fine-grained encapsulation (than VM)
- faster to start, stop, snapshot, migrate (w.r.t. VM)
- more modular, more scalable (than VM)
- useful for dev-ops, and CI/CD
- most-likely, it's the future of cloud computing

---

## Containerisation vs. Orchestration

- __Containerisation__ $:=$ the process of _encapsulating_ an application into a _container_

- __Orchestration__ $:=$ the process of _deploying_ one or more container onto _one or more_ machines

- __Containerisation__ is a _pre-requisite_ for __orchestration__

- Two syntaxes involved:
    + one to _containerise_ (i.e. create container images)
    + one to _orchestrate_ (i.e. deploy containers)

---

## Main abstractions

TBD

---

## Docker

Docker is a containerization platform

*Standard de-facto* in industry

**Base concepts**
- *Image*
    * a *read-only template* with instructions for creating a Docker container
    * images can get built upon other images
    * images are made of a stack of *layers*
- *Container*
    * a *runnable instance* of an image
    * namely, a "writable layer" atop an image
- *Service*
    * A software component in charge of running one or multiple containers

---

## Docker architecture

- *Registry*: repository of images
- *Daemon*: service pulling images from registries and instancing containers
- *Client*: interface towards the daemon

![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/architecture.svg)

---

## Running docker containers

1. Install docker
2. Add your user to the `docker` group
3. Enable the docker service (on most Linux distributions `systemctl start docker`)
4. Pull an image: `docker pull adoptopenjdk`
5. Run a container! `docker run adoptopenjdk`

Every container provides a *default command*, running without options runs such default in a *non-interactive* terminal.

Running in interactive mode can be achieved with the `-i` option

Running a custom command can be achieved with writing the command after the image name
* e.g., `docker run -i adoptopenjdk bash`
* parameters for the custom command can follow
* use the `t` option to run in a *pseudo-tty*
* use the `--rm` to remove the container after use

---

## Interaction with the outside world

A docker container runs *in isolation*.

Environment variables, network ports, and file system folders are **not** shared.

Sharing must be explicit and requires options to be specified

* Passing environment variables: `-e <name>=<value>`
* Mounting volumes: `-v <host>:<guest>:<options>`
    * `<host>` is the path on the host system
    * `<guest>` is the location where it will be mounted on the guest
    * `<options>` can be optionally specified as mount options (e.g., `rw`, `ro`)
* Publishing ports: `-p <host>:<guest>`
    * `<host>` is the port on the host system
    * `<guest>` is the corresponding port on the container

---

## Managing images

Every image has a unique **ID**, and may have an associated **tag**

The subcommand `images` lists the pulled images and their associated information

The subcommand `image` allows for running maintenance tasks, e.g.
* `docker image ls` -- same as `docker images`
* `docker image prune` -- removes unused images
* `docker image rm` -- removes images by name
* `docker image tag` -- associates a tag to an image

---

## Creating docker images

Docker images are written in a *Dockerfile*

Every command inside a Dockerfile generates a new *layer*

The final stack of layers creates the final *image*

The `docker build` command interprets the Dockerfile commands to produce a sequence of layers

Changes to a layer do not invalidate previous layers

---

## Dockerfile syntax

```dockerfile
# Pulls an image from docker hub with this name. Alternatively, "scratch" can be used for an empty container
FROM manjarolinux/base 
# Runs a command
RUN pacman -Sy --noconfirm gnupg archlinux-keyring manjaro-keyring
# Copies a file from the local folder into the image
COPY makepkg.conf /etc/makepkg.conf
# Adds a new environment variable
ENV GEM_HOME=/rubygems/bin
# Configures the default command to execute
CMD bash
```

---

## Naming images

Image naming is done via *tags*

The easiest way to do so is assigning tags at *build time* with the `-t` options of `docker build`

The option can be repeated multiple times to make multiple tags

```bash
docker build -t "myImage:latest" -t "myImage:0.1.0"
```

`latest` is usually used to identify the most recent version of some image

---

## Publishing docker images

Images get published in *registries*

The most famous, free for publicly available images, is *Docker Hub*

By default, Docker uses Docker Hub as registry (both for `pull` and `push` operations)

Docker Hub requires registration and CLI login:
* `docker login docker.io`

Once done, publication is performed via `push`:
* `docker push <image name>`

---

## Building docker images in CI

Of course, as any other software, *custom docker images should get built in CI*

Several integrators use containers as build environments: it is possible to *build a container using a container*

More in general, there is *no inherent limit to nesting containers*


