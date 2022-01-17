package com.mikepenz.markdown

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes

/**
 * Represents the colors of the text, background and content used in a markdown component.
 *
 * See [MarkdownDefaults.markdownColors] for the default colors used in [Markdown].
 */
@Stable
interface MarkdownColors {
    /**
     * Represents the color used for the text of this [Markdown] component.
     */
    val textColor: Color

    /**
     * Represents the background color for this [Markdown] component.
     */
    val backgroundColor: Color

    /**
     * Represents the code background color for this [Markdown] component.
     */
    val codeBackgroundColor: Color

    /**
     * Returns the color used for the text of a specific type of this [Markdown] component.
     *
     * Type as represented by the markdown library. [MarkdownElementTypes.ATX_1], ...
     */
    fun textColorByType(type: IElementType): Color = textColor
}


/**
 * Contains the default values used by [Markdown].
 */
object MarkdownDefaults {

    /**
     * Creates a [MarkdownColors] that represents the default colors of the text, background and content used in a markdown component.
     */
    @Composable
    fun markdownColors(
        textColor: Color = MaterialTheme.colors.onBackground,
        backgroundColor: Color = MaterialTheme.colors.onSurface,
        codeBackgroundColor: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
        colorByType: ((type: IElementType) -> Color?)? = null
    ): MarkdownColors = DefaultMarkdownColors(
        textColor = textColor,
        backgroundColor = backgroundColor,
        codeBackgroundColor = codeBackgroundColor,
        colorByType = colorByType
    )
}


@Immutable
private class DefaultMarkdownColors(
    override val textColor: Color,
    override val backgroundColor: Color,
    override val codeBackgroundColor: Color,
    val colorByType: ((type: IElementType) -> Color?)? = null,
) : MarkdownColors {
    override fun textColorByType(type: IElementType): Color {
        return colorByType?.invoke(type) ?: super.textColorByType(type)
    }
}