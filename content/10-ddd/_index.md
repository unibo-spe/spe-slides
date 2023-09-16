+++

title = "Domain Driven Design"
description = "Practical introduction to Domain Driven Design"
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

# Domain Driven Design  


## {{< course_name >}}

<br>

### [Giovanni Ciatto --- `giovanni.ciatto@unibo.it`](mailto:giovanni.ciatto@unibo.it)

<br>

Compiled on: {{< today >}} --- [<i class="fa fa-print" aria-hidden="true"></i> printable version](?print-pdf&pdfSeparateFragments=false)

[<i class="fa fa-undo" aria-hidden="true"></i> back](..)

---

# Motivation and Context

---

## Why a structured design process?

- You know programming, in many programming languages

- You know about object-oriented programming and design patterns

- You know about software architectures and design principles

- You know about software engineering best practices

<br>

*What's the __criterion__ to choose __if__ and __when__ to adopt languages / patterns / architectures / principles / practices?*

---

## Recommended workflow

### Problem $\xrightarrow{\color{red}analysis}$ **Model** $\xrightarrow{\color{red}design}$ Architecture $\xrightarrow{implementation}$ Solution

- yet, how to derive the model?

---

## Why Domain Driven Design?

- Here we present __domain-driven design__ (DDD)
    + one of many approaches to software design

- It consists of principles, best practices, and patterns leading design
    + unified under a common _philosophy_
    + focus is on the _design workflow_, other than the result

- We prefer it over alternatives for several reasons:
    + it stresses _adherence_ to the problem at hand
    + it focuses on delivering a _business-tailored_ model
        * and, therefore, a business-tailored solution
    + it harmonises _communication_ among managers, technicians, and users
    + it stresses the production of maintanable and _extensible_ software

---

#  Main notions of DDD

---

{{% section %}}

## What is the domain?

- Definition of __domain__:
    + a well established sphere of knowledge, influence or activity
    + the subject area to which the user applies the software

- Remarks:
    + focus is on how users and experts perceive the domain
    + focus is not on how developers perceive the domain

- Examples of domains and the contexts composing them
    + some university (department, faculty, HR, etc.)
    + some given company (manufacturing, marketing, HR, etc.)
    + linear algebra (matrices, complex numbers, polynoms, etc.)
    + machine learning (classification, regression, feature selection, etc.)

---

## DDD Philosophy (pt. 1)

- Software will represent a solution to a problem in some business domain
    + it should be modelled & implemented to match that domain
        * i.e. modelling should elicit the key aspects of a domain, possibly by interacting with experts

- Words do not have meaning per se, but rather w.r.t. a domain
    + i.e. the same word may have different meanings in different domains
    + each domain comes with a particular language characterising it
        * SW components (interfaces, classes, etc.) should be named after that language
    + interaction with experts is essential to identify a domain’s language

---

## DDD Philosophy (pt. 2)

- SW should stick to the domain, at any moment
    + archiecture and implementation should favour adherence to the domain
        * in spite of their evolution / modification

- Functionalities, structure, and UX should mirror the domain too
    - using the libraries should be natural for developers
    - UX should be natural for users

    as both developers and users are (supposed to be) immersed in the domain

{{% /section %}}

---

{{% section %}}

## Overview of main notions 

- __Domain__: the reference area of knowledge

- __Context__: a portion of the domain

- __Model__: a reification of the domain in SW

- __Ubiquitous Language__: language used by domain experts and mirrored by the model

---

## The _Domain_

> A well established sphere of knowledge, influence or activity

- e.g. some university (department, faculty, HR, etc.), linear algebra, etc.

---

## _Contexts_

> A portion of the domain:
> - relying on a sub-set of the concepts of the domain
> - where words/names have a unique, precise meaning

- e.g. departments, divisions, complex numbers, etc.

--- 

## Domain _vs._ Context

![Domain and context](./domain.png)

---

## The domain _Model_

> Set of __software__ abstractions mapping relevant _concepts_ of the domain

- e.g. C# projects, namespaces, interfaces, classes, structures, methods, etc.

---

## The _Ubiquitous Language_

> - A _language_ structured around the domain model
>   + _used by all people_ involved into the domain
>   + which should be _used in the software_
>   + in such a way that their _semantics is preserved_

- underlying assumption:
    * different people call the same things differently
    * especially when they come from different contexts

