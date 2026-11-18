+++

title = "Agentic DevOps"
description = "Agents as contributors: context, skills, tools, guardrails, and automation"
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

# Agentic DevOps

<!-- write-here "reusable/header.md" -->

<!-- end-write -->

---

## Outline

1. From bots to agents
2. Context engineering: `AGENTS.md`
3. Skills
4. Tools and the Model Context Protocol
5. Hooks, permissions, and sandboxes
6. Plugins: packaging agent configuration
7. Agents in CI/CD
8. Security
9. The pipeline is the guardrail

---

<!-- write-here "shared-slides/agentic/intro.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/context.md" -->

<!-- end-write -->

---

## A real `AGENTS.md`: these slides

<div style="font-size: 0.55em">

```markdown
<!-- write-here-code "AGENTS.md" -->
<!-- end-write -->
```

</div>

The agent *cannot guess* that `_index.md` files are generated:
<br>
it would happily edit them, and the changes would be lost at the next build

---

<!-- write-here "shared-slides/agentic/skills.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/tools.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/guardrails.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/plugins.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/ci.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/security.md" -->

<!-- end-write -->

---

<!-- write-here "shared-slides/agentic/pipeline.md" -->

<!-- end-write -->

---

# Agentic DevOps

<!-- write-here "reusable/header.md" -->

<!-- end-write -->
