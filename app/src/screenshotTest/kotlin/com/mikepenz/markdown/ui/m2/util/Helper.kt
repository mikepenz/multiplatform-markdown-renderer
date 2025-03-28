package com.mikepenz.markdown.ui.m2.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.SampleTheme

@Composable
fun TestMarkdown(content: String) = SampleTheme(isSystemInDarkTheme()) {
    Markdown(rememberMarkdownState(content, immediate = true))
}