package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.compose.Markdown

@Immutable
interface MarkdownColors {
    /** Represents the color used for the text of this [Markdown] component. */
    val text: Color

    /** Represents the color used for the background of code. */
    val codeBackground: Color

    /** Represents the color used for the inline background of code. */
    val inlineCodeBackground: Color

    /** Represents the color used for the color of dividers. */
    val dividerColor: Color

    /** Represents the color used for the background of tables. */
    val tableBackground: Color
}

@Immutable
class DefaultMarkdownColors(
    override val text: Color,
    override val codeBackground: Color,
    override val inlineCodeBackground: Color,
    override val dividerColor: Color,
    override val tableBackground: Color,
) : MarkdownColors {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMarkdownColors

        if (text != other.text) return false
        if (codeBackground != other.codeBackground) return false
        if (inlineCodeBackground != other.inlineCodeBackground) return false
        if (dividerColor != other.dividerColor) return false
        if (tableBackground != other.tableBackground) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + codeBackground.hashCode()
        result = 31 * result + inlineCodeBackground.hashCode()
        result = 31 * result + dividerColor.hashCode()
        result = 31 * result + tableBackground.hashCode()
        return result
    }
}