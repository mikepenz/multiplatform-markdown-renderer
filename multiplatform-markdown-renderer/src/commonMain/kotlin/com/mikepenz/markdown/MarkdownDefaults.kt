package com.mikepenz.markdown

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes


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
        textColor = textColor, backgroundColor = backgroundColor, codeBackgroundColor = codeBackgroundColor, colorByType = colorByType
    )

    /**
     * Creates a [MarkdownTypography] that represents the default text scale used in a markdown component.
     */
    @Composable
    fun markdownTypography(
        h1: TextStyle = MaterialTheme.typography.h1,
        h2: TextStyle = MaterialTheme.typography.h2,
        h3: TextStyle = MaterialTheme.typography.h3,
        h4: TextStyle = MaterialTheme.typography.h4,
        h5: TextStyle = MaterialTheme.typography.h5,
        h6: TextStyle = MaterialTheme.typography.h6,
        body1: TextStyle = MaterialTheme.typography.body1,
        body2: TextStyle = MaterialTheme.typography.body2,
    ): MarkdownTypography = DefaultMarkdownTypography(h1 = h1, h2 = h2, h3 = h3, h4 = h4, h5 = h5, h6 = h6, body1 = body1, body2 = body2)
}

/**
 * Represents the colors of the text, background and content used in a markdown component.
 *
 * See [MarkdownDefaults.markdownColors] for the default colors used in [Markdown].
 */
@Stable
interface MarkdownColors {
    /** Represents the color used for the text of this [Markdown] component. */
    val textColor: Color

    /** Represents the background color for this [Markdown] component. */
    val backgroundColor: Color

    /** Represents the code background color for this [Markdown] component. */
    val codeBackgroundColor: Color

    /**
     * Returns the color used for the text of a specific type of this [Markdown] component.
     *
     * Type as represented by the markdown library. [MarkdownElementTypes.ATX_1], ...
     */
    fun textColorByType(type: IElementType): Color = textColor
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


/**
 * Represents the type scale for the [Markdown].
 *
 * See [MarkdownDefaults.markdownTypography] for the default type scale used in [Markdown].
 */
@Stable
interface MarkdownTypography {
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val h5: TextStyle
    val h6: TextStyle
    val body1: TextStyle
    val body2: TextStyle
}


@Immutable
private class DefaultMarkdownTypography(
    override val h1: TextStyle,
    override val h2: TextStyle,
    override val h3: TextStyle,
    override val h4: TextStyle,
    override val h5: TextStyle,
    override val h6: TextStyle,
    override val body1: TextStyle,
    override val body2: TextStyle
) : MarkdownTypography