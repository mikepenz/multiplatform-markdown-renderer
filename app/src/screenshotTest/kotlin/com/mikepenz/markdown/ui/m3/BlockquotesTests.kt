package com.mikepenz.markdown.ui.m3

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.ui.annotation.DarkLightPreview
import com.mikepenz.markdown.ui.m3.theme.SampleTheme

/**
 * Covers blockquote behavior as documented
 * https://www.markdownguide.org/basic-syntax/#blockquotes-1
 */
class BlockquotesTests {
    @DarkLightPreview
    @Composable
    fun BlockquoteTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            > Dorothy followed her through many of the beautiful rooms in her castle.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun BlockquoteWithMultipleParagraphsTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            > Dorothy followed her through many of the beautiful rooms in her castle.
            >
            > The Witch bade her clean the pots and kettles and sweep the floor and keep the fire fed with wood.
            """.trimIndent()
        )
    }


    @DarkLightPreview
    @Composable
    fun NestedBlockquoteTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
            """
            > Dorothy followed her through many of the beautiful rooms in her castle.
            >
            >> The Witch bade her clean the pots and kettles and sweep the floor and keep the fire fed with wood.
            """.trimIndent()
        )
    }

    @DarkLightPreview
    @Composable
    fun BlockquoteWithOtherElementsTest() = SampleTheme(isSystemInDarkTheme()) {
        Markdown(
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
}