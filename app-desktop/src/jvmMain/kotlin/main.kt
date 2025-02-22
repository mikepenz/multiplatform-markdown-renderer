import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import com.mikepenz.app_desktop.generated.resources.Res
import com.mikepenz.markdown.Github
import com.mikepenz.markdown.OpenSourceInitiative
import com.mikepenz.markdown.SampleTheme
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.model.markdownExtendedSpans
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        val scrollState = rememberScrollState()
        val isDarkTheme = isSystemInDarkTheme()
        val uriHandler = LocalUriHandler.current
        var showLicenses by remember { mutableStateOf(false) }

        SampleTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Markdown Sample") },
                        actions = {
                            IconButton(onClick = { showLicenses = !showLicenses }) {
                                Icon(
                                    imageVector = OpenSourceInitiative,
                                    contentDescription = "Open Source"
                                )
                            }
                            IconButton(onClick = {
                                uriHandler.openUri("https://github.com/mikepenz/multiplatform-markdown-renderer")
                            }) {
                                Icon(
                                    imageVector = Github,
                                    contentDescription = "GitHub"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                if (showLicenses) {
                    var libs by remember { mutableStateOf<Libs?>(null) }
                    LaunchedEffect("aboutlibraries.json") {
                        libs = Libs.Builder()
                            .withJson(Res.readBytes("files/aboutlibraries.json").decodeToString())
                            .build()
                    }
                    LibrariesContainer(
                        libraries = libs,
                        modifier = Modifier.fillMaxSize(),
                        // sample uses material 2 - proxy theme
                        colors = LibraryDefaults.libraryColors(
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = contentColorFor(MaterialTheme.colors.background),
                            badgeBackgroundColor = MaterialTheme.colors.primary,
                            badgeContentColor = contentColorFor(MaterialTheme.colors.background),
                            dialogConfirmButtonColor = MaterialTheme.colors.primary,
                        ),
                        contentPadding = padding
                    )
                } else {
                    val highlightsBuilder = remember(isDarkTheme) {
                        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
                    }

                    SelectionContainer {
                        Markdown(
                            MARKDOWN,
                            components = markdownComponents(
                                codeBlock = { MarkdownHighlightedCodeBlock(it.content, it.node, highlightsBuilder) },
                                codeFence = { MarkdownHighlightedCodeFence(it.content, it.node, highlightsBuilder) },
                            ),
                            imageTransformer = Coil3ImageTransformerImpl,
                            extendedSpans = markdownExtendedSpans {
                                val animator = rememberSquigglyUnderlineAnimator()
                                remember {
                                    ExtendedSpans(
                                        RoundedCornerSpanPainter(),
                                        SquigglyUnderlineSpanPainter(animator = animator)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


private const val MARKDOWN = """
# Markdown Playground

---

# This is an H1

## This is an H2

### This is an H3

#### This is an H4

##### This is an H5

###### This is an H6

This is a paragraph with some *italic* and **bold** text.

This is a paragraph with some `inline code`.

This is a paragraph with a [link](https://www.jetbrains.com/).

This is a code block:
```kotlin
fun main() {
    println("Hello, world!")
}
```

> This is a block quote.

This is a divider

---

The above was supposed to be a divider.

~~This is strikethrough with two tildes~~

~This is strikethrough~

This is an ordered list:
1. Item 1
2. Item 2
3. Item 3

This is an unordered list with dashes:
- Item 1
- Item 2
- Item 3

This is an unordered list with asterisks:
* Item 1
* Item 2
* Item 3

This is an ordered list with task list items:
1. [ ] foo
2. [x] bar

This is an unordered list with task list items:
- [ ] foo
- [x] bar

-------- 

This is a markdown table:

| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |

--------

# Random

### Getting Started
                
For multiplatform projects specify this single dependency:

```
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer:{version}")
}
```

You can find more information on [GitHub](https://github.com/mikepenz/multiplatform-markdown-renderer). More Text after this.

![Image](https://avatars.githubusercontent.com/u/1476232?v=4)

There are many more things which can be experimented with like, inline `code`. 

Title 1
======

Title 2
------
              
[https://mikepenz.dev](https://mikepenz.dev)
[https://github.com/mikepenz](https://github.com/mikepenz)
[Mike Penz's Blog](https://blog.mikepenz.dev/)
<https://blog.mikepenz.dev/>
"""