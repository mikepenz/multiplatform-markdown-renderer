<h1 align="center">
  Kotlin Multiplatform Markdown Renderer
</h1>

<p align="center">
    ... a Kotlin Multiplatform Markdown Renderer. (Android, Desktop, ...) powered by Compose Multiplatform
</p>

<div align="center">
    <a href="https://github.com/mikepenz/multiplatform-markdown-renderer/actions">
		<img src="https://github.com/mikepenz/multiplatform-markdown-renderer/workflows/CI/badge.svg"/>
	</a>
    <a href="https://central.sonatype.com/artifact/com.mikepenz/multiplatform-markdown-renderer">
        <img src="https://img.shields.io/maven-central/v/com.mikepenz/multiplatform-markdown-renderer?style=flat-square&color=%231B4897"/>
    </a>
</div>
<br />

-------

<p align="center">
    <a href="#whats-included-">What's included 🚀</a> &bull;
    <a href="#setup">Setup 🛠️</a> &bull;
    <a href="#usage">Usage 🛠️</a> &bull;
    <a href="#license">License 📓</a>
</p>

-------

### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight

-------

## Setup

### Using Gradle

<details open><summary><b>Multiplatform</b></summary>
<p>

For multiplatform projects specify this single dependency:

```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer:${version}")

    // Offers Material 2 defaults for Material 2 themed apps (com.mikepenz.markdown.m2.Markdown)
    implementation("com.mikepenz:multiplatform-markdown-renderer-m2:${version}")

    // Offers Material 3 defaults for Material 3 themed apps (com.mikepenz.markdown.m3.Markdown)
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:${version}")
}
```

</p>
</details>

<details><summary><b>JVM</b></summary>
<p>

To use the library on JVM, you have to include:

```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer-jvm:${version}")
}
```

</p>
</details>

<details><summary><b>Android</b></summary>
<p>

For Android a special dependency is available:

```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer-android:${version}")
}
```

</p>
</details>

> [!TIP]
> Since 0.13.0 the core library does not depend on a Material theme anymore. Include the `-m2`
> or `-m3` module to get
> access to the defaults.


-------

## Usage

```Kotlin
val markdown = """
### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight
""".trimIndent()

//
Markdown(markdown)
```

<details><summary><b>Advanced Usage</b></summary>
<p>

The library offers the ability to modify different behaviour when rendering the markdown.

### Provided custom style

```kotlin
Markdown(
    content,
    colors = markdownColors(text = Color.Red),
    typography = markdownTypography(h1 = MaterialTheme.typography.body1)
)
```

### Disable Animation

By default, the `MarkdownText` animates size changes (if images are loaded).

```kotlin
Markdown(
    content,
    animations = markdownAnimations(
        animateTextSize = {
            this
            /** No animation */
        }
    ),
}
```

### Extended spans

