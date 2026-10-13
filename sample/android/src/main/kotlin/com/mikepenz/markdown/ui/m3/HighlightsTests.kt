package com.mikepenz.markdown.ui.m3

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.annotation.DarkLightPreview
import com.mikepenz.markdown.ui.m3.theme.SampleTheme
import com.mikepenz.markdown.ui.m3.util.TestMarkdown

@DarkLightPreview
@Composable
fun HighlightTest() = TestMarkdown(
    """
    Before ==highlighted text== after.

    ==A highlighted sentence long enough to wrap onto another line while retaining its background.==

    ==First== and ==second== highlights.
    """.trimIndent()
)

@DarkLightPreview
@Composable
fun HighlightWithInlineFormattingTest() = TestMarkdown(
    """
    ==**Bold**, *italic*, and ~~strikethrough~~==.

    ==A [link](https://example.com)== and ==a = b==.

    Literal equals: a = b, =text=, ===text===, ==a=b==, and \=escaped\=.
    """.trimIndent()
)

@DarkLightPreview
@Composable
fun HighlightInTableTest() = TestMarkdown(
    """
    | Feature | ==Result== |
    | --- | --- |
    | Plain | ==highlighted== |
    | Nested | ==**bold** and *italic*== |
    | Equals | ==a = b== |
    """.trimIndent()
)

@DarkLightPreview
@Composable
fun HighlightWithCustomColorsTest() = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Markdown(
            rememberMarkdownState("Before ==custom colors== after.\n\n==**Bold** and *italic*==."),
            colors = markdownColor(
                highlightText = Color(0xFF6A1B9A),
                highlightBackground = Color(0xFFFFF59D),
            ),
        )
    }
}
