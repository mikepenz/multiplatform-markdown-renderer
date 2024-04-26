package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.ast.ASTNode

interface MarkdownAnnotator {
    /**
     * Use the [AnnotatedString.Builder] to build the string to display.
     * Return `true` to consume the child, false to allow default handling.
     *
     * @param content contains the whole content, and requires the `child` [ASTNode] to extract relevant text.
     */
    val annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean)?
}

@Immutable
class DefaultMarkdownAnnotator(
    override val annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean)?
) : MarkdownAnnotator

@Composable
fun markdownAnnotator(
    annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean)? = null
): MarkdownAnnotator = DefaultMarkdownAnnotator(
    annotate
)
