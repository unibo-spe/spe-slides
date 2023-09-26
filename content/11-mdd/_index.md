+++

title = "Model Driven Development"
description = "Practical introduction to Model Driven Development
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

# Model Driven Development

---

## Lecture goals

- Understand __metamodelling__
- Understand __domain specific languages__
- Practice with __model driven development__ in _Xtext_

---

## Metamodelling

0. (Abstract) __Language__ $\approx$ (abstract) _syntax_ + _semantics_ 

1. __Model__ $\approx$ the abstract language by which we describe the possible entities involved in a (class of) system(s)
    + the model abstracts a number of similar systems
    + a system is an _instance_ of the model it has been designed from
    + a model is a _template_ for a system

2. In which language is the model expressed?
    + __Meta-model__ $\approx$ the abstract language by which we describe __models__
    + for instance UML is the meta-model behind object-oriented programming
