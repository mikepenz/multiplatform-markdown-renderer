package com.mikepenz.markdown.ui

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.markdown.m2.Markdown

class SnapshotTests {
    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1750)
    @Composable
    fun DefaultTest() = TestThemeSetup {
        Markdown(MARKDOWN_DEFAULT)
    }

    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1750)
    @Composable
    fun DefaultDarkTest() = TestThemeSetup(true) {
        Markdown(MARKDOWN_DEFAULT)
    }

    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1000)
    @Composable
    fun RandomTest() = TestThemeSetup {
        Markdown(MARKDOWN_RANDOM)
    }

    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1000)
    @Composable
    fun RandomDarkTest() = TestThemeSetup(true) {
        Markdown(MARKDOWN_RANDOM)
    }

    /**Helper to have a single place setting up the theme*/
    @Composable
    private fun TestThemeSetup(dark: Boolean = false, block: @Composable () -> Unit) {
        SampleTheme(dark) {
            block()
        }
    }
}


private val MARKDOWN_DEFAULT = """
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
""".trimIndent()

private val MARKDOWN_RANDOM = """
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
""".trimIndent()