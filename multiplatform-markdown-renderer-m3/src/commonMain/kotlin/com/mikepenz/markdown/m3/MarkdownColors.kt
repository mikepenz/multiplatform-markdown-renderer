package com.mikepenz.markdown.m3

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.MarkdownColors

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colorScheme.onBackground,
    codeText: Color = MaterialTheme.colorScheme.onBackground,
    inlineCodeText: Color = codeText,
    linkText: Color = text,
    codeBackground: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
    dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = codeText,
    inlineCodeText = inlineCodeText,
    linkText = linkText,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
    dividerColor = dividerColor,
)
