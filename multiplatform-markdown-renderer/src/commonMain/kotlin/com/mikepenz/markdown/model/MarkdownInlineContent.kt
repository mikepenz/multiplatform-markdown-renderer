package com.mikepenz.markdown.model

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
interface MarkdownInlineContent {
    @Composable
    fun inlineContent(content: AnnotatedString): Map<String, InlineTextContent>
}

/**
 * Creates a [MarkdownInlineContent] from a static map (ignores the AnnotatedString).
 */
fun markdownInlineContent(
    staticContent: Map<String, InlineTextContent> = mapOf(),
): MarkdownInlineContent = StaticMarkdownInlineContent(staticContent)

@Immutable
private class StaticMarkdownInlineContent(
    private val staticContent: Map<String, InlineTextContent>,
) : MarkdownInlineContent {
    @Composable
    override fun inlineContent(content: AnnotatedString) = staticContent

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StaticMarkdownInlineContent) return false
        return staticContent == other.staticContent
    }

    override fun hashCode(): Int = staticContent.hashCode()
}
