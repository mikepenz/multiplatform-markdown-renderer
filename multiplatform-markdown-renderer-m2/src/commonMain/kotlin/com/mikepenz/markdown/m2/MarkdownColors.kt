package com.mikepenz.markdown.m2

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.MarkdownColors

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colors.onBackground,
    codeText: Color = MaterialTheme.colors.onBackground,
    linkText: Color = text,
    codeBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = codeText,
    linkText = linkText,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
)
