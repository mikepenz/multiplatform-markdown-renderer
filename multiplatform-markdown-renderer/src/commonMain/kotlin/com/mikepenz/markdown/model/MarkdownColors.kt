package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.compose.Markdown

@Stable
interface MarkdownColors {
    /** Represents the color used for the text of this [Markdown] component. */
    val text: Color

    /** Represents the color used for the text of code. */
    val codeText: Color

    /** Represents the color used for the text of code. */
    val inlineCodeText: Color

    /** Represents the color used for the text of links. */
    val linkText: Color

    /** Represents the color used for the background of code. */
    val codeBackground: Color

    /** Represents the color used for the inline background of code. */
    val inlineCodeBackground: Color

    /** Represents the color used for the color of dividers. */
    val dividerColor: Color

    /** Represents the color used for the text of tables. */
    val tableText: Color

    /** Represents the color used for the background of tables. */
    val tableBackground: Color
}

@Immutable
class DefaultMarkdownColors(
    override val text: Color,
    override val codeText: Color,
    override val inlineCodeText: Color,
    override val linkText: Color,
    override val codeBackground: Color,
    override val inlineCodeBackground: Color,
    override val dividerColor: Color,
    override val tableText: Color,
    override val tableBackground: Color,
) : MarkdownColors