- commonly reified into a __glossary__ of terms

- commonly used to name software components

{{% /section %}}

---

{{% section %}}

## Conceptual Workflow

1. Identify the domain

2. Identify the main contexts within the domain
    -  possibly, by interacting with experts

3. Identify the actual meaning of commonly used words within the domain
    - possibly, by interacting with experts
    
    - _without assuming you already know the meaning of words_
        + i.e. do __not__ rely on (your) __common sense__

    - keep in mind that the meaning of word may vary among contexts
        + this is not just a glossary, but also
            * idiomatic or domain-specific expressions
            * procedures, events, etc.

4. Adhere to the language, use it, make it yours
    - especially when talking about the domain / model / software
    - design/sketch code mirroring the language

5. Draw a __context map__ tracking
    - the main contexts and their junctions
    - words whose meaning varies across contexts

6. Model the software around the ubiquitous language
    - rule of thumb: __1 concept $\approx$ 1 interface__

---

## Example of context map

![Context map](./context-map.jpg)

{{% /section %}}

---

# DDD Building Blocks

---

## Towards building blocks

{{< multicol >}}
{{% col %}}
Domain

- Concept
    + instance
{{% /col %}}
{{% col %}}
$\xrightarrow{modelling}$
{{% /col %}}
{{% col %}}
Model

- Type
    + object
{{% /col %}}
{{< /multicol >}}

- Each _concept_ from each **context** shall become a _type_ in the **model**
    + type $\approx$ class, interface, structure, ADT, etc.
        * depends on what the programming language has to offer

- Use _building blocks_ as __archetypes__
    + let them guide and constrain your design

---

## Workflow

(continued)

7. Chose the most adequate building block for each concept
    - depending on the nature of the concept
    - ... or the properties of its instances

8. The building block dictates how to design the type corresponding to the concept
    - objects in OOP are shaped by types

9. The choice of building block may lead to the identification of other concepts / models
    - e.g. entities may need value objects as identifiers
    - e.g. entities may need repositories to be stored
    - e.g. entities may need factories to be created
    - e.g. aggregates may be composed by entities or value objects

---

{{% section %}}

## Building blocks (overview)

- __Entity__: objects with an identifier

- __Value Object__: objects without identity

- __Aggregate Root__: compound objects

- __Domain Event__: objects modelling relevant event (notifications)

- __Service__ objects: providing stateless functionalities

- __Repository__: objects providing storage facilities

- __Factory__: objects creating other objects

--- 

## Building blocks (concept)

![Building blocks graphical overview](./building-blocks.png)

---

## Entities vs. Value Objects

> Genus-differentia definition:
> - _genus_: both can be used to model __elementary__ concepts
> - _differentia_: entities have an explicit __identity__, value objects are __interchangeable__

<br>

### Quick modelling examples

#### Classroom

- Seats in classroom may be modelled as value-objects

- Attendees of a class may be modelled as entities

#### Seats on a plane

- Numbered seats $\rightarrow$ entities

- otherwise $\rightarrow$ value objects

---

## Entities vs. Value Objects (constraints)

### Value Objects

- Identified by their attributes
    + equality compares attributes alone
- Must be stateless $\Rightarrow$ require an immutable design
    + read-only properties
    + lack of state-changing methods
- May be implemented as 
    - structures in .NET
    - data classes in Kotlin, Scala, Python
    - records in Java
- Must implement `equals()` and `hashCode()` on JVM
    + implementation must compare the objects' attributes

---

## Entities vs. Value Objects (constraints)

### Entities

- They have an inherent identity, which never changes during their lifespan
    + common modelling: __identifier attribute__, of some value type
    + equality compares identity
- Can be stateful $\Rightarrow$ may have a mutable design
    + modifiable properties
    + state-changing methods
- May be implemented via classes in most languages
- Must implement `equals()` and `hashCode()` on JVM
    + implementation must compare (at least) the objects' identifiers

---

## Entities vs. Value Objects (example)

{{< plantuml >}}
interface Customer {
    + CustomerID getID()
    + String getName()
    + void **setName**(name: String)
    + String getEmail()
    + void **setEmail**(email: String)
}
note left: Entity

interface CustomerID {
    + Object getValue()
}
note right: Value Object

interface TaxCode {
    + String getValue()
}
note left: Value Object

