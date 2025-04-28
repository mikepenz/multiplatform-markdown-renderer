package com.mikepenz.markdown.model

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface MarkdownInlineContent {
    /** Represents the map used to store tags and corresponding inline content */
    val inlineContent: Map<String, InlineTextContent>
}

@Immutable
class DefaultMarkdownInlineContent(
    override val inlineContent: Map<String, InlineTextContent>,
) : MarkdownInlineContent

@Composable
fun markdownInlineContent(
    content: Map<String, InlineTextContent> = mapOf()
): MarkdownInlineContent = DefaultMarkdownInlineContent(content)
