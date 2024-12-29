package com.mikepenz.markdown.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.model.MarkdownTypography
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

/**
 * Tag used to indicate an url for inline content. Required for click handling.
 */
const val MARKDOWN_TAG_URL = "MARKDOWN_URL"

/**
 * Tag used to indicate an image url for inline content. Required for rendering.
 */
const val MARKDOWN_TAG_IMAGE_URL = "MARKDOWN_IMAGE_URL"

/**
 * Find a child node recursive
 */
internal fun ASTNode.findChildOfTypeRecursive(type: IElementType): ASTNode? {
    children.forEach {
        if (it.type == type) {
            return it
        } else {
            val found = it.findChildOfTypeRecursive(type)
            if (found != null) {
                return found
            }
        }
    }
    return null
}

/**
 * Helper function to drop the first and last element in the children list.
 * E.g., we don't want to render the brackets of a link
 */
internal fun List<ASTNode>.innerList(): List<ASTNode> = this.subList(1, this.size - 1)

/**
 * Extension function for `ASTNode` that retrieves the unescaped text within the node.
 *
 * This function first gets the text within the node, then converts it to a string.
 * It then uses `EntityConverter.replaceEntities` to replace any escaped entities
 * in the text, ensuring that the text is properly unescaped.
 *
 * @param allFileText The complete text of the file, which is used to extract the text within the node.
 * @return The unescaped text within the node.
 */
fun ASTNode.getUnescapedTextInNode(allFileText: CharSequence): String {
    val escapedText = getTextInNode(allFileText).toString()
    return EntityConverter.replaceEntities(
        escapedText,
        processEntities = false,
        processEscapes = true
    )
}

/**
 * Extension property to get the `SpanStyle` for link text.
 * This style is defined by the `link` typography and the current markdown colors.
 */
val MarkdownTypography.linkTextSpanStyle: SpanStyle
    @Composable
    get() = link.copy(color = LocalMarkdownColors.current.linkText).toSpanStyle()

/**
 * Extension property to get the `SpanStyle` for inline code text.
 * This style is defined by the `inlineCode` typography and the current markdown colors.
 */
val MarkdownTypography.codeSpanStyle: SpanStyle
    @Composable
    get() = inlineCode.copy(
        color = LocalMarkdownColors.current.inlineCodeText,
        background = LocalMarkdownColors.current.inlineCodeBackground
    ).toSpanStyle()
