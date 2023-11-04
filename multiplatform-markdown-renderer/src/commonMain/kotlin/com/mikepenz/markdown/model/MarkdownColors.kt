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

    /** Represents the color used for the text of links. */
    val linkText: Color

    /** Represents the color used for the background of code. */
    val codeBackground: Color

    /** Represents the color used for the inline background of code. */
    val inlineCodeBackground: Color
}

@Immutable
private class DefaultMarkdownColors(
    override val text: Color,
    override val codeText: Color,
    override val linkText: Color,
    override val codeBackground: Color,
    override val inlineCodeBackground: Color,
) : MarkdownColors

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