interface VatNumber {
    + long getValue()
}
note right: Value Object

VatNumber -d-|> CustomerID
TaxCode -d-|> CustomerID

Customer *-r- CustomerID
{{< /plantuml >}}

---

## Aggregate Root

### Definition

- A composite object, aggregating related entities/value objects

- It ensures the consistency of the objects it contains

- It mediates the usage of the composing objects from the outside
    + acting as a façade

- Outside objects should avoid holding references to composing objects

---

## Aggregate Root (constraints)

- They are usually _compound_ entities

- They can be or exploit _collections_ to contain composing items
    + they may leverage on the [composite pattern](https://en.wikipedia.org/wiki/Composite_pattern)

- May be better implemented as classes in most programming languages

- Must implement `equals()` and `hashCode()` on JVM
    + implementation may take composing items into account

- Components of an aggregate should _not_ hold **references** to components of _other_ aggregates
    + that's why they are called aggregate _roots_

    ![Aggregate roots should not hold references to other aggregates' components](./aggregate-references.png)

---

## Aggregate Root (example)

![Two aggregates with inter-dependencies](./aggregate-root.png)

---

## Factories (definition)

> Objects aimed at __creating__ other objects

<br>

![Concept of factory](./factories.png)

---

## Factories (details)

### Purpose

+ Factories encapsulate the creation logic for complex objects
    * making it evolvable, interchangeable, replaceable

+ They ease the enforcement of invariants

+ They support dynamic selection of the most adequate implementation

### Remarks

- DDD’s notion of factory is quite wide
    + DDD's Factories $\supset$ GOF's Factories $\cup$ Builders $\cup$ ...

---

## Factories (constraints)

- They are usually identityless and stateless objects
    + recall the [abstract factory pattern](https://en.wikipedia.org/wiki/Abstract_factory_pattern)

- May be implemented as classes in most OOP languages

- Provide methods to instantiate entities or value objects

- Usually they require no mutable field/property

- No need to implement `equals()` and `hashCode()` on JVM

---

## Factories (example)

{{< plantuml >}}
interface CustomerID

interface TaxCode

interface VatNumber

interface Customer

Customer "1" *-- "1" CustomerID

VatNumber -u-|> CustomerID
TaxCode -u-|> CustomerID

interface CustomerFactory {
    + VatNumber computeVatNumber(String name, String surname, Date birthDate, String birthPlace)
    --
    + Customer newCustomerPerson(TaxCode code, String fullName, string email)
    + Customer newCustomerPerson(String name, String surname, Date birthDate, String birthPlace, String email)
    --
    + Customer newCustomerCompany(VatNumber code, String fullName, String email)
}

CustomerFactory -r-> VatNumber: creates
CustomerFactory -u-> Customer: creates
{{< /plantuml >}}

--- 

## Repositories (definition)

> Objects mediating the persistent storage/retrieval of other objects

![Concept of repositories](./repositories.png)

---

## Repositories (details)

### Purpose

- Hiding (i.e. be backed by) some database technology 
- Realising an object-relational mapping (ORM)
- Storing / retrieving aggregate roots as wholes
- Supporting CRUD operations on aggregate roots
    - Create, Read, Update, Delete

### Remarks

- They may exploit factories for information retrieval
- If properly engineered, avoids lock-in effect for DB technologies 
- Design & implementation may require thinking about:
    - the architecture, the infrastructure, the expected load, etc.

---

## Repositories (constraints)

- They are usually identity-less, stateful, and composed objects 
    + state may consist of the stored objects
    + state may consist of DB connections

- May be implemented as classes in most OOP languages

- Provide methods to
    + add, remove, update aggregate root entities 
    + select and return one or more entity, possibly in a lazy way
        * this should return `Iterable` or some `Collection` on JVM 

- Non-trivial implementations should take care of
    + enforcing consistency, in spite of concurrent access I support complex transactions

---

## Repositories (example)

{{< plantuml >}}
interface CustomerID 

interface Customer

Customer "1" *-u- "1" CustomerID

interface CustomerRegistry {
    + Iterable<Customer> getAllCustomers()
    ..
    + Customer findCustomerById(CustomerID id)
    + Iterable<Customer> findCustomerByName(string name)
    + Iterable<Customer> findCustomerByEmail(string email)
    ..
    + void addNewCustomer(Customer customer)
    + void updateCustomer(Customer customer)
    + void removeCustomer(Customer customer)
}

CustomerRegistry "1" o--> "N" Customer: contains
CustomerRegistry --> CustomerID: exploits
{{< /plantuml >}}

---

## Services

> Functional objects encapsulating the business logic of the software (commonly stateless & identity-less),
> e.g. operations spanning through several entities, objects, aggregates, etc.

### Purpose
- Reifying control-related aspects of the software
- Wiring aggregates, entities, and value objects together 
- Exposing coarse-grained functionalities to the users
- Providing a façade for the domain
- Make the business logic evolvable, interchangeable, replaceable

### Remarks

- Services may be exposed via ReSTful API
- Should be designed keeping current uses cases into account
    + entities/objects should support future use cases, too

---

## Services (constraints)

- They are usually identity-less, stateless objects 

- May be implemented as classes in OOP languages
    + or bare functions in functional languages

- Commonly provide procedures to do business-related stuff 
    + e.g. a particular operation
        * concerning some particular aggregate root 
        * which does not support it directly 
        * because the operation is use-case specific
    + e.g. proxying an external service
    + e.g. a complex operation involving aggregates, repositories, factories, etc.

- Non-trivial implementations should take care of
    + supporting concurrent access to the service’s facilities 
    + exposing domain events to the external world

--- 

## Services (example)

{{< plantuml >}}
interface OrderManagementService {
    + void performOrder(Order order)
}

interface Order {
    + OrderID getId()
    + Customer getCustomer()
    + void setCustomer(Customer customer)
    + Date getTimestamp()
    + void setTimestamp(Date timestamp)
    + Map<Product, long> Amounts getAmounts()
}

interface OrderID

interface Customer

interface Product

interface OrderStore

Order "1" *-r- "1" OrderID
Order "1" *-d- "1" Customer
Order "1" *-u- "N" Product
OrderStore "1" *-- "N" Order

OrderManagementService ..> Order: handles
OrderManagementService ..> OrderStore: updates

note bottom of OrderStore: repository
note top of Order: entity
note right of OrderID: value object
note right of Product: entity
note right of Customer: entity
note top of OrderManagementService: service

OrderID -u[hidden]- Product
OrderID -d[hidden]- Customer 

{{< /plantuml >}}

---

## Domain Events (definition)

> A value-like object capturing some domain-related event (i.e. a relevant variation)

- actually, only the event notification/description is reified to a type

![Concept of domain events](./domain-events.png)

---

## Domain Events (details)

### Purpose
- Propagate changes among portions of the domain model 
- Record changes concerning the domain

### Remarks
- Strong relation with the [observer pattern](https://en.wikipedia.org/wiki/Observer_pattern) (i.e. publish-subscribe) 
- Strong relation with the event sourcing approach (described later) 
- Strong relation with the CQRS pattern (described later)

---

## Domain Events (constraints)

- They are usually time-stamped value objects

- May be implemented as data-classes or records

- They represent a relevant variation in the domain
    + e.g. a change in the state of some entity / repository

- Event sources & listeners shall be identified too
    + who is generating the event?
    + who is consuming the event?

- Infrastructural components may be devoted to propagate events across contexts
    + e.g. a message broker, a message queue, etc.

---

## Domain Events (example)

{{< plantuml >}}
interface OrderManagementService {
    + void performOrder(Order order)
    ..
    + void **notifyOrderPerformed**(OrderEventArgs event)
}

interface OrderEventArgs {
    + OrderID getID()
    + CustomerID getCustomer()
    + Date **getTimestamp**()
    + Dictionary<ProductID, long> getAmounts()
}

interface OrderID

interface CustomerID

interface ProductID

OrderEventArgs "1" *-u- "1" OrderID
OrderEventArgs "1" *-r- "1" CustomerID
OrderEventArgs "1" *-d- "N" ProductID

OrderEventArgs .. OrderManagementService

note left of OrderEventArgs: domain event
note left of OrderID: value object
note left of ProductID: value object
note right of CustomerID: value object
note right of OrderManagementService: service
{{< /plantuml >}}

{{% /section %}}

--- 

# DDD Patterns

---

## Towards DDD patterns

### Further notions involving __contexts__

- __Bounded Context__: enforce a model’s boundaries & make them explicit

- __Context Map__: providing a global view on the domain and its contexts

---

## Actual definitions

### Bounded Context

> The explicit boundary of a software model, from a
> - technical (e.g., dependencies among classes/interfaces)
> - physical (e.g., common database, common facilities)
> - organizational (e.g. people maintaining/using the code)
>
> perspective

<br>

### Context Map

> A map of all the contexts in a domain and their boundaries
> - and their points of contact
>   + e.g. their dependencies, homonyms, false friends, etc.
> - providing the whole picture of the domain

---

## Example of bounded context map

![Context map](./bounded-contexts.png)

---

## Bounded Contexts & Context Maps (best practices)

- Clearly identify & represent boundaries among contexts

- Avoid responsibility diffusion over a single context
    + one responsible person / team for each context

- Avoid changing the model for problems arising outside the context
    + rather, extend the domain by creating new contexts

- Enforce context’s cohesion via automated unit and integration testing
    + to be (re)executed as frequently as possible

---

## Model integrity problem

### How to preserve the integrity of the model?

- As the domain evolves, the software model should evolve with it
    + in order to maintain the coupling

- Yet, the domain rarely changes as a whole
    + more commonly, it changes in a context-specific way

- Contexts-are bounded, but not isolated
    + so are models, which may depend on each other

- Changes to a context, and its model may propagate to other context / models

> Domain / model changes are critical and should be done carefully

---

## Model integrity patterns

- __Shared kernel__: sharing a common model among contexts
- __Customer--supplier__: the consumer model's team requests changes in the supplier model
- __Conformist__: one model's team reacts to changes of some model they depend on
- __Anti-corruption layer__: a model's team isolates itself from another model

### Purposes

- _Preserve_ the integrity of the model w.r.t. the domain

- _Minimise_ the potential _impact_ / _reach_ of changes
    + each context should be as independent as possible
    + each change affect as few contexts as possible

--- 

## Model integrity patterns (background, pt. 1)

![Context maps concept](./context-map.jpg)

- Context maps highlight relations among contexts
    + yet, not all relations are equal, nor symmetric

--- 

## Model integrity patterns (background, pt. 2)

![Upstream and downstream roles](./provider-consumer.jpg)

Each relation among 2 contexts usually involves 2 ends/roles:
- __upstream__ end, i.e. the one _providing_ functionalities
- __downstream__ end, i.e. the one _consuming_ functionalities
    + the downstream _depends_ upon the upstream, but _not_ vice versa

<br>

__Integration__ among _contexts_ $\leftrightarrow$ __interaction__ among _teams_
- several strategies may be employed, depending on
    + mutual _trust_* among teams
    + ease of _communication_/cooperation among teams
    + technical / organizational / administrative / legal _constraints_

<br><br>

*trust $\approx$ willingness to collaborate + seek for stability

---

## Shared Kernel

![Shared kernel concept](./shared-kernel.jpg)

- Best when: multiple contexts _share_ the same team / organization / product

- Key idea: _factorise_ common portions of the model into a shared kernel

- Upstream and downstream _collaborate_ in designing / developing / maintaining the model
    + they are _peers_

- Keeping the kernel as _small_ as possible is fundamental

---

## Customer--Supplier

![Customer--supplier concept](./customer-supplier.jpg)

- Best when: 
    + multiple teams
    + mutual trust 
    + good communication

- Key idea:
    + upstream acts as supplier, downstream acts as customer
    + both sides collaborate to maximise integration among their models
        * and interoperability among their SW

- Customers may ask for features, suppliers will do their best

- Suppliers shall warn before changing their model

---

## Conformist

![Conformist concept](./conformist.jpg)

- Best when: 
    + multiple teams
    + poor communication / different pace
    + some trust

- Key idea: downstream must conform to the upstream, reactively
    + adapting their model accordingly
    + whenever the upstream's one changes

---

## Anti-corruption layer

![Anti-corruption layer concept](./anti-corruption-layer.jpg)

- Best when: 
    + multiple teams
    + poor communication 
    + poor trust

 - If upstream cannot be trusted, and interaction is pointless...
    + e.g. legacy code, poorly maintained library, etc.

- ... downstream must defend from unexpected / unanticipated change

- The upstream's model is then reverse engineered & __adapted__
    + e.g. often, repository types are anti-corruption layers for DB technologies