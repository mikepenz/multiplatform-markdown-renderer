package com.mikepenz.markdown.compose.extendedspans.internal

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles

/**
 * Updates the [TextLinkStyles] with the provided [block].
 */
fun TextLinkStyles.update(block: SpanStyle.() -> SpanStyle): TextLinkStyles {
    return TextLinkStyles(
        style = style?.let { block(it) },
        focusedStyle = focusedStyle?.let { block(it) },
        hoveredStyle = hoveredStyle?.let { block(it) },
        pressedStyle = pressedStyle?.let { block(it) },
    )
}