package com.mikepenz.markdown.ui.m2

import androidx.compose.runtime.Composable
import com.mikepenz.markdown.ui.annotation.DarkLightPreview
import com.mikepenz.markdown.ui.m2.util.TestMarkdown

/**
 * Covers blockquote behavior as documented
 * https://www.markdownguide.org/basic-syntax/#blockquotes-1
 */
class BlockquotesTests {
    @DarkLightPreview
    @Composable
    fun BlockquoteTest() = TestMarkdown(
        """
        > Dorothy followed her through many of the beautiful rooms in her castle.
        """.trimIndent()
    )

    @DarkLightPreview
    @Composable
    fun BlockquoteWithMultipleLinesTest() = TestMarkdown(
        """
        > first line
        >
        > third line  
        > fourth line
        """.trimIndent()
    )

    @DarkLightPreview
    @Composable
    fun BlockquoteWithMultipleParagraphsTest() = TestMarkdown(
        """
        > Dorothy followed her through many of the beautiful rooms in her castle.
        >
        > The Witch bade her clean the pots and kettles and sweep the floor and keep the fire fed with wood.
        """.trimIndent()
    )

    @DarkLightPreview
    @Composable
    fun NestedBlockquoteTest() = TestMarkdown(
        """
        > Dorothy followed her through many of the beautiful rooms in her castle.
        >
        >> The Witch bade her clean the pots and kettles and sweep the floor and keep the fire fed with wood.
        >
        > Dorothy followed her through many of the beautiful rooms in her castle.
        """.trimIndent()
    )

    @DarkLightPreview
    @Composable
    fun BlockquoteWithOtherElementsTest() = TestMarkdown(
        """
        > #### The quarterly results look great!
        >
        > - Revenue was off the chart.
        > - Profits were higher than ever.
        >
        > *Everything* is going according to **plan**.
        """.trimIndent()
    )
}