Starting with 0.16.0 the library includes support
for [extended-spans](https://github.com/saket/extended-spans).
> The library was integrated to make it multiplatform-compatible.
> All credits for its functionality go to [Saket Narayan](https://github.com/saket).

It is not enabled by default, however you can enable it quickly by configuring the `extendedSpans`
for your `Markdown` composeable.
Define the `ExtendedSpans` you want to apply (including optionally your own custom ones) and return
it.

```kotlin
Markdown(
    content,
    extendedSpans = markdownExtendedSpans {
        val animator = rememberSquigglyUnderlineAnimator()
        remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(),
                SquigglyUnderlineSpanPainter(animator = animator)
            )
        }
    }
)
```

### Extend Annotated string handling

The library already handles a significant amount of different tokens, however not all. To allow
special integrations expand this, you can pass in a custom `annotator` to the `Markdown`
composeable. This `annotator` allows you to customize existing handled tokens, but also add new
ones.

```kotlin
Markdown(
    content,
    annotator = markdownAnnotator { content, child ->
        if (child.type == GFMElementTypes.STRIKETHROUGH) {
            append("Replaced you :)")
            true // return true to consume this ASTNode child
        } else false
    }
)
```

### Adjust List Ordering

```kotlin
// Use the bullet list symbol from the original markdown
CompositionLocalProvider(LocalBulletListHandler provides { "$it " }) {
    Markdown(content)
}

// Replace the ordered list symbol with `A.)` instead.
CompositionLocalProvider(LocalOrderedListHandler provides { "A.) " }) {
    Markdown(content, Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState))
}
```

### Custom Components

Since v0.9.0 it is possible to provide custom components, instead of the default ones.
This can be done by providing the components `MarkdownComponents` to the `Markdown` composable.

Use the `markdownComponents()` to keep defaults for non overwritten components.

The `MarkdownComponent` will expose access to
the `content: String`, `node: ASTNode`, `typography: MarkdownTypography`,
offering full flexibility.

```kotlin
// Simple adjusted paragraph with different Modifier.
val customParagraphComponent: MarkdownComponent = {
    MarkdownParagraph(it.content, it.node, Modifier.align(Alignment.End))
}

// Full custom paragraph example
val customParagraphComponent: MarkdownComponent = {
    // build a styled paragraph. (util function provided by the library)
    val styledText = buildAnnotatedString {
        pushStyle(LocalMarkdownTypography.current.paragraph.toSpanStyle())
        buildMarkdownAnnotatedString(content, it.node)
        pop()
    }

    // define the `Text` composable
    Text(
        styledText,
        modifier = Modifier.align(Alignment.End),
        textAlign = TextAlign.End
    )
}

// Define the `Markdown` composable and pass in the custom paragraph component
Markdown(
    content,
    components = markdownComponents(
        paragraph = customParagraphComponent
    )
)
```

Another example to of a custom component is changing the rendering of an unordered list.

```kotlin
// Define a custom component for rendering unordered list items in Markdown
val customUnorderedListComponent: MarkdownComponent = {
    // Use the MarkdownListItems composable to render the list items
    MarkdownListItems(it.content, it.node, level = 0) { index, child ->
        // Render an icon for the bullet point with a green tint
        Icon(
            imageVector = icon,
            tint = Color.Green,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
    }
}

// Define the `Markdown` composable and pass in the custom unorderedList component
Markdown(
    content,
    components = markdownComponents(
        unorderedList = customUnorderedListComponent
    )
)
```

### Table Support

Starting with 0.30.0, the library includes support for rendering tables in markdown. The `Markdown`
composable will automatically handle table elements in your markdown content.

```kotlin
val markdown = """
| Header 1 | Header 2 |
|----------|----------|
| Cell 1   | Cell 2   |
| Cell 3   | Cell 4   |
""".trimIndent()

Markdown(markdown)
```

</p>
</details>

### Image Loading

Starting with 0.21.0 the library does not include image loading by default, however exposes 2
modules for either coil2 or coil3 dependencies.
The chosen image transformer implementation has to be passed to the `Markdown` API.

#### coil2

```groovy
// Offers coil2 (Coil2ImageTransformerImpl)
implementation("com.mikepenz:multiplatform-markdown-renderer-coil2:${version}")
```

```kotlin
Markdown(
    MARKDOWN,
    imageTransformer = Coil2ImageTransformerImpl,
)
```

> [!NOTE]
> 0.21.0 adds JVM support for this dependency via `HTTPUrlConnection` -> however this is expected to
> be removed in the
> future.

> [!NOTE]  
> Please refer to the official coil2 documentation on how to adjust the `ImageLoader`

#### coil3

```groovy
// Offers coil3 (Coil3ImageTransformerImpl)
implementation("com.mikepenz:multiplatform-markdown-renderer-coil3:${version}")
```

```kotlin
Markdown(
    MARKDOWN,
    imageTransformer = Coil3ImageTransformerImpl,
)
```

> [!NOTE]  
> Please refer to the official coil3 documentation on how to adjust the `SingletonImageLoader`

### Syntax Highlighting

The library (introduced with 0.27.0) offers optional support for syntax highlighting via
the [Highlights](https://github.com/SnipMeDev/Highlights) project.
This support is not included in the core, and can be enabled by adding the
`multiplatform-markdown-renderer-code`
dependency.

```groovy
implementation("com.mikepenz:multiplatform-markdown-renderer-code:${version}")
```

Once added, the `Markdown` has to be configured to use the alternative code highlighter.

```kotlin
// Use default color scheme
Markdown(
    MARKDOWN,
    components = markdownComponents(
        codeBlock = highlightedCodeBlock,
        codeFence = highlightedCodeFence,
    )
)

// ADVANCED: Customize Highlights library by defining different theme
val isDarkTheme = isSystemInDarkTheme()
val highlightsBuilder = remember(isDarkTheme) {
    Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
}
Markdown(
    MARKDOWN,
    components = markdownComponents(
        codeBlock = {
            MarkdownHighlightedCodeBlock(
                content = it.content,
                node = it.node,
                highlights = highlightsBuilder
            )
        },
        codeFence = {
            MarkdownHighlightedCodeFence(
                content = it.content,
                node = it.node,
                highlights = highlightsBuilder
            )
        },
    )
)
```

## Dependency

This project uses JetBrains [markdown](https://github.com/JetBrains/markdown/) Multiplatform
Markdown processor as
dependency to parse the markdown content.

## Developed By

* Mike Penz
* [mikepenz.com](http://mikepenz.com) - <mikepenz@gmail.com>
* [paypal.me/mikepenz](http://paypal.me/mikepenz)

## Contributors

This free, open source software was also made possible by a group of volunteers that put many hours
of hard work into
it. See the [CONTRIBUTORS.md](CONTRIBUTORS.md) file for details.

## Credits

Big thanks to [Erik Hellman](https://twitter.com/ErikHellman) and his awesome article
on [Rendering Markdown with Jetpack Compose](https://www.hellsoft.se/rendering-markdown-with-jetpack-compose/),
and the related source [MarkdownComposer](https://github.com/ErikHellman/MarkdownComposer).

Also huge thanks to [Saket Narayan](https://github.com/saket/) for his great work on
the [extended-spans](https://github.com/saket/extended-spans) project. Ported into this project to
make it multiplatform.

## Fork License

Copyright for portions of the code are held by [Erik Hellman, 2020] as part of
project [MarkdownComposer](https://github.com/ErikHellman/MarkdownComposer) under the MIT license.
All other copyright
for project multiplatform-markdown-renderer are held by [Mike Penz, 2023] under the Apache License,
Version 2.0.

## License

    Copyright 2024 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
