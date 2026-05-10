package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable

@Immutable
interface MarkdownAnnotatorConfig {
    /** Defines if a EOL should be treated as a new line */
    val eolAsNewLine: Boolean

    /**
     * If `true`, inline images that are taller than the surrounding line
     * height (by [BLOCK_FALLBACK_LINE_MULTIPLIER]) are promoted to block
     * rendering below the text, instead of being drawn as an inline
     * placeholder. This avoids overlap with preceding lines on platforms
     * where Compose's text engine does not grow line metrics to fit tall
     * placeholders.
     */
    val inlineImageAsBlock: Boolean
        get() = true

    companion object {
        /**
         * Multiplier of the surrounding text's line height above which an
         * inline image is promoted to a block element when
         * [inlineImageAsBlock] is enabled.
         */
        const val BLOCK_FALLBACK_LINE_MULTIPLIER = 1.5f
    }
}

@Immutable
class DefaultMarkdownAnnotatorConfig(
    override val eolAsNewLine: Boolean = false,
    override val inlineImageAsBlock: Boolean = true,
) : MarkdownAnnotatorConfig {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMarkdownAnnotatorConfig

        if (eolAsNewLine != other.eolAsNewLine) return false
        if (inlineImageAsBlock != other.inlineImageAsBlock) return false
        return true
    }

    override fun hashCode(): Int {
        var result = eolAsNewLine.hashCode()
        result = 31 * result + inlineImageAsBlock.hashCode()
        return result
    }
}

fun markdownAnnotatorConfig(
    eolAsNewLine: Boolean = false,
    inlineImageAsBlock: Boolean = true,
): MarkdownAnnotatorConfig = DefaultMarkdownAnnotatorConfig(
    eolAsNewLine = eolAsNewLine,
    inlineImageAsBlock = inlineImageAsBlock,
)
