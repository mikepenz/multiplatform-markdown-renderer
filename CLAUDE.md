# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commit Conventions

All commits must follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```
<type>(<scope>): <short description>

[optional body]

[optional footer(s)]
```

Common types: `feat`, `fix`, `chore`, `refactor`, `docs`, `test`, `ci`, `perf`, `build`.

### Dependency Update Commits

Dependency update commits must be **verbose** — every changed dependency must be listed explicitly with its old and new version. Vague messages like `chore: bump dependencies` are not acceptable.

Format:
```
chore(deps): update dependencies

- <dependency-name> <old-version> -> <new-version>

via <catalog-artifact> <old-version> -> <new-version>:
- <dependency-name> <old-version> -> <new-version>
```

## Dependency Management

This project uses two sources for dependency versions:

1. **`gradle/libs.versions.toml`** — project-local versions (coil, ktor, androidx, etc.)
2. **`com.mikepenz:version-catalog`** in `settings.gradle.kts` — shared catalog from https://github.com/mikepenz/convention

When the version catalog is bumped in `settings.gradle.kts`, always check https://github.com/mikepenz/convention to identify every transitive dependency version change between the old and new catalog versions. Include them all in the commit message under a `via com.mikepenz:version-catalog <old> -> <new>:` section.

## Build & Development Commands

```bash
# Build
./gradlew :multiplatform-markdown-renderer:assemble

# Run sample apps
./gradlew :sample:android:installDebug
./gradlew :sample:desktop:run
./gradlew :sample:web:wasmJsBrowserDevelopmentRun

# Tests (Paparazzi screenshot tests)
./gradlew :sample:android:verifyPaparazzi        # validate against snapshots
./gradlew :sample:android:updateDebugScreenshotTest  # update snapshots

# API compatibility check
./gradlew apiCheck

# Lint
./gradlew lintDebug
```

## Module Structure

| Module | Purpose |
|--------|---------|
| `multiplatform-markdown-renderer` | Core parsing & rendering (all platforms) |
| `multiplatform-markdown-renderer-m2` | Material 2 themed defaults |
| `multiplatform-markdown-renderer-m3` | Material 3 themed defaults |
| `multiplatform-markdown-renderer-coil2` | Coil 2 image loading integration |
| `multiplatform-markdown-renderer-coil3` | Coil 3 image loading integration |
| `multiplatform-markdown-renderer-code` | Syntax highlighting via Highlights |
| `sample/shared` | KMP sample code shared across platforms |
| `sample/android` | Android sample + Paparazzi tests |
| `sample/desktop` | Desktop (JVM) sample |
| `sample/web` | Web (WASM) sample |

## Architecture

### Entry Points

- `Markdown(content: String, ...)` — simple composable, creates state internally
- `Markdown(markdownState: MarkdownState, ...)` — advanced, use pre-created state
- M2/M3 modules wrap the core composable with Material-themed defaults via `markdownColor()` / `markdownTypography()`

### Rendering Flow

```
String input
  → MarkdownState (async parse via coroutines)
  → JetBrains Markdown parser → AST tree
  → ReferenceLinkHandler (resolves reference-style links)
  → Composable tree via MarkdownComponents
      MarkdownComponent = @Composable (MarkdownComponentModel) -> Unit
```

Each element type (text, headings, lists, code, images, tables, blockquotes) has a corresponding component. All components are overridable by passing a custom `MarkdownComponents` instance.

### Key Interfaces

- **`MarkdownComponents`** — defines all renderable elements; override individual components to customise rendering
- **`ImageTransformer`** — image loading abstraction; implemented by `Coil3ImageTransformerImpl` / `Coil2ImageTransformerImpl`; default is no-op
- **`MarkdownAnnotator`** — extend token annotation (links, code spans, custom styles)
- **`ReferenceLinkHandler`** — resolves reference-style markdown links

### Configuration Models

`MarkdownTypography`, `MarkdownColors`, `MarkdownDimens`, `MarkdownPadding`, `MarkdownAnimations`, `MarkdownExtendedSpans`, `MarkdownInlineContent` — passed as composition locals via `ComposeLocal.kt`.

### Extended Spans

`ExtendedSpans` enables advanced text decoration (rounded code backgrounds, squiggly underlines) via `RoundedCornerSpanPainter` and `SquigglyUnderlineSpanPainter`, applied in `MarkdownText`.

## API Compatibility

Binary compatibility validation is enabled (`com.mikepenz.binary-compatibility-validator.enabled=true`). Run `./gradlew apiCheck` before committing API-visible changes, and `./gradlew apiDump` to update `.api` files when intentional API changes are made.
