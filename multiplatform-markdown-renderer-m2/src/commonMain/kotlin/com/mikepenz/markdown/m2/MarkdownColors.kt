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
    inlineCodeText: Color = codeText,
    linkText: Color = text,
    codeTitleText: Color = codeText,
    codeTitleBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
    codeBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
    dividerColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = codeText,
    inlineCodeText = inlineCodeText,
    linkText = linkText,
    codeTitleBackground = codeTitleBackground,
    codeTitleText = codeTitleText,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
    dividerColor = dividerColor
)
