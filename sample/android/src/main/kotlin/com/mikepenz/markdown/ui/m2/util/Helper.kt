package com.mikepenz.markdown.ui.m2.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.m2.theme.SampleTheme

@Composable
fun TestMarkdown(content: String) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Markdown(rememberMarkdownState(content))
    }
}

@Composable
fun TestMarkdownCodeBlock(content: String) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Markdown(
            rememberMarkdownState(content),
            components = markdownComponents(
                codeBlock = highlightedCodeBlock,
                codeFence = highlightedCodeFence,
            ),
        )
    }
}