import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.mikepenz.markdown.Github
import com.mikepenz.markdown.OpenSourceInitiative
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.markdownExtendedSpans
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalResourceApi::class
)
fun main() {
    CanvasBasedWindow("Markdown Sample", canvasElementId = "markdownCanvas") {
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
                                uriHandler.openUri("https://github.com/mikepenz/AboutLibraries")
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
                    /*
                    var libs by remember { mutableStateOf<Libs?>(null) }
                    LaunchedEffect("aboutlibraries.json") {
                        libs = Libs.Builder()
                            .withJson(Res.readBytes("files/aboutlibraries.json").decodeToString())
                            .build()
                    }
                    LibrariesContainer(
                        libraries = libs,
                        modifier = Modifier.fillMaxSize(),
                        colors = LibraryDefaults.libraryColors(backgroundColor = Color.Transparent),
                        contentPadding = padding
                    )

                     */
                } else {
                    val scrollState = rememberScrollState()
                    SelectionContainer {
                        Markdown(
                            MARKDOWN,
                            modifier = Modifier.padding(padding).fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(16.dp),
                            extendedSpans = markdownExtendedSpans {
                                val animator = rememberSquigglyUnderlineAnimator()
                                remember {
                                    ExtendedSpans(
                                        RoundedCornerSpanPainter(),
                                        SquigglyUnderlineSpanPainter(animator = animator)
                                    )
                                }
                            },
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
"""