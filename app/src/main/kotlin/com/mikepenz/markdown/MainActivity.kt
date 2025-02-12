package com.mikepenz.markdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.ui.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {
    val isSystemInDarkMode = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(isSystemInDarkMode) }

    SampleTheme(darkMode) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 16.dp),
        ) {

            item {
                Spacer(modifier = Modifier.height(56.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Dark mode enabled", color = MaterialTheme.colors.onBackground)
                    Switch(checked = darkMode, onCheckedChange = { darkMode = !darkMode })
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
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
                )
            }
            item {
                Spacer(modifier = Modifier.height(48.dp))
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