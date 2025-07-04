package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable

@Immutable
interface MarkdownAnnotatorConfig {
    /** Defines if a EOL should be treated as a new line */
    val eolAsNewLine: Boolean
}

@Immutable
class DefaultMarkdownAnnotatorConfig(
    override val eolAsNewLine: Boolean = false,
) : MarkdownAnnotatorConfig {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMarkdownAnnotatorConfig

        return eolAsNewLine == other.eolAsNewLine
    }

    override fun hashCode(): Int {
        return eolAsNewLine.hashCode()
    }
}

fun markdownAnnotatorConfig(
    eolAsNewLine: Boolean = false,
): MarkdownAnnotatorConfig = DefaultMarkdownAnnotatorConfig(
    eolAsNewLine = eolAsNewLine,
)