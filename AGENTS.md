# AGENTS quick guide

## Repository purpose
- Course slides for **Software Process Engineering**, built with **Hugo** + **reveal-hugo**.
- Main site content lives under `content/`; generated slide pages are written to `build/`.

## Important paths
- `config.toml`: Hugo configuration (theme, reveal settings, publish dir).
- `content/`: slide decks (`_index.md` per module).
- `shared-slides/`: reusable markdown snippets + tooling.
- `shared-slides/preprocess.rb`: expands `*generator.md` files into `_index.md`.
- `shared-slides/serve.sh`: local dev loop (preprocess + `hugo server` + file watching).
- `themes/reveal-hugo/`: vendored theme and reveal.js assets.

## How to operate safely
1. Prefer editing source files (`*generator.md` and files under `shared-slides/`) rather than generated `_index.md` sections.
2. If using generated content placeholders (`write-here` blocks), run preprocessing before preview/build.
3. Keep changes scoped to course content/config unless theme changes are explicitly needed.

## Common commands
- Preprocess generated slide sources:
  - `shared-slides/preprocess.rb`
- Run local preview loop:
  - `HUGO_ARGS='-D' shared-slides/serve.sh`
- One-shot production build:
  - `hugo`

## Notes for future agents
- Do not assume generated `_index.md` content is hand-maintained.
- `ignoreFiles = ['.*generator.md$']` in `config.toml` means generator sources are excluded from direct publishing.
