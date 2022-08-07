package com.mikepenz.markdown.model

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

interface MarkdownColors {
    val text: Color
    val backgroundCode: Color
}

@Immutable
private class DefaultMarkdownColors(
    override val text: Color,
    override val backgroundCode: Color
) : MarkdownColors

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colors.onPrimary,
    backgroundCode: Color = MaterialTheme.colors.primary
): MarkdownColors = DefaultMarkdownColors(text = text, backgroundCode = backgroundCode)
