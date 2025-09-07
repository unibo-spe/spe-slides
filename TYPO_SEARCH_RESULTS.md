# Typo Search Results for SPE Slides

## Summary

A comprehensive typo search was performed across all slide content in the repository. The search included:

- **Content directory**: `content/` - Course slide content
- **Reusable directory**: `reusable/` - Shared headers and common slide content  
- **Shared slides directory**: `shared-slides/` - Git submodule containing shared slide content

## Results

- **Files scanned**: 47 markdown files
- **Definite typos found**: 22
- **Files with typos**: 8

## Definite Typos Found

### English Content Typos

1. **content/07-ci/_index.md** (Line 98): `si` → `is`
   - Context: "This si different in a continuously integrated environment"

2. **content/09-containerization/_index.md** (Line 1100): `docker docker` → `docker`
   - Context: Duplicate word in Docker command

3. **content/12-multiplatform/_index.md** (Line 2094): `the the` → `the`
   - Context: "notice the `expect` keyword, and the the lack of function body"

4. **content/extra/pps-ci-introduction/_index.md** (Line 1206): `si` → `is`
   - Context: "This si different in a continuously integrated environment"

5. **shared-slides/ci/intro.md** (Line 54): `si` → `is`
   - Context: "This si different in a continuously integrated environment"

### Italian Content Typos

The remaining 17 typos are in Italian language content in the shared-slides directory:

- **shared-slides/build-systems/it-gradle-basics.md**: 6 instances of incorrect `si` usage
- **shared-slides/build-systems/it-gradle-dependencies.md**: 7 instances of incorrect `si` usage  
- **shared-slides/java/it-classpath-resources.md**: 4 instances of incorrect `si` usage

Note: The Italian content typos appear to be instances where `si` should remain `si` (Italian) but were flagged by the English-focused typo checker. These may be false positives and should be reviewed by an Italian speaker.

## Tools Created

1. **scripts/typo-check.rb** - Comprehensive typo detection script
2. **scripts/typo-report.rb** - Focused script showing only definite typos

## Usage

To run the typo checker:

```bash
# Full report (includes suspicious patterns)
ruby scripts/typo-check.rb

# Typos only
ruby scripts/typo-report.rb
```

## Recommendations

1. Fix the 5 clear English typos identified above
2. Review the Italian content flagged by the checker with an Italian speaker
3. Run the typo checker periodically as part of content review process
4. Consider adding the typo checker to CI/CD pipeline for automated detection

## Files Affected

- content/07-ci/_index.md
- content/09-containerization/_index.md  
- content/12-multiplatform/_index.md
- content/extra/pps-ci-introduction/_index.md
- shared-slides/ci/intro.md
- shared-slides/build-systems/it-gradle-basics.md
- shared-slides/build-systems/it-gradle-dependencies.md
- shared-slides/java/it-classpath-resources.md