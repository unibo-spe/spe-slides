# Software Process Engineering Slides
Software Process Engineering slides is a Hugo-based static site generator that creates reveal.js presentation slides for a university course. The system uses Ruby preprocessing to generate markdown content from shared slide components, then builds static HTML with Hugo.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively
- **Prerequisites and Setup:**
  - Ensure Git submodules are initialized: `git submodule init && git submodule update` 
  - Install Hugo extended: Download from `https://github.com/gohugoio/hugo/releases/download/v0.149.1/hugo_extended_0.149.1_linux-amd64.deb` then `sudo dpkg -i hugo.deb`
  - Install inotify-tools for development: `sudo apt-get install -y inotify-tools`
  - Ruby should be available (version 3.4.5+): `ruby --version`

- **Build the slides (FAST - takes ~3 seconds total):**
  - `shared-slides/preprocess.rb` -- Ruby preprocessor, takes ~1 second
  - `hugo` -- Hugo build, takes ~1.5 seconds. Output goes to `build/` directory
  - **Note**: This is a FAST build system, NOT slow like some projects. No long timeouts needed.

- **Development workflow:**
  - For simple development server: `hugo server --bind 0.0.0.0 --port 1313`
  - For development with automatic rebuild on file changes: `./shared-slides/serve.sh`
  - Access slides at http://localhost:1313/
  - **File watching**: The serve.sh script automatically runs the preprocessor and rebuilds when content or shared-slides change

## Validation
- After making changes, ALWAYS run the complete build process to ensure nothing breaks:
  1. `shared-slides/preprocess.rb`
  2. `hugo`
  3. Check that `build/index.html` and individual slide pages like `build/01-devops-intro/index.html` exist
- **Manual testing**: Visit http://localhost:1313/ in a browser to verify slides render correctly
- **No formal tests**: This repository does not have unit tests or linting tools - it's a content-focused slides system

## Repository Structure
- **`content/`**: Course slide content, including generator files (`*_generator.md`)
- **`shared-slides/`**: Git submodule containing shared slide content and Ruby preprocessor
- **`themes/reveal-hugo/`**: Git submodule containing the reveal.js Hugo theme
- **`reusable/`**: Shared headers and common slide content
- **`config.toml`**: Hugo configuration
- **`build/`**: Generated static site output (created by Hugo)

## Key Files and Scripts
- **`shared-slides/preprocess.rb`**: Ruby script that processes `*_generator.md` files, replacing `<!-- write-here "file" -->` directives with actual file content
- **`shared-slides/serve.sh`**: Development server script with file watching and automatic rebuild
- **`config.toml`**: Main Hugo configuration (reveal.js theme, build settings, etc.)

## Common Tasks
The following are outputs from frequently run commands. Reference them instead of viewing, searching, or running bash commands to save time.

### Repository root listing
```
ls -la /
.git/             (Git repository)
.github/          (GitHub workflows and settings)  
.gitmodules       (Git submodule configuration)
.ruby-version     (Ruby version: 3.4.5)
config.toml       (Hugo configuration)
content/          (Slide content and generators)
shared-slides/    (Git submodule: shared slide content)
themes/           (Git submodule: reveal-hugo theme)
reusable/         (Shared slide components)
layouts/          (Hugo layout templates)
static/           (Static assets)
assets/           (Theme assets)
build/            (Generated output - created by Hugo)
```

### Hugo build output
```
hugo
Start building sites … 
                  │ EN  
──────────────────┼─────
 Pages            │  18 
 Paginator pages  │   0 
 Non-page files   │ 102 
 Static files     │ 103 
 Processed images │   0 
 Aliases          │   0 
 Cleaned          │   0 

Total in ~1500 ms
```

### Development server startup
```
hugo server --bind 0.0.0.0 --port 1313
Web Server is available at http://localhost:1313/
Built in ~500ms
Environment: "development"
```

## Important Notes
- **Git Submodules**: This repository requires two submodules. Always run `git submodule init && git submodule update` after cloning
- **Generator Files**: Files named `*_generator.md` in content directories are processed by the Ruby script to create the actual `_index.md` files
- **Fast Builds**: Unlike many projects, this builds very quickly (total ~3 seconds). Don't use long timeouts
- **No Package Managers**: This project doesn't use npm, pip, or other package managers - just Hugo, Ruby, and Git submodules  
- **Live Reload**: The development server automatically reloads slides in the browser when files change
- **Output Directory**: All generated content goes to `build/` directory as configured in `config.toml`
- **Content Editing**: To modify slides, edit the source files in `content/` directories or the shared content in `shared-slides/`
- **Theme Customization**: The reveal.js theme is configured in `config.toml` and custom styles are in `assets/`

## Typical Development Workflow
1. **Initial setup** (once): `git submodule init && git submodule update`
2. **Start development**: `./shared-slides/serve.sh` 
3. **Edit content**: Modify files in `content/` or `shared-slides/`
4. **View changes**: Slides automatically rebuild and reload at http://localhost:1313/
5. **Final build**: `shared-slides/preprocess.rb && hugo` to generate production build in `build/`

## Troubleshooting
- **Empty shared-slides directory**: Run `git submodule init && git submodule update`
- **Hugo not found**: Install Hugo extended from GitHub releases (see prerequisites)
- **Ruby errors**: Ensure Ruby 3.4.5+ is installed
- **Build fails**: Check that all submodules are properly initialized
- **Development server won't start**: Ensure inotify-tools is installed for file watching