package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle

@Immutable
interface MarkdownTypography {
    val text: TextStyle
    val code: TextStyle
    val inlineCode: TextStyle
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val h5: TextStyle
    val h6: TextStyle
    val quote: TextStyle
    val paragraph: TextStyle
    val ordered: TextStyle
    val bullet: TextStyle
    val list: TextStyle
    val textLink: TextLinkStyles
    val table: TextStyle
}

@Immutable
class DefaultMarkdownTypography(
    override val h1: TextStyle,
    override val h2: TextStyle,
    override val h3: TextStyle,
    override val h4: TextStyle,
    override val h5: TextStyle,
    override val h6: TextStyle,
    override val text: TextStyle,
    override val code: TextStyle,
    override val inlineCode: TextStyle,
    override val quote: TextStyle,
    override val paragraph: TextStyle,
    override val ordered: TextStyle,
    override val bullet: TextStyle,
    override val list: TextStyle,
    override val textLink: TextLinkStyles,
    override val table: TextStyle,
) : MarkdownTypography {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMarkdownTypography

        if (h1 != other.h1) return false
        if (h2 != other.h2) return false
        if (h3 != other.h3) return false
        if (h4 != other.h4) return false
        if (h5 != other.h5) return false
        if (h6 != other.h6) return false
        if (text != other.text) return false
        if (code != other.code) return false
        if (inlineCode != other.inlineCode) return false
        if (quote != other.quote) return false
        if (paragraph != other.paragraph) return false
        if (ordered != other.ordered) return false
        if (bullet != other.bullet) return false
        if (list != other.list) return false
        if (textLink != other.textLink) return false
        if (table != other.table) return false

        return true
    }

    override fun hashCode(): Int {
        var result = h1.hashCode()
        result = 31 * result + h2.hashCode()
        result = 31 * result + h3.hashCode()
        result = 31 * result + h4.hashCode()
        result = 31 * result + h5.hashCode()
        result = 31 * result + h6.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + inlineCode.hashCode()
        result = 31 * result + quote.hashCode()
        result = 31 * result + paragraph.hashCode()
        result = 31 * result + ordered.hashCode()
        result = 31 * result + bullet.hashCode()
        result = 31 * result + list.hashCode()
        result = 31 * result + textLink.hashCode()
        result = 31 * result + table.hashCode()
        return result
    }
}