package com.mikepenz.markdown.ui.m2

import android.content.res.Configuration
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.markdown.ui.m2.util.TestMarkdown

class SnapshotTests {
    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1750)
    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1750, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun DefaultTest() = TestMarkdown(MARKDOWN_DEFAULT)

    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1000)
    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun RandomTest() = TestMarkdown(MARKDOWN_RANDOM)

    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 380)
    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 380, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun ListCodeBlockTest() = TestMarkdown(MARKDOWN_LIST_CODE_BLOCK)

    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1250)
    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1250, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun ListTest() = TestMarkdown(MARKDOWN_LIST)

    @Preview(showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 1500)
    @Preview(showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 1500, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun TableTest() = TestMarkdown(MARKDOWN_TABLE)
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

This is a paragraph with some *italic* and **bold** text\.

This is a paragraph with some `inline code`\.

This is a paragraph with a [link](https://www.jetbrains.com/)\.

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


private val MARKDOWN_LIST_CODE_BLOCK = """
- Main item
  - Subitem 1
  - Subitem 2
    - Sub-subitem 1
    ```
    Some code
    
    
    And some more
    ```
    - Sub-subitem 2
  - Subitem 3
""".trimIndent()

private val MARKDOWN_TABLE = """
// simple table
    
| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |

// simple table with long rows

| Command | Description |
| --- | --- |
| git status | List all new or modified files |
| git diff | Show file differences that haven't been staged |

//  formatting table content

| Command | Description |
| --- | --- |
| `git status` | List all *new or modified* files |
| `git diff` | Show file differences that **haven't been** staged |

// alignment - not supported as of yet

| Left-aligned | Center-aligned | Right-aligned |
| :---         |     :---:      |          ---: |
| git status   | git status     | git status    |
| git diff     | git diff       | git diff      |

// special content

| Name     | Character |
| ---      | ---       |
| Backtick | `         |
| Pipe     | \|        |

// incorrect columns

| abc | def |
| --- | --- |
| bar |
| bar | baz | boo |

// different cell lengths

| Header 1 | Header 2 |
|----------|----------|
| Longer text | Ab  |
| Lorem   | CD 45   |
| Ipsum dd   | EF 67   |

""".trimIndent()

private val MARKDOWN_LIST = """
// unordered list

- George Washington
- John Adams
- Thomas Jefferson

// mixed unordered list (not properly supported by markdown lib)

- George Washington
* John Adams
+ Thomas Jefferson

// ordered list

1. James Madison
2. James Monroe
3. John Quincy Adams

// nested list

1. First list item
   - First nested list item
     - Second nested list item

// deep nested un-ordered list

* 1
* **Bold**: 2
  * 3
  * **Bold**: 4
      * 5
      * **Bold**: 6
      * 7
  * 8
* **Bold**: 9
""".trimIndent()