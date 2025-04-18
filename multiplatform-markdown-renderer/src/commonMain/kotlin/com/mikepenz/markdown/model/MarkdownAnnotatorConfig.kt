package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface MarkdownAnnotatorConfig {
    /** Defines if a EOL should be treated as a new line */
    val eolAsNewLine: Boolean
}

@Immutable
class DefaultMarkdownAnnotatorConfig(
    override val eolAsNewLine: Boolean = false,
) : MarkdownAnnotatorConfig

fun markdownAnnotatorConfig(
    eolAsNewLine: Boolean = false,
): MarkdownAnnotatorConfig = DefaultMarkdownAnnotatorConfig(
    eolAsNewLine = eolAsNewLine,
)