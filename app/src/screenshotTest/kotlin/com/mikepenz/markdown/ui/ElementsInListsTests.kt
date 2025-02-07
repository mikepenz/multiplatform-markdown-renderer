package com.mikepenz.markdown.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.ui.annotation.DarkLightPreview

/**
 * Covers elements in lists behavior as documented
 * https://www.markdownguide.org/basic-syntax/#adding-elements-in-lists
 */
class ElementsInListsTests {
    @DarkLightPreview
    @Composable
    fun ParagraphInListTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            * This is the first list item.
            * Here's the second list item.
            
                I need to add another paragraph below the second list item.
            
            * And here's the third list item.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun BlockQuoteInListTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            * This is the first list item.
            * Here's the second list item.
            
                > A blockquote would look great below the second list item.
            
            * And here's the third list item.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun CodeBlockInListTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            1. Open the file.
            2. Find the following code block on line 21:
            
                    <html>
                      <head>
                      </head>
            
            3. Update the title to match the name of your website.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun ImageInListTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            1. Open the file containing the Linux mascot.
            2. Marvel at its beauty.
            
                ![Image](https://avatars.githubusercontent.com/u/1476232?u=3db0792ad9649618b182c9e24170c9be8ad9e32f&v=4&size=80)
            
            3. Close the file.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun UnorderedListInListTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            1. First item
            2. Second item
            3. Third item
                - Indented item
                - Indented item
            4. Fourth item
            """.trimIndent()
        )
    }
}