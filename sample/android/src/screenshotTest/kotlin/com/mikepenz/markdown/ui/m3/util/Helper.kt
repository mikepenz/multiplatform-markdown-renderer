package com.mikepenz.markdown.ui.m3.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.m3.theme.SampleTheme

@Composable
fun TestMarkdown(content: String) = SampleTheme(isSystemInDarkTheme()) {
    Markdown(rememberMarkdownState(content))
}

@Composable
fun TestMarkdownCodeBlock(content: String) = SampleTheme(isSystemInDarkTheme()) {
    Markdown(rememberMarkdownState(content),
        components = markdownComponents(
            codeBlock = highlightedCodeBlock,
            codeFence = highlightedCodeFence,
        ),
    )
}
