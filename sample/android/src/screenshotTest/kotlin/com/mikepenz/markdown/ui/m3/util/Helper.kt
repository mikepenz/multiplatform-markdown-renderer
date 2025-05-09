package com.mikepenz.markdown.ui.m3.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.m3.theme.SampleTheme

@Composable
fun TestMarkdown(content: String) = SampleTheme(isSystemInDarkTheme()) {
    Markdown(rememberMarkdownState(content))
}