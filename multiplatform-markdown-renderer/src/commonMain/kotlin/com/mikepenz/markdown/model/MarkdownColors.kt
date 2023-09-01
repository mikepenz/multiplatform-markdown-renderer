package com.mikepenz.markdown.model

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
interface MarkdownColors {
    /** Represents the color used for the text of this [Markdown] component. */
    val text: Color

    /** Represents the color used for the text of code. */
    val codeText: Color

    /** Represents the color used for the background of code. */
    val codeBackground: Color

}

@Immutable
private class DefaultMarkdownColors(
    override val text: Color,
    override val codeText: Color,
    override val codeBackground: Color,
) : MarkdownColors

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colors.onBackground,
    codeText: Color = MaterialTheme.colors.onBackground,
    codeBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f)
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = codeText,
    codeBackground = codeBackground
)
