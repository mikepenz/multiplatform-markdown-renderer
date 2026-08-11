package com.mikepenz.markdown.ui.readme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.elements.MarkdownCheckBox
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.sample.theme.SampleTheme
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes

/**
 * Panels rendered for the README showcase. Unlike the `ui.m2` / `ui.m3` previews — which pin
 * behaviour for tests — these go through the sample app's own [SampleTheme] and mirror
 * `MarkDownPage` so the README shows the product, not test output.
 *
 * Copied into `art/` by the `copyReadmeArt` task; see the map in `sample/android/build.gradle.kts`.
 */
@Preview(name = "light", heightDp = 490)
@Preview(name = "dark", heightDp = 490, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ShowcasePreview

@Composable
private fun Panel(content: @Composable () -> Unit) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) { content() }
    }
}

/** Headings, emphasis, links, lists and blockquotes — the default `Markdown` composable. */
@ShowcasePreview
@Composable
fun ShowcaseRichText() = Panel {
    Markdown(rememberMarkdownState(RICH_TEXT))
}

/** `markdownComponents(codeFence = ...)` wired to `MarkdownHighlightedCodeFence`. */
@ShowcasePreview
@Composable
fun ShowcaseSyntaxHighlighting() = Panel {
    val darkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(darkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = darkTheme))
    }
    Markdown(
        rememberMarkdownState(CODE),
        components = markdownComponents(
            codeFence = {
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    highlightsBuilder = highlightsBuilder,
                    showHeader = true,
                )
            },
        ),
    )
}

/** GFM tables and GitHub alert banners, both rendered out of the box. */
@ShowcasePreview
@Composable
fun ShowcaseTablesAndAlerts() = Panel {
    Markdown(rememberMarkdownState(TABLE_AND_ALERT))
}

/** `markdownComponents(checkbox = ...)`, `markdownColor(...)` and `markdownExtendedSpans`. */
@ShowcasePreview
@Composable
fun ShowcaseCustomComponents() = Panel {
    Markdown(
        rememberMarkdownState(CUSTOM),
        colors = markdownColor(inlineCodeBackground = Color(0x2600B9FF)),
        components = markdownComponents(
            checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) },
        ),
        extendedSpans = markdownExtendedSpans {
            val animator = rememberSquigglyUnderlineAnimator()
            remember {
                ExtendedSpans(
                    RoundedCornerSpanPainter(),
                    SquigglyUnderlineSpanPainter(animator = animator),
                )
            }
        },
    )
}

private val RICH_TEXT = """
# Markdown, rendered

Compose Multiplatform text with *italic*, **bold**, `inline code`
and a [real link](https://github.com/mikepenz).

> Blockquotes keep their accent bar.

1. Ordered lists
2. Nested content
   - and unordered children

- Unordered lists, ~~strikethrough~~
""".trimIndent()

private val CODE = """
### Syntax highlighting

```kotlin
fun greet(name: String): String {
    val greeting = "Hello, ${'$'}name"
    return greeting.uppercase()
}
```

```json
{ "highlights": true }
```

Opt in with the `-code` module, then pass
`highlightedCodeFence` to `markdownComponents()`.
""".trimIndent()

private val TABLE_AND_ALERT = """
### Tables & alerts

| Module | Purpose |
| --- | --- |
| `-m2` | Material 2 defaults |
| `-m3` | Material 3 defaults |
| `-coil3` | Image loading |

> [!NOTE]
> GitHub alerts render as an accent bar with an icon and a title.

> [!WARNING]
> Every color and dimension is configurable.
""".trimIndent()

private val CUSTOM = """
### Bring your own components

Every element is a `MarkdownComponent`, overridable
through `markdownComponents()`.

- [x] Custom `checkbox`
- [ ] Custom `codeFence`

Inline `code spans` get a rounded background from
`RoundedCornerSpanPainter`.
""".trimIndent()
