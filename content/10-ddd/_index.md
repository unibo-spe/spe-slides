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

.reveal blockquote::before{
    content: "";
}

.reveal blockquote::after{
    content: "";
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

## Lecture goals

- Understand how __domain-driven design__ guides the derivation of a software _model_ from a _domain_
- Know the DDD __building blocks__ (tactical design) and __integration patterns__ (strategic design)
- Map a DDD model onto a __layered architecture__
- Understand __event sourcing__ and __CQRS__
- Practice on the exercises

---

## Outline

1. [Motivation](#/motivation): _why a structured design process_
2. [Main notions](#/notions): _domain, subdomains, bounded contexts, model, ubiquitous language_
3. [Building blocks](#/building-blocks): _entities, value objects, aggregates, factories, repositories, services, dependency injection, domain events_
4. [Strategic design](#/strategic-design): _bounded contexts, context maps, integration patterns_
5. [Layered architecture](#/architecture): _layers, ports & adapters, modules_
6. [Event sourcing and CQRS](#/advanced): _storing changes, segregating reads and writes_
7. [Exercises](#/exercises)

<br>

> Main references: [Evans, _Domain-Driven Design_, 2003](https://www.domainlanguage.com/ddd/), [Vernon, _Implementing Domain-Driven Design_, 2013](https://www.informit.com/store/implementing-domain-driven-design-9780321834577)

---

{{< slide id="motivation" >}}

# Motivation and Context

---

## Why a structured design process?

- You know programming, in many _programming languages_

- You know about object-oriented programming and _design patterns_

- You know about _software architectures_ and _design principles_

- You know about _software engineering_ best practices

<br>

> What's the __criterion__ to choose __if__ and __when__ to adopt languages / patterns / architectures / principles / practices?

---

## Recommended workflow

```mermaid
flowchart LR
  P([Problem]) -- analysis --> M[[Model]]
  M -- design --> A[Architecture]
  A -- implementation --> S([Solution])

  classDef focus fill:#f9e1e0,stroke:#b5332e,stroke-width:3px,color:#b5332e,font-weight:bold;
  class M focus;
  linkStyle 0,1 stroke:#b5332e,stroke-width:3px
```

- yet, how to derive the model?

---

## Why Domain Driven Design?

- Here we present __domain-driven design__ (DDD)
    + one of many approaches to software design

<br>

- It consists of principles, best practices, and patterns guiding design
    + unified under a common _philosophy_
    + focus is on the _design workflow_, other than the result

<br>

- Major benefits:
    + it stresses _adherence_ to the problem at hand
    + it focuses on delivering a _business-tailored_ model
        * and, therefore, a business-tailored solution
    + it harmonises _communication_ among managers, technicians, and users
    + it stresses the production of maintainable and _extensible_ software

---

{{< slide id="notions" >}}

# Main notions of DDD

---

{{% section %}}

## What is the domain?

- Definition of __domain__:
    + a well-established _sphere of knowledge_, influence or activity
    + the _subject area_ to which the user applies the software

<br>

- Remarks:
    + focus is on how __users and experts perceive the domain__
    + focus is __not__ on how __developers__ perceive the domain

<br>

- Examples of domains and the contexts composing them
    + some __university__ (department, faculty, HR, etc.)
    + some given __company__ (manufacturing, marketing, HR, etc.)
    + __linear algebra__ (matrices, complex numbers, polynomials, etc.)
    + __machine learning__ (classification, regression, feature selection, etc.)

---

## DDD Philosophy (pt. 1)

- _Software_ will represent a _solution_ to a _problem_ in some __business domain__
    + it should be _modelled_ & _implemented_ to match that domain
        * i.e. modelling should elicit the key aspects of a domain, possibly by interacting with experts
        * i.e. design and implementation should _mirror_ the domain

<br>

- __Words do not have meaning per se__, _but rather w.r.t. a domain_
    + i.e. the _same word_ may have __different meanings__ in different domains
    + each domain comes with a particular __language__ characterising it
        * _software components_ (interfaces, classes, etc.) should be _named after_ that language
    + _interaction with experts_ is essential to identify a domain’s language

---

## DDD Philosophy (pt. 2)

- Software should _adhere_ to the _domain_, at any moment
    + architecture and implementation should favour adherence to the domain
        * in spite of their evolution / modification

<br>

- Functionalities, structure, and UX should _mirror_ the domain too
    - using the libraries should be natural for developers
    - UX should be natural for users

    as both developers and users are (supposed to be) immersed in the domain

{{% /section %}}

---

{{% section %}}

## Overview of main notions

- __Domain__: the reference area of knowledge

- __Subdomain__: a portion of the domain (problem space)

- __Bounded context__: the boundary within which a model and its language are consistent (solution space)

- __Model__: a reification of the domain in software

- __Ubiquitous Language__: language used by domain experts and mirrored by the model

---

## Overview of main notions (picture)

![Problem space: an e-commerce domain split into payment (generic), offer (core), customer (supporting), and shipping (supporting) subdomains. Solution space: each subdomain is realised, ideally one-to-one, by a bounded context, which contains its own model and its own ubiquitous language](./notions-overview.svg)

---

## _Subdomains_ and _Bounded Contexts_

> A bounded context is a _boundary_ within which a model and its language are consistent:
> - relying on a sub-set of the concepts of the domain
> - where words/names have a unique, precise meaning
> - clearly distinguishable from other contexts

- ideally 1 subdomain $\approx$ 1 bounded context, but not necessarily

---

## Kinds of subdomains

- _core_: where the business differentiates itself $\rightarrow$ invest most modelling effort here
- _supporting_: needed, specific to the business, not differentiating
- _generic_: common to many businesses (e.g. authentication, invoicing) $\rightarrow$ buy / reuse
- e.g. for a university: teaching may be core, course scheduling supporting, authentication generic

(cf. [Evans, 2003](https://www.domainlanguage.com/ddd/); [Evans, _DDD Reference_, 2015](https://www.domainlanguage.com/ddd/reference/))

---

{{< slide id="domain-subdomains-contexts" >}}

## Domain _vs._ Subdomains _vs._ Contexts

![An e-commerce domain split into payment (generic), offer (core), customer (supporting), and shipping (supporting) subdomains, each realised by one bounded context with its services, except shipping, realised by a shipping and a tracking context. Every context has its own Customer model, all linked to the same customer identity (CustomerID): as payer in payment (billing address, tax code or VAT number, payment methods), as shopper in the catalog (purchase history, preferences, segment), as account holder in the customer context (personal data, credentials, contacts), and as recipient in shipping (shipping addresses, delivery preferences) and tracking (shipments to track, notification contacts)](./domain.svg)

---

{{< slide id="same-concept-different-models" >}}

## Same concept, different models

- The same real-world _concept_ (e.g. a __customer__) shows up in several subdomains, seen from a different __angle__ each time:
    + _payment_ $\rightarrow$ the customer as __payer__: billing (fiscal) address, tax code / VAT number, payment methods
    + _offer_ $\rightarrow$ the customer as __shopper__: purchase history, preferences, segment $\Rightarrow$ predict offers and recommendations
    + _customer_ $\rightarrow$ the customer as __account holder__: personal data, credentials, contacts, which they manage themselves
    + _shipping_ $\rightarrow$ the customer as __recipient__: shipping addresses, delivery preferences, shipments to track

- Each bounded context keeps its __own model__ of the concept, with only the data and behaviour it needs
    + possibly under a different name, in its own _ubiquitous language_ (payer, shopper, recipient, ...)

- What contexts _share_ is the __identity__ (e.g. `CustomerID`), _not_ the model
    + contexts refer to each other's objects _by identifier_
    + the same holds for other concepts, e.g. a _product_: price and discounts in _offer_, VAT rate in _payment_, weight and size in _shipping_

- __Anti-pattern__: one all-encompassing `Customer` class shared by every context
    + any change for one context affects all the others, and the constraints of different contexts end up conflicting
    + how contexts integrate with each other is the subject of [context maps](#/context-map)

---

## The domain _Model_

> Set of __software__ abstractions mapping relevant _concepts_ of the domain

- e.g. Java/Kotlin/Scala projects, packages, interfaces, classes, records, methods, etc.

---

## The _Ubiquitous Language_

> - A _language_ structured around the domain model
>   + _used by all people_ involved in the domain
>   + which should be _used in the software_
>   + in such a way that their _semantics is preserved_

- underlying assumption:
    * different people call the same things differently
    * especially when they come from different contexts

- commonly reified into a __glossary__ of terms

- __used to name software components__

---

{{< slide id="glossary" >}}

## How a glossary may look like

![Excerpt of a glossary for an e-commerce domain, as a table with columns term, context, definition, and synonyms to avoid. Basket (offer): the products a customer is considering, not binding, not to be confused with order. Order (offer): a binding request to buy products at the agreed prices. Place an order (offer): turn a basket into an order. Customer appears twice, with different meanings: in payment, whoever is billed (a.k.a. payer); in shipping, whoever receives a shipment (a.k.a. recipient). Shipment (shipping): a parcel carrying part of one order to one address](./glossary.svg)

- one entry per term _and_ per context: the same word may mean different things in different contexts

---

## The glossary is reflected everywhere

![The glossary terms (Basket, Order, place, Customer, Shipment) are mirrored by the code (interfaces Basket, Order, Customer, method place, List of Shipment), by the documentation (a page explaining how a customer places a basket, which becomes an order delivered via shipments), and by conversations among a domain expert, a developer, and a manager, who all use the same terms](./glossary-reflection.svg)

- same words, same meanings, for _all_ people: experts, developers, managers, users

{{% /section %}}

---

{{% section %}}

## Conceptual Workflow

1. Identify the __domain__, give a name to it

2. Identify the __main contexts__ within the domain, giving them names
    -  possibly, by interacting with experts

3. Identify the actual meaning of __relevant words__ from the domain, and track them into a __glossary__
    - possibly, by interacting with experts

    - _without assuming you already know the meaning of words_
        + i.e. do __not__ rely on (your) __common sense__

    - keep in mind that the meaning of words may vary among contexts
        + __homonyms__: similar names, different meanings
        + __synonyms__: different names, similar meanings

4. Adhere to the language, use it, make it yours
    - especially when talking about the domain / model / software
    - design/sketch code mirroring the language

5. Draw a __context map__ (defined [later](#/context-map)) tracking
    - the main contexts and their junctions
    - words whose meaning varies across contexts

6. Model the software around the ubiquitous language
    - rule of thumb: __1 concept $\approx$ 1 interface__

---

{{< slide id="conceptual-workflow-diagram" >}}

## Conceptual Workflow (activity diagram)

{{< plantuml max-h="85vh" alt="Activity diagram of the conceptual workflow: identify and name the domain, then its main contexts; then repeatedly elicit the meaning of relevant words with domain experts and update the glossary, as long as ambiguous words remain; then adopt the ubiquitous language, draw the context map, model the software around the language, and continue with the choice of building blocks" >}}
skinparam defaultFontSize 18
skinparam ActivityBackgroundColor #e3edf8
skinparam ActivityBorderColor #2f6db5
start
:Identify and name the **domain**;
:Identify and name the main **contexts**;
repeat
  :Elicit the meaning of relevant **words**
  with **domain experts**
  (beware of homonyms and synonyms);
  :Update the **glossary**;
repeat while (ambiguous or missing words?) is (yes)
-> no;
:Adopt the **ubiquitous language**
in talks, sketches, and code;
:Draw the **context map**;
:Model the software around the language
(1 concept ≈ 1 interface);
(A)
note right: continues with\nbuilding blocks
{{< /plantuml >}}

---

## Discovering the domain: Event Storming

- Collaborative workshop with domain experts and developers (cf. [Brandolini](https://www.eventstorming.com/))
- Put _domain events_ (past tense) on a timeline, then add commands, actors, aggregates, and policies
- Boundaries between clusters of events suggest _bounded contexts_
- Domain events are introduced [later](#/domain-events)

<br>

> More on event storming in the course [Software Architecture and Platforms](https://www.unibo.it/en/study/course-units-transferable-skills-moocs/course-unit-catalogue/course-unit/2026/558673)

---

## Event Storming: notation

![Sticky-note colours used in event storming: orange for domain events (e.g. Order Placed), blue for commands (Place Order), small yellow for actors (Customer), large yellow for aggregates (Order), lilac for policies (whenever ... then ...), green for read models (Basket Summary), pink for external systems (Payment Gateway), and red, tilted, for hot spots (open questions)](./event-storming-notation.svg)

- one colour per kind of sticky note, on a long paper roll (or a virtual whiteboard)

---

## Event Storming: big picture

![Domain events of an e-commerce placed along a timeline: product added to or removed from basket, order placed, payment authorized or refused, invoice issued, shipment prepared, parcel dispatched, delivered, or returned. Hot spots ask what if stock runs out after placing, and who pays for returns. Dashed boundaries group the events into candidate offer, payment, and shipping contexts](./event-storming-big-picture.svg)

1. everybody writes the events they know, then the group sorts them in time
2. discussions reveal _hot spots_ and the _words_ people use $\rightarrow$ glossary
3. clusters of events suggest _bounded contexts_

---

## Event Storming: process modelling

![Process-level event storming: a Customer, informed by the Basket Summary read model, issues the Place Order command, handled by the Order aggregate, which emits Order Placed; a policy (whenever an order is placed, charge the customer) issues the Charge Customer command, sent to the Payment Gateway external system, which emits Payment Authorized, and so on](./event-storming-process.svg)

- zoom into one process: _actor_ $\rightarrow$ _command_ $\rightarrow$ _aggregate_ $\rightarrow$ _event_ $\rightarrow$ _policy_ $\rightarrow$ _command_ ...
- aggregates and events found here become building blocks of the model

{{% /section %}}

---

{{< slide id="building-blocks" >}}

# DDD Building Blocks

---

## Towards building blocks

![Modelling maps the domain onto the model: a domain concept (e.g. customer) becomes a type (e.g. interface Customer), and each instance of the concept (e.g. Alice, Bob) is reified as an object of that type (alice, bob)](./domain-to-model.svg)


- Each _concept_ from each **context** shall become a _type_ in the **model**
    + type $\approx$ class, interface, structure, ADT, etc.
        * depends on what the programming language has to offer

<br>

- Use _building blocks_ as __archetypes__
    + let them guide and constrain your design

---

## Workflow

(continued)

{{< multicol >}}
{{% col class="col-7" %}}

7. _Choose_ the most adequate __building block__ for each concept
    - depending on the nature of the concept
    - ... or the properties of its instances

8. The building block dictates how to __design the type__ corresponding to the concept
    - objects in OOP are shaped by types

9. The choice of building block may lead to the identification of __other concepts / models__
    - e.g. entities may need value objects as identifiers
    - e.g. entities may need repositories to be stored
    - e.g. entities may need factories to be created
    - e.g. aggregates may be composed of entities or value objects

{{% /col %}}
{{% col class="col-5" %}}

{{< plantuml max-h="75vh" alt="Continuation of the conceptual workflow activity diagram: for each concept, choose the most adequate building block and design the corresponding type; if new concepts emerge (identifiers, repositories, factories, ...), add them to the glossary and to the model; repeat while concepts are left" >}}
skinparam defaultFontSize 18
skinparam ActivityBackgroundColor #e3edf8
skinparam ActivityBorderColor #2f6db5
(A)
repeat
  :Pick a **concept**;
  :Choose its **building block**;
  :Design the corresponding **type**;
  if (new concepts emerged?) then (yes)
    :Add them to the **glossary**
    and to the model;
  else (no)
  endif
repeat while (concepts left?) is (yes)
-> no;
stop
{{< /plantuml >}}

{{% /col %}}
{{< /multicol >}}

---

{{% section %}}

## Building blocks (overview)

- __Entity__: objects with an identifier

- __Value Object__: objects without identity

- __Aggregate__: cluster of entities/value objects with a consistency boundary, accessed via its __root__ entity

- __Repository__: objects providing storage facilities

- __Service__ objects: providing stateless functionalities

- __Domain Event__: objects modelling relevant events (notifications)

- __Factory__: objects creating other objects
    + they enable _dependency injection_ (and, hence, testability)

---

## Building blocks (concept)

![A concept map linking model-driven design to services, entities, value objects, aggregates, factories, and repositories; entities act as aggregate roots and repositories access aggregates](./building-blocks.svg)

{{% /section %}}

---

{{% section %}}

## Entities vs. Value Objects

> Genus-differentia definition:
> - _genus_: both can be used to model __elementary__ concepts
> - _differentia_: entities have an explicit __identity__, value objects are __interchangeable__

<br>

### Quick modelling examples

#### Classroom

- Seats in a classroom may be modelled as value objects

- Attendees of a class may be modelled as entities

#### Seats on a plane

- Numbered seats $\rightarrow$ entities

- otherwise $\rightarrow$ value objects

---

## Entities vs. Value Objects (constraints)

### Value Objects

- Identified by their _attributes_
    + equality compares attributes alone
- Must be _immutable_: their state is their attributes, which never change
    + read-only properties
    + lack of state-changing methods
- May be implemented as
    - structures in .NET
    - _data classes_ in Kotlin, Scala, Python
    - records in Java
- _Must_ implement `equals()` and `hashCode()` on JVM
    + implementation must compare the objects' attributes

---

## Entities vs. Value Objects (constraints)

### Entities

- They have an inherent _identity_, which never changes during their lifespan
    + common modelling: __identifier attribute__, of some value type
    + equality compares identity
- Can be _stateful_ $\Rightarrow$ may have a _mutable_ design
    + modifiable properties
    + state-changing methods
- May be implemented via _classes_ in most languages
- Must implement `equals()` and `hashCode()` on JVM
    + implementation must compare (at least) the objects' identifiers

---

## Entities vs. Value Objects (example)

{{< plantuml >}}
interface Customer {
    + CustomerID getID()
    ..
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

{{% /section %}}

---

{{% section %}}

## Aggregates and Aggregate Roots

### Definition

- An _aggregate_ is a cluster of entities/value objects treated as a unit for consistency and changes

- The _aggregate root_ is the single entity in the aggregate that outside objects may reference

- It _guarantees_ the _consistency_ of the objects it contains

- It _mediates_ the usage of the composing objects from the outside
    + acting as a _façade_ ([à la GOF](https://en.wikipedia.org/wiki/Facade_pattern))

- Outside objects should _avoid_ holding _references_ to composing objects

(cf. [Evans, 2003](https://www.domainlanguage.com/ddd/); [Vernon, 2013](https://www.informit.com/store/implementing-domain-driven-design-9780321834577))

---

## Aggregate Roots (constraints)

- They are usually _compound_ entities

- They can be or exploit _collections_ to contain composing items
    + they may leverage the [composite pattern](https://en.wikipedia.org/wiki/Composite_pattern)

- May be better implemented as _classes_ in most programming languages

- _Must_ implement `equals()` and `hashCode()` on JVM (as any other entity)
    + equality compares the root's identity, as for any entity

- Components of an aggregate should _not_ hold **references** to components of _other_ aggregates
    + that's why they are called aggregate _roots_
    + notable exception: _references_ to _identifiers_ of other aggregates

    ![Three aggregates, each with an aggregate root and internal components; references from one aggregate to another aggregate's root are allowed, while a reference to another aggregate's internal component is forbidden](./aggregate-references.svg)

---

## Aggregate Root (example)

{{< plantuml height="65vh" alt="A Customer aggregate, whose root Customer owns a CustomerID, and an Order aggregate, whose root Order owns an OrderID, an Address value object, and many OrderItem entities; Order refers to the customer only through its CustomerID" >}}
package "Customer aggregate" <<Rectangle>> {
    interface Customer {
        + CustomerID getID()
        ..
        + String getName()
        + String getEmail()
    }
    note left of Customer: aggregate root

    interface CustomerID
    note left of CustomerID: value object
}

package "Order aggregate" <<Rectangle>> {
    interface Order {
        + OrderID getID()
        + CustomerID **getCustomer**()
        + Date getTimestamp()
        + Address getShippingAddress()
        + Iterable<OrderItem> getItems()
        ..
        + void **setShippingAddress**(address: Address)
        + void **addItem**(product: ProductID, amount: long)
        + void **removeItem**(product: ProductID)
        + Money getTotal()
    }
    note top of Order: aggregate root

    interface OrderID
    note bottom of OrderID: value object

    interface Address {
        + String getStreet()
        + String getCity()
    }
    note bottom of Address: value object

    interface OrderItem {
        + ProductID getProduct()
        + long getAmount()
        + Money getPrice()
    }
    note bottom of OrderItem: entity
}

Customer "1" *-d- "1" CustomerID
Order "1" *-d- "1" OrderID
Order "1" *-d- "1" Address
Order "1" *-d- "N" OrderItem
Order .l.> CustomerID: refers to\nthe customer\nby ID
{{< /plantuml >}}

(notice that `Order` refers to its `Customer` via the `CustomerID` only, and that `OrderItem`s refer to products via `ProductID`s)

{{% /section %}}

---

{{% section %}}

## Repositories (definition)

> Objects mediating the _persistent_ __storage/retrieval__ of other objects

![A client gets an aggregate from a repository, modifies it, and saves it back; the repository rebuilds (hydrates) the aggregate from data loaded from the database, and stores it there, dealing with whole aggregates only](./repositories.svg)

---

## Repositories (details)

### Purpose

- Hiding (i.e. be backed by) some _database technology_
- Possibly realising some sort of [object-relational mapping (ORM)](https://en.wikipedia.org/wiki/Object%E2%80%93relational_mapping)
- _Storing_ / _retrieving_ aggregate roots as _wholes_
- Supporting _CRUD_ operations on aggregate roots, and/or _wrapping common queries_
    - Create, Read, Update, Delete

### Remarks

- They may exploit _factories_ for turning retrieved data into objects
- If properly engineered, they avoid the _lock-in_ effect for database technologies
- Design & implementation may require thinking about:
    - the architecture,
    - the infrastructure,
    - the expected load,
    - etc.

---

## Repositories (constraints)

- They are usually _identity-less_, _stateful_, and _composed_ objects
    + state may consist of the _stored objects_
    + state may consist of _DB connections_

- May be implemented as _classes_ in most OOP languages

- Provide methods to
    + _add_, _remove_, _update_ aggregate root entities
    + _select_ and return one or more entities, possibly in a _lazy_ way
        * this should return `Iterable`, `Collection`, or `Stream` on JVM

- _Non-trivial_ implementations should take care of
    + enforcing _consistency_, in spite of _concurrent_ access
    + supporting complex _transactions_

---

## Repositories (example)

{{< plantuml height="70vh" >}}
interface CustomerID

interface Customer

Customer "1" *-u- "1" CustomerID

interface CustomerRegistry {
    + Iterable<Customer> getAllCustomers()
    ..
    + Customer findCustomerById(CustomerID id)
    + Iterable<Customer> findCustomerByName(String name)
    + Iterable<Customer> findCustomerByEmail(String email)
    ..
    + void addNewCustomer(Customer customer)
    + void updateCustomer(Customer customer)
    + void removeCustomer(Customer customer)
}

CustomerRegistry "1" o--> "N" Customer: contains
CustomerRegistry --> CustomerID: exploits
{{< /plantuml >}}

{{% /section %}}

---

{{% section %}}

## Services

> Operations that do not naturally belong to any entity or value object

### Purpose

- Reifying _control-related_ aspects of the software
- _Wiring_ aggregates, entities, and value objects _together_
- Exposing _coarse-grained functionalities_ to the users
- Providing a _façade_ for the domain
- Making the business logic evolvable, interchangeable, replaceable

- _domain service_: domain logic spanning several aggregates; lives in the domain layer
- _application service_: orchestrates a use case (loads aggregates via repositories, calls them, saves, publishes events); lives in the application layer
- Rule of thumb: put behaviour on entities/aggregates first; a model of only getters/setters plus services is an _anemic domain model_ (cf. [Fowler](https://martinfowler.com/bliki/AnemicDomainModel.html))

### Remarks

- Services may be _exposed_ via RESTful APIs
- Should be designed taking _current use cases_ into account (i.e. design services to be _purpose-specific_)
    + entities/objects should support _future use cases_, too (i.e. design entities/objects to be _general purpose_)

---

## Services (constraints)

- They are usually _identity-less_, _stateless_ objects

- May be implemented as classes in OOP languages
    + or bare _functions_ in functional languages

- Commonly provide _procedures_ to do business-related stuff
    + e.g. a __complex operation__...
        * ... concerning some particular _aggregate root_
        * ... which does <u>not</u> support the operation directly through its methods
            - (maybe because the operation is use-case specific)
    + e.g. __proxying__ an external service
    + e.g. a complex operation spanning __several aggregates__, repositories, factories, etc.

- _Non-trivial_ implementations should take care of
    + supporting _concurrent access_ to the service’s facilities
    + exposing _domain events_ to the external world

---

## Services and events

- Services are most commonly __prosumers__ of _events_
    + i.e. they both _produce_ and _consume_ events
    + events are most commonly _domain events_ (cf. [later](#/domain-events))

- In practice, services commonly implement the [observer pattern](https://en.wikipedia.org/wiki/Observer_pattern) (aka _event listener_)
    + often in _both_ roles: event __source__ (subject) _and_ event __listener__ (observer)

- Hence, services may come with _ad-hoc methods_ to
    + _produce_ events, i.e. let listeners (un)subscribe
        * e.g. `addOrderEventListener(listener)`, `removeOrderEventListener(listener)`
    + _react_ to events, i.e. handle notifications from other sources
        * e.g. `onCustomerEvent(event)`

---

## Services (example)

{{< plantuml height="70vh" >}}
interface OrderManagementService {
    + void performOrder(Order order)
    ..
    + void **addOrderEventListener**(OrderEventListener listener)
    + void **removeOrderEventListener**(OrderEventListener listener)
}

interface OrderEventListener {
    + void **onOrderEvent**(OrderEvent event)
}

interface CustomerEventListener {
    + void **onCustomerEvent**(CustomerEvent event)
}

interface Order {
    + OrderID getId()
    + CustomerID getCustomer()
    + void setCustomer(CustomerID customer)
    + Date getTimestamp()
    + void setTimestamp(Date timestamp)
    + Map<ProductID, Long> getAmounts()
}

interface OrderID

interface CustomerID

interface ProductID

interface OrderStore

Order "1" *-r- "1" OrderID
Order "1" *-d- "1" CustomerID
Order "1" *-u- "N" ProductID
OrderStore "1" *-- "N" Order

OrderManagementService ..> Order: handles
OrderManagementService ..> OrderStore: updates
OrderManagementService -u-|> CustomerEventListener
OrderManagementService "1" o-l- "N" OrderEventListener: notifies

note bottom of OrderStore: repository
note top of Order: entity (aggregate root)
note right of OrderID: value object
note right of ProductID: value object
note right of CustomerID: value object
note right of OrderManagementService: service\n(source of order events,\nlistener of customer events)
note left of OrderEventListener: listener (observer)
note left of CustomerEventListener: listener (observer)

OrderID -u[hidden]- ProductID
OrderID -d[hidden]- CustomerID

{{< /plantuml >}}

{{% /section %}}

---

{{% section %}}

{{< slide id="domain-events" >}}

## Domain Events (definition)

> A value-like object capturing some domain-related _event_
> <br> (i.e., an observable _variation_ in the domain, which is _relevant_ to the software)

- actually, only the event _notification_/description is reified to a type

![A customer places an order via the Order Management Service, which saves the Order aggregate and then publishes an OrderEvent of kind "placed"; the event notifies a Recommendation Service, which updates the Recommendations aggregate](./domain-events.svg)

---

## Domain Events (details)

### Purpose
- _Propagate_ changes among portions of the domain model (e.g. contexts, aggregates, entities, etc.)
- _Record_ changes concerning the domain

### Remarks

- Strong relation with the [observer pattern](https://en.wikipedia.org/wiki/Observer_pattern) (i.e. publish-subscribe)

- Strong relation with the _event sourcing_ approach (described later)

- Strong relation with the _CQRS pattern_ (described later)

---

## Domain Events (constraints)

- They are usually _time-stamped_ value objects

- May be implemented as _data-classes_ or _records_

- They represent a _relevant variation_ in the domain
    + e.g. a _change_ in the _state_ of some entity / repository

- Event sources & listeners shall be identified too
    + who is _generating_ the event?
    + who is _consuming_ the event?

- _Infrastructural components_ may be devoted to _propagate_ events across contexts
    + e.g. a message broker, a message queue, etc.

- Common convention: one type per event, named in the _past tense_ (e.g. `OrderPlaced`, `OrderCancelled`) (cf. [Vernon, 2013](https://www.informit.com/store/implementing-domain-driven-design-9780321834577))
- \[Teacher's Suggestion\]: use a neutral type (e.g. `OrderEvent`) plus a kind/discriminator field when many events share the same payload

---

## Domain Events (example)

{{< plantuml >}}
interface OrderManagementService {
    + void performOrder(Order order)
    ..
    + void **addOrderEventListener**(OrderEventListener listener)
    + void **removeOrderEventListener**(OrderEventListener listener)
}

interface OrderEventListener {
    + void **onOrderEvent**(OrderEvent event)
}

interface RecommendationService

interface OrderEvent {
    + String **getKind**()
    + OrderID getOrder()
    + CustomerID getCustomer()
    + Date **getTimestamp**()
    + Map<ProductID, Long> getAmounts()
}

interface OrderID

interface CustomerID

interface ProductID

OrderEvent "1" *-r- "1" OrderID
OrderEvent "1" *-d- "1" CustomerID
OrderEvent "1" *-d- "N" ProductID
OrderID -[hidden]d- ProductID

OrderManagementService .r.> OrderEvent: publishes
OrderEventListener <|-d- RecommendationService
OrderManagementService "1" o-d- "N" OrderEventListener: notifies
OrderEventListener .r.> OrderEvent: consumes

note top of OrderEvent
    domain event
    (kind: "placed",
    "cancelled", ...)
end note
note right of OrderID: value object
note right of ProductID: value object
note left of CustomerID: value object
note top of OrderManagementService: service
note right of RecommendationService: service
note left of OrderEventListener: listener (observer)
{{< /plantuml >}}

{{% /section %}}

---

{{% section %}}

## Factories (definition)

> Objects aimed at __creating__ other objects

<br>

![A client delegates entity creation to a factory, which hides the complexity of creating the entity](./factories.svg)

---

## Factories (details)

### Purpose

+ Factories encapsulate the _creation logic_ for _complex objects_
    * making it evolvable, interchangeable, replaceable

+ They ease the enforcement of _invariants_

+ They support _dynamic selection_ of the most adequate _implementation_
    + while _hiding_ the actual implementation choice

### Remarks

- DDD's notion of factory is quite loose
    + DDD's Factories $\supset$ GOF's Factories $\cup$ Builders $\cup$ ...

---

## Factories (constraints)

- They are usually _identity-less_ and _state-less_ objects
    + recall the [abstract factory pattern](https://en.wikipedia.org/wiki/Abstract_factory_pattern)

- May be implemented as _classes_ in most OOP languages

- Provide methods to _instantiate_ entities or value objects

- Usually they require _no mutable_ field/property

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
    + TaxCode computeTaxCode(String name, String surname, Date birthDate, String birthPlace)
    ..
    + Customer newCustomerPerson(TaxCode code, String fullName, String email)
    + Customer newCustomerPerson(String name, String surname, Date birthDate, String birthPlace, String email)
    ..
    + Customer newCustomerCompany(VatNumber code, String fullName, String email)
}
note bottom of CustomerFactory
- method for computing tax codes
- methods for creating person customers
- methods for creating company customers
end note

CustomerFactory -r-> TaxCode: creates
CustomerFactory -u-> Customer: creates
{{< /plantuml >}}

{{% /section %}}

---

{{% section %}}

{{< slide id="dependency-injection" >}}

## Factories enable Dependency Injection

- Services depend on _repositories_ and _factories_ (and possibly on other services)
    + e.g. `OrderManagementService` needs an `OrderStore` to save orders
- __Who creates__ these dependencies?
    + if the service calls `new SqlOrderStore()` itself, it is _bound_ to that implementation
    + ... and it can hardly be tested without a real database

![Left: without DI, the service itself creates a SqlOrderStore, so the implementation choice is hard-coded in the client. Right: with DI, a factory first creates the SqlOrderStore and then injects it into the service, which only uses it through the OrderStore interface](./di-motivation.svg)

- Factories already _hide_ the implementation choice
    + __Dependency Injection__ (DI) goes one step further: the client does not create its dependencies, it _receives_ them

---

## Dependency inversion

> Goal: _minimise_ dependencies among types, to make implementations _interchangeable_

- Let _interfaces_ depend on _interfaces_ only
- Let _classes_ depend on _interfaces_, never on other _classes_
    + built-in types (e.g. `String`, `LocalDate`) are tolerated exceptions
- _Delay_ the choice of the actual implementation of each interface as much as possible
- Do not _leak_ implementation types into public APIs

![Before: OrderManagementServiceImpl depends directly on the SqlOrderStore class. After: OrderManagementServiceImpl depends on the OrderStore interface, which is implemented by both SqlOrderStore and InMemoryOrderStore, so the arrows towards the implementation are inverted](./dependency-inversion.svg)

---

## Dependency Injection (concept)

> Interfaces are separated from classes; the __wiring__ between them is _delayed_ and performed _outside_ the classes using them

1. __Design & segregate interfaces__
    - interface members only expose interface (or built-in) types: natural after DDD
    - interfaces and implementations in _separate_ modules (e.g. `:domain` vs. `:storage`)
2. __Implement classes relying on interfaces only__
    - fields, parameters, return types: interfaces (private members too)
    - expose a mechanism to _inject_ the actual implementations:
        + _constructor injection_: dependencies are constructor parameters
            * the object is always complete and consistent $\rightarrow$ __preferred__
        + _setter injection_: one setter per dependency
            * more flexible, yet objects may be left in _inconsistent_ states
3. __Wire interfaces and classes__ at runtime
    - choosing the most adequate implementation for the situation at hand (production, test, etc.)

---

## Inversion of Control

- Without DI, an object _controls_ the creation of its own dependencies
- With DI, this control is _inverted_: some external entity creates the dependencies and passes them in
    + the _composition root_: the (only) place where implementations are chosen and wired
        * e.g. the `main` method, or an application-level factory
    + or a _DI container_ (a.k.a. _IoC container_), i.e. a library doing the wiring for us
- _Hollywood principle_: "don't call us, we'll call you"
- Dependency Injection is a particular form of __Inversion of Control__ (IoC)
    + cf. [Fowler, _Inversion of Control Containers and the Dependency Injection pattern_, 2004](https://martinfowler.com/articles/injection.html)

### Remark

- A factory _alone_ just moves the `new` somewhere else: the client now depends on the factory
- It becomes DI when the factory is _the composition root_, i.e. it creates the objects __and__ injects them into each other

---

## DI in Java (example, pt. 1): implementation

```java
public interface OrderStore { void add(Order order); /* ... */ }

class OrderManagementServiceImpl implements OrderManagementService {
    private final OrderStore store;          // interface
    private final CustomerFactory customers; // interface

    // constructor injection: dependencies are received, not created
    OrderManagementServiceImpl(OrderStore store, CustomerFactory customers) {
        this.store = Objects.requireNonNull(store);
        this.customers = Objects.requireNonNull(customers);
    }

    @Override
    public void performOrder(Order order) {
        // ... validate the order, possibly via customers ...
        store.add(order);
        // ... then notify order event listeners ...
    }

    // ... (un)subscription methods for order event listeners ...
}
```

- The service only mentions _interfaces_: `OrderStore`, `CustomerFactory`, `Order`
- No `new` for any dependency $\rightarrow$ the implementation choice is somebody else's business

---

## DI in Java (example, pt. 2): the composition root

```java
public class ApplicationFactory { // the composition root
    public OrderStore orderStore() {
        return new SqlOrderStore(System.getenv("DB_URL"));
    }
    public CustomerFactory customerFactory() {
        return new CustomerFactoryImpl();
    }
    public OrderManagementService orderManagementService() {
        return new OrderManagementServiceImpl(orderStore(), customerFactory());
    }
}

// in main():
OrderManagementService service = new ApplicationFactory().orderManagementService();
```

- The factory is the _only_ class mentioning _implementation_ classes
    + which can stay package-private (not leaked into the API)
- __A hand-written factory is a minimal DI container__
    + one method per interface, specifying _which_ implementation and _how_ to initialise it
- In a layered architecture, this is the job of the outermost module (cf. [modules](#/enforcing-architecture))

---

## DI eases testability

- The class under test does not create its dependencies $\rightarrow$ the _test_ decides what to pass in
    + no need to edit production code, nor to patch it with static mocking tricks

{{< multicol >}}
{{% col %}}
__Unit tests__: inject _test doubles_ (stubs, fakes, mocks)

```java
@Test
void performOrderStoresTheOrder() {
    OrderStore store = mock(OrderStore.class); // Mockito
    var service = new OrderManagementServiceImpl(
        store, new CustomerFactoryImpl());
    service.performOrder(order);
    verify(store).add(order);
}
```

- tests the service in _isolation_, no DB needed
{{% /col %}}
{{% col %}}
__Integration tests__: inject _real_ (yet lightweight) components

```java
@Test
void orderIsActuallyStored() {
    var store = new InMemoryOrderStore();
    var service = new OrderManagementServiceImpl(
        store, new CustomerFactoryImpl());
    service.performOrder(order);
    assertTrue(store.contains(order));
}
```

- or a `SqlOrderStore` on a throw-away DB (e.g. via [Testcontainers](https://testcontainers.com/))
{{% /col %}}
{{< /multicol >}}

- Each test acts as a _test-specific composition root_

---

## DI eases testability (overview)

![The same OrderManagementServiceImpl code receives an OrderStore via its constructor in three different set-ups: in production it is wired by ApplicationFactory with a SqlOrderStore; in unit tests it is wired by the test with a Mockito mock; in integration tests it is wired by the test with an InMemoryOrderStore or a SqlOrderStore on a test database run with Testcontainers](./di-testing.svg)

- Only the _wiring_ changes across production, unit tests, and integration tests
    + the production code is tested _as is_
- Dependencies that are hard to control in tests (DB, network, clock, randomness) should be _injected_ as well
    + e.g. inject a `java.time.Clock` instead of calling `LocalDateTime.now()`

---

## DI technologies

- Hand-written factories become tedious as the dependency graph grows
- _DI containers_ automate the wiring. They let developers:
    1. _register_ the chosen implementation for each interface (a.k.a. _binding_)
    2. specify its _lifetime_: _singleton_ (one shared instance), _transient_ (new instance per request), _scoped_ (one per scope, e.g. per HTTP request)
    3. _retrieve_ (or have injected) an object for a given interface

<div style="font-size: 0.7em">

| Technology | Language | Notes | Docs |
|---|---|---|---|
| Jakarta Dependency Injection (ex JSR-330) | Java | standard annotations (`@Inject`, `@Singleton`), no container | [spec](https://jakarta.ee/specifications/dependency-injection/) |
| Jakarta CDI | Java | standard container of Jakarta EE (e.g. Quarkus) | [spec](https://jakarta.ee/specifications/cdi/) |
| Spring Framework | Java, Kotlin | the most widespread container, runtime reflection | [docs](https://docs.spring.io/spring-framework/reference/core/beans.html) |
| Google Guice | Java | bindings declared in _modules_, runtime | [docs](https://github.com/google/guice/wiki) |
| Dagger 2 / Hilt | Java, Kotlin (Android) | wiring code generated at _compile time_ | [docs](https://dagger.dev/) |
| Koin | Kotlin (multiplatform) | Kotlin DSL, no reflection | [docs](https://insert-koin.io/) |
| `Microsoft.Extensions.DependencyInjection` | C# | `IServiceCollection` + `IServiceProvider` | [docs](https://learn.microsoft.com/en-us/dotnet/core/extensions/dependency-injection) |

</div>

- Containers support _overriding_ bindings in tests too
    + e.g. Spring's `@MockitoBean`, Guice's `Modules.override(...)`

{{% /section %}}

---

{{< slide id="strategic-design" >}}

# Strategic Design

---

## Towards DDD patterns

### Further notions involving __contexts__

- __Bounded Context__: enforce a model’s _boundaries_ & make them explicit

- __Context Map__: providing a _global view_ on the domain and its contexts

---

{{< slide id="context-map" >}}

## Actual definitions

### Context Boundary

> The _boundary_ of a context and its software model should be __explicit__.
> This is helpful from several perspectives:
> - technical (e.g., __dependencies__ among classes/interfaces)
> - physical (e.g., common database, __common facilities__)
> - organizational (e.g. __people/teams__ maintaining/using the code)

<br>

### Context Map

> A __map__ of _all_ the __contexts__ in a domain and their __boundaries__
> - and their __points of contact__
>   + e.g. their dependencies, homonyms, false friends, etc.
> - providing the whole picture of the domain

---

## Example of bounded context map

![A sales context containing Opportunity, Pipeline, Territory, and Sales Person, connected by Customer and Product to a support context containing Ticket, Defect, and Product Version](./bounded-contexts.svg)

(source: [Fowler](https://martinfowler.com/bliki/BoundedContext.html))

---

## Bounded Contexts & Context Maps (best practices)

- Clearly _identify & represent_ **boundaries** among contexts

- *Avoid* __responsibility diffusion__ over a single context
    + one responsible person / team for each context

- *Avoid changes* in the model for __problems__ arising __outside__ the context
    + rather, extend the domain by creating new contexts

- Enforce context’s __cohesion__ via automated unit and integration __testing__
    + to be (re)executed as frequently as possible

---

## Model integrity problem

### How to preserve the integrity of the model?

- As the __domain evolves__, the software _model should evolve_ with it
    + in order to maintain the coupling

- Yet, the domain rarely changes as a whole
    + more commonly, it changes in a __context-specific__ way

- Contexts are bounded, but __not isolated__
    + so are models, which may _depend_ on each other

- Changes to a context and its model may __propagate__ to other contexts / models

> Domain / model __changes are critical__ and should be done carefully

---

## Model integrity patterns

- __Shared kernel__: sharing a common model among contexts
- __Customer--supplier__: the downstream (customer) team requests changes in the supplier model
- __Conformist__: one model's team reacts to changes of some model they depend on
- __Anti-corruption layer__: a model's team isolates itself from another model

### Purposes

- _Preserve_ the integrity of the model w.r.t. the domain

- _Minimise_ the potential _impact_ / _reach_ of changes
    + each context should be as independent as possible
    + each change affects as few contexts as possible

---

## Model integrity patterns (background, pt. 1)

![Three contexts, A, B, and Legacy, each enclosing one or more models; lines between models of different contexts mark contact points between contexts](./context-map.svg)

- Context maps highlight relations among contexts
    + yet, _not_ __all relations are equal__, nor symmetric

---

## Model integrity patterns (background, pt. 2)

![A downstream Search Page Context and an upstream Search API Context connected by a line, marked D and U at the two ends](./provider-consumer.svg)

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

*trust $\approx$ willingness to collaborate + seek stability

---

## Shared Kernel

![Catalog and Inventory contexts overlap around a shared supplier model used by both contexts](./shared-kernel.svg)

- Best when: multiple contexts _share_ the __same team__ / organization / product

- Key idea: __factorise__ common portions of the model into a shared kernel

- Upstream and downstream __collaborate__ in designing / developing / maintaining the model
    + they are _peers_

- Keeping the __kernel__ as __small__ as possible is fundamental

---

## Customer--Supplier

![A downstream Search Page Context and upstream Search API Context connected by a customer-supplier relationship](./customer-supplier.svg)

- Best when:
    + multiple teams
    + __mutual trust__
    + good communication

- Key idea:
    + __upstream__ acts as _supplier_, __downstream__ acts as _customer_
    + both sides collaborate to __maximise integration__ among their models
        * and interoperability among their software

- Customers may _ask_ for features, suppliers will do their best to __provide__ them

- Suppliers shall __warn__ before changing their model

---

## Conformist

![A Search Page Context downstream of a Search API Context, while an External Context conforms downstream to the Search API Context](./conformist.svg)

- Best when:
    + multiple teams
    + poor communication / different pace
    + some trust

- Key idea: downstream must conform to the upstream, reactively
    + adapting their model accordingly
    + whenever the upstream's one changes

---

## Anti-corruption layer

![A Search Context isolates its model from an upstream Catalog Context through an anti-corruption translation layer with no business logic](./anti-corruption-layer.svg)

- Best when:
    + multiple teams
    + poor communication
    + poor trust

- If upstream cannot be trusted, and interaction is pointless...
    + e.g. legacy code, poorly maintained library, etc.

- ... downstream must defend itself against unexpected / unanticipated change

- The upstream's model is then reverse engineered & __adapted__
    + e.g. often, repository types are anti-corruption layers for DB technologies

---

{{< slide id="architecture" >}}

# Layered (onion) Architecture

---

## Layered Architecture (disclaimer)

- DDD does not enforce a particular architecture

- Any is fine as long as the model keeps its integrity

- Layered architectures are well suited to preserve models' integrity

- The same dependency rule (outer depends on inner, never the converse) underlies _hexagonal_ (ports & adapters, cf. [Cockburn, 2005](https://alistair.cockburn.us/hexagonal-architecture/)), _onion_, and _clean_ architectures
    + in hexagonal terms, domain/application define _ports_ (e.g. repository interfaces); outer layers provide _adapters_ (e.g. DB-backed repositories, REST controllers)

---

## Layered (onion) architecture (concept)

![Concentric layers: Domain at the centre, Application and Presentation around it, and Messages, REST API, and Storage as outer adapters](./layered-architecture.svg)

- outer layers depend on inner ones
    + the converse is not true

---

## Layered (onion) architecture (explanation)

1. __Domain layer__: contains the domain model (entities, values, events, aggregates, etc.)
    - must support a wide range of applications
    - has no dependency on any other layer

2. __Application layer__: contains services providing business logic
    - supports a particular use case via services
    - depends on the domain layer

3. __Presentation layer__: provides conversion facilities to/from representation formats
    - e.g. JSON, BSON, XML, YAML, XDR, Avro, HTML, etc.
        + depends on the domain layer (and, possibly, on the application layer)

4. __Storage layer__: supports persistent storage/retrieval of domain data
    - this is where repositories are implemented
    - may involve some DB technology
    - depends on the domain layer (and, possibly, on the application layer)

5. __Interface layers__ (e.g. ReST API, MOM, View): let external entities access the software
    - via a GUI, or via some remote interface such as HTTP

---

{{< slide id="enforcing-architecture" >}}

## Enforcing the architecture in the code

- Layering may be enforced in the code

- By mapping layers into modules
    + module $\approx$ packaging unit
        * e.g. Gradle sub-projects, Maven modules, .NET assemblies, etc.
    + each module having its own build dependencies

{{< plantuml height="50vh">}}
top to bottom direction

component ":domain" as domain
component ":application" as application
component ":presentation" as presentation
component "third-party serialization library" as gson
component "third-party DB client library" as db
component ":storage" as storage
component ":web-api" as server
component ":message-queue" as mq
component ":command-line" as cli
component product

application ..> domain
presentation ..> application
server ..> presentation
mq ..> presentation
cli ..> presentation
storage ..> application
presentation ..> gson
storage ..> db
product ..> server
product ..> storage
product ..> mq
{{< /plantuml >}}

- `product` is the _composition root_: it wires implementations from all modules (cf. [Dependency Injection](#/dependency-injection))

---

{{< slide id="advanced" >}}

# Advanced aspects of DDD

---

## Event sourcing (preliminaries)

- Whenever there is a _mutable_ entity ...
- ... whose state evolution over time must be tracked ...
- ... state transitions can be memorised in 2 ways:
    + one may track the __current state__
    + or the __flow__ of __variations__

![Two timelines for a counter: state snapshots record values 0, 1, 2, while a flow records initial value 0 followed by increment events that reconstruct those values](./state-vs-flow.svg)

---

## Event sourcing

> A pattern where _domain events_ are reified into _time-stamped data_ and the whole _evolution_ of a system is persistently _stored_

- perfect match with DDD as domain events are first-class citizens

(cf. [Fowler, Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html))

---

## Event sourcing (pros & cons)

### Benefits

- Historical data can be analysed, to serve several purposes
    + e.g. predictive maintenance, optimisation, analysing & anticipating faults

- Past situations can be replayed
    + it improves debugging and enables measurements

- Enables complex event detection & reaction

- Enables CQRS (described later)

### Limitations

- A lot of data is generated and must be stored, which costs _space_
- Reconstructing the (current) state costs _time_

---

## Command--Query Responsibility Segregation (__CQRS__)

- Advanced pattern for building _highly-scalable_ applications

- It is _often combined_ with event sourcing, but does not require it (cf. [Fowler, CQRS](https://martinfowler.com/bliki/CQRS.html))

- It can deliver **reactive**, **eventually consistent** solutions where:
    + context boundaries can be easily enforced
    + single responsibility principle is applied extensively

---

## CQRS definition

> __Split__ the _domain_ and _application_ layers to _segregate_ __read/write__ responsibilities

(cf. [Fowler, CQRS](https://martinfowler.com/bliki/CQRS.html); [Fowler, Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html))

- __Read__ model (a.k.a. _view_ or _query_ model)
    + accepts queries aimed at __observing__ the state of the system

- __Write__ model (a.k.a. _command_ model)
    + accepts commands aimed at __altering__ the state of the system

---

## CQRS concept

![A UI sends commands to the command model, which validates them, applies domain logic, and writes to the database; the query model reads from the database and serves query results to the UI](./cqrs.svg)

(source: [Fowler](https://martinfowler.com/bliki/CQRS.html))

---

## CQRS workflow (writing)

Whenever users want to _perform an action_ in the system:
1. they create a __command__ and forward it to the __write model__
  - i.e. an object describing a _variation_ to be applied to some domain aspect

<br>

2. the command is validated by the __write model__; if accepted, it produces one or more _domain events_
  - with event sourcing, the _events_ (not commands) are appended to an _event store_


---

## CQRS workflow (reading)

Whenever users want to _inspect/observe the system_ at time $t$:
1. they perform a __query__ on the __read model__
    - asking for the state of the system _at time $t$_
    - e.g. $t$ $\equiv$ now

<br>

2. the read model is a _projection_ built from the events up to time $t$
    - it returns a read-optimised representation of the system state at time $t$

---

## CQRS -- When is the read model updated?

> A _projection_ computes a read model by applying the events recorded up to time $t$

- If read and write data are stored on different databases
    + projecting events updates the query database
    + the query database should be __read-efficient__
    + the event store should be __write-efficient__

- Several, non-mutually-exclusive strategies:
    + __eager__: events are projected as soon as they are received
    + __on read__: events are projected when a query is read
    + __scheduled__: events are projected in the background, periodically

---

{{< slide id="exercises" >}}

# Exercises

Repository: <{{% github-url repo="ddd-exercise" %}}>, branch `exercise`

(solutions on branch `master`)

---

{{% section %}}

## Exercise 1 -- Simple Store (pt. 1)

A simple domain keeping track of: _customers_, _products_, and _orders_.

### Customers

- Customers can either be companies or persons

- Companies are identified by a VAT number

- Persons are identified by tax codes

- In any case a name and email are recorded for each customer

- Both the name and email may vary over time

### Products

- Products are identified by name and type/category

- They have a nominal price, expressed using some currency

- The availability of each product is tracked as well

- Both the price and the available quantity may vary over time

---

## Exercise 1 -- Simple Store (pt. 2)

### Money and exchange rates

- Money is represented by a decimal value and a currency

- Currencies are represented by a name, a symbol and an acronym
    - e.g. Euro, EUR, €
    - e.g. US Dollar, USD, $

- Exchange rates keep track of the conversion rate
    + from a source currency
    + to a destination currency
    + at a particular moment

- Information about exchange rates can be obtained from the Internet

- We can compute the price of each product, in any currency, at any moment

---

## Exercise 1 -- Simple Store (pt. 3)

### Orders

- Orders are identified by a serial number

- They keep track of the products ordered by a customer
    * and the number of copies ordered for each product

- Also, orders keep track of when they have been performed

- All such information may be modified before the order is delivered

- When a new order is registered, many actions should be performed in reaction

- It must be possible to compute the actual total price of an order
    * at a particular moment, using a particular currency

---

## Exercise 1 -- Simple Store (pt. 4)

### TO-DO

1. __Read__ informal domain description
2. __Identify__ the main __domain concepts__ composing the ubiquitous language
3. __Model__ the domain as __Java types__ (classes or _interfaces_)
    - the model should include entities, value objects, repositories, factories, and services
4. __Structure__ the Java types according to some module structure compliant with __hexagonal architecture__
    - i.e. put code into either the `domain` or `application` modules
5. Sketch _tests_ and, then, implementation for at least one
    - entity
    - value object
    - factory
    - repository
    - service
6. __Wire__ services, repositories, and factories via _constructor injection_
    - write at least one _integration test_ injecting an in-memory repository

{{% /section %}}

---

{{% section %}}

## Exercise 2 -- Trivial CQRS (pt. 1)

- Very simple repository type: the `Counter`
- It contains 1 `long` number, initially set to `0`
- The value may be read or changed arbitrarily
- Whenever the value changes, a new domain event is published, of type `Variation`
- However, the repository only memorises the _current_ value of the counter

---

## Exercise 2 -- Trivial CQRS (pt. 2)

### TO-DO

1. Switch the design towards __event sourcing__, by memorising variations instead of snapshots.
2. Implement the __CQRS pattern__ by splitting the repository into 2 parts:
  - a __write-model__ for storing variations
  - a __read-model__ for retrieving a snapshot of the counter's value at a given moment

<br>

In practice:
1. provide __implementations__ for the `CounterReader` and `CounterWriter` interfaces
2. optionally, _test_ those implementations

{{% /section %}}

---

{{% section %}}

## Exercise 3 -- Anti-corruption layer (pt. 1)

- Very simple domain: `Table`s
    + i.e. 2-D containers of `Row`s...
    + ... where each row contains one or more `String` values

- Functionalities for __CSV__ _import/export_ are missing and need to be implemented via _third-party libraries_
    + [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
        * Guide here: <https://www.baeldung.com/apache-commons-csv>
    + [OpenCSV](https://opencsv.sourceforge.net/)
        * Guide here: <https://www.baeldung.com/opencsv>

---

## Exercise 3 -- Anti-corruption layer (pt. 2)

### TO-DO

1. Extend the model of our domain with new __interfaces__ supporting __CSV parsing / writing functionalities__
2. Design the interfaces so that they are __agnostic__ of the third-party libraries
    - without corrupting the domain model with library-specific types
3. Provide __implementations__ for the interfaces, using __one of the two__ libraries
4. Sketch _tests_ for the implementations
5. Provide __implementations__ for the interfaces, using __the other__ library
6. Use the same tests as above to prove the two implementations work _the same way_

{{% /section %}}
