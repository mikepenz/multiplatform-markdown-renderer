import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.model.markdownExtendedSpans

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        SampleTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Markdown Sample") }
                    )
                }
            ) {
                val scrollState = rememberScrollState()

                Markdown(
                    MARKDOWN,
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