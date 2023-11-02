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

## Why containers? (pt. 1)

{{< multicol >}}
{{% col %}}
![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/works-on-my-machine.jpeg)
{{% /col %}}
{{% col %}}
![](https://raw.githubusercontent.com/DanySK/shared-slides/6824b93d3d52b841386a744f57953a73ccb67378/containerization/docker-born.jpeg)
{{% /col %}}
{{< /multicol >}}

---

## Why containers? (pt. 2)

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

### Encapsulation level

- __Container__: a sandbox for running a process and its computational environment
- __Image__: a template for creating containers
- __Layer__: a single, cacheable, step in the creation of an image
- __Host__: the machine hosting the containers 
- __Registry__: a repository of images (possibly external w.r.t. the host)
- __Network__: a virtual network for connecting containers (among each others and with the host)
- __Volume__: a bridge for letting containers share data with the host
- __Engine__ (a.k.a. __daemon__): the software running on the host, managing containers, images, volumes, layers, and networks

### Orchestration level

- __Cluster__: a set of machines, joint together by the same container orchestrator 
- __Node__: a machine in the cluster (each one acting as a host, and running the container engine)
- __Service__: a set of replicas of the same container
- __Stack__: a set of inter-related services
- __Secret__: encrypted information to be made available on containers

---

## About technologies

__Docker__: the most famous container technology, actually consisting of several components
- __Docker Engine__: the container engine managing containers and images, locally
- __Docker CLI__: the command line interface for Docker Engine (this is what you use)
- __Docker Desktop__: a GUI for Docker Engine, mostly for inspection purposes
- __Docker Hub__: the default registry for Docker images, available online
- __Docker Compose__: a tool for orchestrating containers on a single machine
- __Docker Swarm__: a tool for orchestrating containers on a cluster of machines

![](./architecture.svg)

---

## Configure Docker locally

1. Install Docker
    + https://docs.docker.com/engine/install/
    + most commonly available on package managers
2. \[Linux only\] Add your user to the `docker` group
    + `sudo usermod -aG docker $USER`
    + log out and log in again
3. Enable and start the Docker service 
    + on most Linux distributions `sudo systemctl enable docker; sudo systemctl start docker`
    + on MacOS and Windows, start the Docker Desktop application
4. Test your installation
    + `docker run hello-world`
5. Explore admissible sub-commands with `docker --help`
    + general command structure `docker <resource> <command> <options> <args>`
        * sometimes, when it's obvious, `<resource>` can be omitted

---

## Running containers

1. Pull an image: `docker pull adoptopenjdk`
2. Run a container! `docker run adoptopenjdk`

Every image provides a *default command*, running without options runs such default in a *non-interactive* terminal.

Running in interactive mode can be achieved with the `-i` option

Running a custom command *inside the container* can be achieved with writing the command after the image name
* e.g., `docker run -i adoptopenjdk bash`
* parameters for the custom command can follow
* use the `t` option to run in a *pseudo-tty* (always use it whenever you use `-i`)
* use the `--rm` to remove the container after use

---

## Interaction with the outside world

A docker container runs *in isolation*, w.r.t. the host and other containers.

Environment variables, network ports, and file system folders are **not** shared.

Sharing must be explicit and requires options to be specified after `docker run`:

* Passing environment variables: `-e <name>=<value>`
* Mounting volumes: `-v <host>:<guest>:<options>`
    * `<host>` is the path (or volume name) on the host system
    * `<guest>` is the location where it will be mounted on the container
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

## Creating Docker images

Docker images are written in a `Dockerfile`

Every command inside a Dockerfile generates a new *layer*

The final stack of layers creates the final *image*

The `docker build` command interprets the Dockerfile commands to produce a sequence of layers

Changes to a layer do not invalidate previous layers

---

{{% section %}}

## Dockerfile syntax

```dockerfile
# Pulls an image from docker hub with this name. Alternatively, "scratch" can be used for an empty container
FROM alpine:latest
# Runs a command
RUN apk update; apk add nodejs npm
# Copies a file/directory from the host into the image
COPY path/to/my/nodejs/project /my-project
# Sets the working directory
WORKDIR /my-project
# Runs a command
RUN npm install
# Adds a new environment variable
ENV SERVICE_PORT=8080
# Exposes a port
EXPOSE 8080
# Configures the default command to execute
CMD npm run service
```

to be built by means of the command: `docker build -t PATH`
- `PATH` is the host path containing the `Dockerfile`

---

## Content of the `path/to/my/nodejs/project` DIRECTORY

1. `Dockerfile` as for the previous slide

{{< multicol >}}
{{% col %}}
2. File `package.json`
```json
{
  "name": "example",
  "version": "1.0.0",
  "description": "Example Express WS for exemplifying Docker usage",
  "main": "index.mjs",
  "scripts": {
    "service": "node index.mjs"
  },
  "dependencies": {
    "express": "^4.18.2"
  },
  "author": "gciatto",
  "license": "ISC"
}
```
{{% /col %}}
{{% col %}}
``````
{{% /col %}}
{{% col %}}
3. File `index.mjs`
```js
import { env } from 'process'
import express from 'express'
import { randomBytes } from 'crypto'

const port = 'SERVICE_PORT' in env ? env.SERVICE_PORT : 8080
const hostname = 'HOSTNAME' in env ? env.HOSTNAME : 'localhost'
const serverID = randomBytes(8).toString('hex') // 8-char random string

const server = express()

let counter = 0
 
server.get('/', function (req, res) {
  res.send(`[${serverID}@${hostname}:${port}] Hit ${++counter} times`)
})
 
console.log(`Service ${serverID} listening on ${hostname}:${port}`)
server.listen(port)
```
i.e. a Web service listening on the port indicated by the `SERIVICE_PORT` env var, showing Web pages of the form 
```
[$SERVER_ID@$HOST_NAME:PORT] Hit $VIEW_COUNT times
```
{{% /col %}}
{{< /multicol >}}

4. File `.dockerignore`
```gitignore
node_modules/
```

{{% /section %}}

---

## Layers and caching (pt. 1)

Every line in a `Dockerfile` generates a new layer:

![](./layers-1.png)

A layer is a *diff* w.r.t. the previous one

In other words, Docker keeps track of what information is added to the image at each step

---

## Layers and caching (pt. 2)

When a `Dockerfile` is built, Docker checks whether the layer has already been built in the recent past

If so, it reuses the cached layer, and skips the execution of the corresponding command

![](./layers-2.png)

---

## Layers and caching (pt. 3)

When the container is run, the images layers are `read-only`, and the container has a `read-write` layer on top of them

![](./rw-layer.png)

So, when a container is stopped, the read-write layer is discarded, and the image layers are kept

In this way, the _space occupied_ by the container is _minimal_

---

## Naming images

Image naming is done via *tags*

The easiest way to do so is assigning tags at *build time* with the `-t` options of `docker build`

The option can be repeated multiple times to make multiple tags

```bash
docker build -t "myImage:latest" -t "myImage:0.1.0" PATH
```

`latest` is usually used to identify the most recent version of some image

For instance, you may build the image from a few slides ago with:
* `docker build -t my-service path/to/my/nodejs/project`
    + the image `my-service:latest` is automatically created

---

## Publishing Docker images

Images get published in *registries*

The most famous, free for publicly available images, is *Docker Hub*

By default, Docker uses Docker Hub as registry (both for `pull` and `push` operations)

Docker Hub requires registration and CLI login:
* `docker login docker.io`

Once done, publication is performed via `push`:
* `docker push <image name>`

---

## Building Docker images in CI

Of course, as any other software, *custom docker images should get built in CI*

Several integrators use containers as build environments: it is possible to *build a container using a container*

More in general, there is *no inherent limit to nesting containers*

For instance:
* you may run DIND (Docker-in-Docker) via: `docker run --privileged --rm -it docker:dind`
* you may use the Docker CLI in a container: `docker run -it --rm docker:cli`

---

## Volumes

Volumes are *bridges* between the _host's_ __file system__ and the _container's_ one

<br/>

Their support several use cases:
- __sharing__ data between the host and the container
- __persisting__ containers' data
- sharing data among containers

---

## Sorts of volumes (pt. 1)

![](./volume-types.png)

- __Bind mount__: a host directory is __mounted__ into the container
    + the host directory is specified as `<host path>:<guest path>` (both must be __absolute paths__)
        + e.g. `docker run -v /home/user/my-project:/my-project ...`
    + data present in `<host path>` can be read within the container at `<guest path>`
    + data written within the container at `<guest path>` can be accessed on `<host path>`
        * even after the container is stopped / deleted

- __Named volume__: virtual drives managed by Docker
    + they are created with `docker volume create <name>`
    + they are mounted into containers as `<name>:<guest path>`
    + their content's life span is independent of any container
    + they can be attached to containers at run-time, referencing their name:
        * e.g. `docker run -v my-volume:/my-project ...`
    + technically, they are just directories on the host's file system, yet handled by Docker
        * `/var/lib/docker/volumes/<name>/_data`

---

## Sorts of volumes (pt. 2)

![](./volume-types.png)

- __Tmpfs mount__ [Linux-only]: a temporary file system, living only as long as the container is alive
    + it is mounted into containers as `tmpfs:<guest path>`
    + it is useful for storing temporary data
    + do not use it for sharing or persisting data
    + it is like an anonymous, ephemeral named volume
    + created by means of a `--tmpfs` option to `docker run`
        * e.g. `docker run --tmpfs <guest path> ...`

---

## Example: containers' data is ephemeral

1. Let's open a Linux shell in a container: `docker run -it --rm alpine:latest sh`

2. Let's create folder in there: `mkdir -p /data`

3. Let's create a file in there: `echo "Hello world" > /data/hello.txt`

4. Does the file exist? `ls -la /data`
    ```
    / # ls -la /data
    total 12
    drwxr-xr-x    2 root     root          4096 Nov  2 09:28 .
    drwxr-xr-x    1 root     root          4096 Nov  2 09:28 ..
    -rw-r--r--    1 root     root             5 Nov  2 09:28 hello.txt
    ```

5. Let's exit the container: `exit`

6. Let's open a new shell in a new container: `docker run -it --rm alpine:latest sh`

7. Does the file exist? `ls -la /data`
    ```
    ls: /data: No such file or directory
    ```

8. Why?
    + the second `docker run ...` command created a new container

---

## Example: sharing data with bind mounts (pt. 1)

1. Let's create a folder on the host: `mkdir -p $(pwd)/shared`

2. Let's create 10 containers
    1. each one creating a file in `./shared/`
    2. named `container-<i>.txt`
    3. containing random data from `/dev/random`, encoded in base64

    ```bash
    for i in $(seq 1 10); do 
        docker run --rm -d \           # detached mode
            -v $(pwd)/shared:/data \   # bind mount
            --hostname container-$i \  # parametric hostname inside the container
            alpine sh -c \
            'cat /dev/random | head -n 1 | base64 > /data/$(hostname).txt'  
    done
    ```

3. Let's have at the shared directory: `ls -la ./shared`
    ```
    ls -la ./shared 
    totale 56
    drwxr-xr-x  2 user    user     4096  2 nov 10.51 .
    drwxr-xr-x 79 user    user    12288  2 nov 10.56 ..
    -rw-r--r--  1 root    root      329  2 nov 10.51 container-10.txt
    -rw-r--r--  1 root    root      333  2 nov 10.51 container-1.txt
    -rw-r--r--  1 root    root      381  2 nov 10.51 container-2.txt
    -rw-r--r--  1 root    root      167  2 nov 10.51 container-3.txt
    -rw-r--r--  1 root    root      110  2 nov 10.51 container-4.txt
    -rw-r--r--  1 root    root      102  2 nov 10.51 container-5.txt
    -rw-r--r--  1 root    root        9  2 nov 10.51 container-6.txt
    -rw-r--r--  1 root    root      106  2 nov 10.51 container-7.txt
    -rw-r--r--  1 root    root      766  2 nov 10.51 container-8.txt
    -rw-r--r--  1 root    root      203  2 nov 10.51 container-9.txt
    ```

---

## Example: sharing data with bind mounts (pt. 2)

4. Things to notice:
    + the files are owned by `root` (inside the container)
    + the files are still owned by `root` on the host (outside the container)
        * despite the fact that the host user is `user`
        * if `user` has no superuser privileges, they cannot delete the files
    + when creating the container, bind mounts should be __absolute__ paths
        * hence the need for `$(pwd)` to get the current working directory

---

## Example: sharing data with named volumes (pt. 1)

1. Let's create a named volume: `docker volume create my-volume`

2. Same 10 containers as before, but using the named volume instead of the bind mount

    ```bash
    for i in $(seq 1 10); do 
        docker run --rm -d \           # detached mode
            -v my-volume:/data \       # reference to volume my-volume
            --hostname container-$i \  # parametric hostname inside the container
            alpine sh -c \
            'cat /dev/random | head -n 1 | base64 > /data/$(hostname).txt'  
    done
    ```

3. How to access the data now?
    1. by means of another container, attached to the same volume
    2. by means of the host, inspecting the volume's file system

---

## Example: sharing data with named volumes (pt. 2)

### Accessing the volume from another container

1. Let's create yet another _interactice_ container attached to the volume
    * `docker run --rm -it -v my-volume:/data alpine sh`
2. Let's have a look at the data: `ls -la /data`
    ```
    total 48
    drwxr-xr-x    2 root     root          4096 Nov  2 10:15 .
    drwxr-xr-x    1 root     root          4096 Nov  2 10:15 ..
    -rw-r--r--    1 root     root           608 Nov  2 10:14 container-1.txt
    -rw-r--r--    1 root     root           349 Nov  2 10:15 container-10.txt
    -rw-r--r--    1 root     root           369 Nov  2 10:14 container-2.txt
    -rw-r--r--    1 root     root           304 Nov  2 10:14 container-3.txt
    -rw-r--r--    1 root     root           240 Nov  2 10:14 container-4.txt
    -rw-r--r--    1 root     root           434 Nov  2 10:14 container-5.txt
    -rw-r--r--    1 root     root           150 Nov  2 10:14 container-6.txt
    -rw-r--r--    1 root     root            41 Nov  2 10:14 container-7.txt
    -rw-r--r--    1 root     root            33 Nov  2 10:15 container-8.txt
    -rw-r--r--    1 root     root          1212 Nov  2 10:15 container-9.txt
    ```

---

## Example: sharing data with named volumes (pt. 3)

### Accessing the volume from the host

3. Alternatively, let's find where the volume is stored on the host
    * `docker volume inspect my-volume
4. Let's analyse the JSON description of the volume:
    ```json
    [
        {
            "CreatedAt": "2023-11-02T11:13:56+01:00",
            "Driver": "local",
            "Labels": null,
            "Mountpoint": "/var/lib/docker/volumes/my-volume/_data",
            "Name": "my-volume",
            "Options": null,
            "Scope": "local"
        }
    ]
    ```

3. The `Mountpoint` entry reveals the location of the volume on the host's file system

4. Let's look into that position (may require _super-user_ access rights):
    * `sudo ls -la /var/lib/docker/volumes/my-volume/_data`
    
        ```
        totale 48
        drwxr-xr-x 2 root root 4096  2 nov 11.15 .
        drwx-----x 3 root root 4096  2 nov 11.13 ..
        -rw-r--r-- 1 root root  349  2 nov 11.15 container-10.txt
        -rw-r--r-- 1 root root  608  2 nov 11.14 container-1.txt
        -rw-r--r-- 1 root root  369  2 nov 11.14 container-2.txt
        -rw-r--r-- 1 root root  304  2 nov 11.14 container-3.txt
        -rw-r--r-- 1 root root  240  2 nov 11.14 container-4.txt
        -rw-r--r-- 1 root root  434  2 nov 11.14 container-5.txt
        -rw-r--r-- 1 root root  150  2 nov 11.14 container-6.txt
        -rw-r--r-- 1 root root   41  2 nov 11.14 container-7.txt
        -rw-r--r-- 1 root root   33  2 nov 11.15 container-8.txt
        -rw-r--r-- 1 root root 1212  2 nov 11.15 container-9.txt
        ```

---

## How to retain space from a volume?

Volumes are not automatically cleaned up when containers are deleted

Volumes must be __explicitly__ deleted by the user
* `docker volume rm <volume name>`

In our example, remember to delete `my-volume`:
* `docker volume rm my-volume`
    * beware: this will delete all the data in the volume!

---

## Sudo-powered containers

- Sometimes one may be willing to let a container access the Docker daemon of its host
    * this is useful for running containers that can: create/delete other containers, handle images, etc.

- To achieve that one may exploit __bind mounts__
    * to share the Docker daemon's socket with the container

- Explanation
    * on the host, the Docker daemon is just a service listening for commands on a [Unix socket](https://en.wikipedia.org/wiki/Unix_domain_socket)
    * the socket is usually reified as a file on the host's file system, at `/var/run/docker.sock`
    * the `docker` command is just a CLI program writing/reading to/from the socket to govern the daemon

- One may simply create a Docker container with the Docker CLI tool installed on it
    * and share the host's Docker socket with that container via a bind mount
        + `docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock --name sudo docker:cli sh`

- Inside that container one may run the `docker` command as usual, to govern the host's Docker daemon
    * e.g. `docker ps`

    ```
    CONTAINER ID   IMAGE        COMMAND                  CREATED          STATUS          PORTS      NAMES
    3d9016cd067e   docker:cli   "docker-entrypoint.s…"   5 seconds ago    Up 3 seconds               sudo
    ```

---

## Networks

Docker may virtualise container's networking facilities
1. network interfaces
2. IP addresses
3. layer-4 ports

<br/>

This is useful for letting containers communicate with any external entity
- most commonly, other containers or the host

<br/>

Networks are virtual entities managed by Docker
- they mimic the notion of __layer-3 network__
- in the eyes of the single container, they are __virtual network interfaces__
- overall, their purpose is to __delimit__ the scope of communication for containers

---

## Sorts of networks (pt. 1)

Different sorts of networks are available in Docker, depending on the __driver__:

![](./networks.png)

---

## Sorts of networks (pt. 2)

Available drivers:
- __none__: no networking facilities are provided to the container, which is then isolated

- __host__: remove network isolation between the container and the Docker host, and use the host's networking directly

- __bridge__ (default): a virtual network, internal to the host, letting multiple containers communicate with each other

- __overlay__: a virtual network, spanning multiple hosts, letting multiple containers communicate with each other among different nodes of the same cluster

<br/>

Other drivers are available as well, cf. https://docs.docker.com/network/drivers/

---

### None network driver

{{< figure src="./none-network.png" width="20%" >}}

- __No__ networking facilities are provided to the container __at all__, which is then _isolated_ w.r.t the external world

- This is useful for containers that do not need to communicate via the network
    + i.e. they _expose no service_, and they __must not access__ the Internet
        * very rare situation

- Example: `docker run --network none -it --rm alpine sh`
    ```bash
    / # ping 8.8.8.8
    PING 8.8.8.8 (8.8.8.8): 56 data bytes
    ping: sendto: Network unreachable
    ```

---

### Host driver

{{< figure src="./host-network.png" width="20%" >}}

- __No isolation__ between the container and the host
    + the container uses the host's networking facilities _directly_

- The main consequence is that the container will be assigned with the __same IP address as the host__
    + the layer-4 ports used by the container are __also busy__ for the host
        * high __collision__ probability

- Example: `docker run --network host my-service`
    ```
    Service 540c3b7bb7860c6a listening on <your pc's hostname>:8080
    ```

    * try now to start a service on port 8080 on your host
        + you should get an error message stating that port 8080 is already in use

---

## Bridge network (pt. 1)

{{< figure src="./bridge-network.png" width="30%" >}}

- __Virtual network__ spanning through _one or more containers_ on the _same host_
    + containers attached to the _same_ network can __communicate__ with each other via _TCP/IP_
    + each container may be attached to __multiple networks__
        * e.g. one for internal communication, one for external communication

- Each container gets assigned with a __private IP address__ on the network
    + commonly in the range `172.x.y.z`
    + the IP address is contactable from the host

---

## Bridge network (pt. 2)

To exemplify the usage of IP:

1. let's create a container using the `my-service` image created a few slides ago
    * `docker run --network bridge -d --rm --name my-container my-service`
    * this should be a service listening on port `8080`
    * we call the container `my-container`
    * specifying `--network bridge` is useless, because that's the default

2. let's inspect the container's IP address
    * `docker inspect my-container | grep IPAddress`
        + at the time of writing, the IP address is `172.17.0.2`

3. open your browser and browse to `http://172.17.0.2:8080`
    * you should see a page of the form: `[$SERVER_ID@$HOST_NAME:PORT] Hit $VIEW_COUNT times`
    * the page is served by the container, contacted by IP
    * so containes attached to `bridge`-like networks can be contacted by IP, from the host

4. try to contact the container:
    * by host name, from the host
        + this should not work $\Rightarrow$ no host-specific DNS resolution for containers
    * by IP, from another host (in the same LAN of the host)
        + this should not work $\Rightarrow$ _bridge_ networks are host specific

---

## Bridge network (pt. 3)

Let's create a non-trivial scenario with 2 container attached to the same network:
1. one container (host name: `dind`) will run the Docker daemon (Docker-in-Docker)
2. the other container (host name: `cli`) will run the Docker CLI
3. let's call the shared network `my-network`

---

## Bridge network (pt. 4)

1. let's create the network: `docker network create --driver bridge my-network`
    * a random string is returned: that's the network's ID

2. let's create the daemon: 
    * `docker run --privileged -d --rm --network my-network --hostname dind docker:dind`

3. let's create the client: `docker run -it --rm --network my-network --hostname cli docker:cli`
    1. inside `cli`, let's check if `dind` is contactable by hostname: `ping dind`
        * this should work $\Rightarrow$ networks come with their own __name resolution__ support
    2. inside `cli`, let's check if the Docker client works: `docker ps`
        ```
        error during connect: Get "http://docker:2375/v1.24/containers/json": dial tcp: lookup docker on 127.0.0.11:53: no such host
        ```
    
    3. apparently the `docker:cli` image is preconfigured to contact a daemon on the `docker` hostname

4. let's stop `dind`:
    - you can use `docker ps` on the host to get the name of the `dind` container
    - then `docker stop <container name>`
    - or `docker stop $(docker ps -q --filter ancestor=docker:dind)`
        + `-q` (_quiet_), only display container IDs
        + `--filter ancestor=X` selects containers whose image is `X`
    - next time let's give an explicit name to the container via `--name`
---

## Bridge network (pt. 5)

5. let's restart a Docker-in-Docker daemon, using the `docker` hostname:
    * `docker run --privileged -d --rm --network my-network --name dind --hostname docker docker:dind dockerd --host=tcp://0.0.0.0:2375`
    * in general, it would be better to both `--name X` and `--hostname X`
        + same name and hostname to avoid confusion!
    * `dockerd --host=tcp://0.0.0.0:2375` is necessary to force the port the daemon will listen on
        + and to disable TLS security (not recommended in production)

6. let's restart the client: `docker run -it --rm --network my-network --hostname cli docker:cli`

7. let's run a command on the CLI, say `docker run hello-world`
    * this should work $\Rightarrow$ the client is now able to contact the daemon via the `docker` hostname

---

## Exposing ports

- When a container wraps a __service__ listening on __layer-4 port__...
    + ... the port is __not accessible__ from the _outside_ world
    + ... unless it is __explicitly exposed__ by the container

- When _creating_ a Docker _image_, the `EXPOSE` command in the `Dockerfile` is used to declare ports _exposed by default_
    + e.g. `EXPOSE 8080`

- The port exposed by a container can be inspected via `docker ps`
    + e.g. `docker run -d --rm my-service; docker ps`
    ```
    CONTAINER ID   IMAGE        COMMAND                  CREATED        STATUS                  PORTS      NAMES
    7a14f3bf95cc   my-service   "/bin/sh -c 'npm run…"   1 second ago   Up Less than a second   8080/tcp   wonderful_goldwasser
    ```

    (look at the `PORTS` column)

- When _running_ a Docker _container_, the exposed port can be mapped to host's ports via `-P` option
    + this would make any `EXPOSE`d port in the image mapped to some _random_ port of the host
    + e.g. `docker run -d --rm -P my-service; docker ps`
    + the actual port mapping you should run `docker ps`

    ```
    CONTAINER ID   IMAGE        COMMAND                  CREATED         STATUS         PORTS                                         NAMES
    b1470831b62b   my-service   "/bin/sh -c 'npm run…"   6 seconds ago   Up 5 seconds   0.0.0.0:32773->8080/tcp, :::32773->8080/tcp   sad_benz
    ```

- One may control the mapping of ports via the `-p <host port>:<guest port>` option
    + e.g. `docker run -d --rm -p 8888:8080 my-service; docker ps`
    + this would map the container's port `8080` to the host's port `8080`

    ```
    CONTAINER ID   IMAGE        COMMAND                  CREATED         STATUS         PORTS                                       NAMES
    f034da93c476   my-service   "/bin/sh -c 'npm run…"   4 seconds ago   Up 3 seconds   0.0.0.0:8888->8080/tcp, :::8888->8080/tcp   bold_booth
    ```