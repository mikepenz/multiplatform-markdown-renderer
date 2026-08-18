package com.mikepenz.markdown.m3

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.MarkdownAlertColors
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.markdownAlertColors

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colorScheme.onBackground,
    codeBackground: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
    dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
    tableBackground: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.02f),
    /** Material 3 has no `isLight` flag; derive it from the background so alerts follow the theme. */
    darkTheme: Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f,
    alert: MarkdownAlertColors = markdownAlertColors(darkTheme),
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
    dividerColor = dividerColor,
    tableBackground = tableBackground,
    alert = alert,
)
