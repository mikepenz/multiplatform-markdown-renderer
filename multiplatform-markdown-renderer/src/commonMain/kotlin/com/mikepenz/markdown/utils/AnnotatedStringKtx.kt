package com.mikepenz.markdown.utils

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mikepenz.markdown.compose.LocalMarkdownAnnotator
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.model.MarkdownAnnotator
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser

/**
 * Extension function to build an `AnnotatedString` from a Markdown string.
 * This function will parse the Markdown content and apply the given styles to the text.
 *
 * It only supports TEXT and PARAGRAPH nodes.
 *
 * @param style The base text style to apply.
 * @param linkTextSpanStyle The style to apply to link text.
 * @param codeSpanStyle The style to apply to code spans.
 * @param flavour The Markdown flavour descriptor to use (default is GFM).
 * @param annotator An optional annotator for additional processing.
 * @return The constructed `AnnotatedString`.
 */
fun String.buildMarkdownAnnotatedString(
    style: TextStyle,
    linkTextSpanStyle: SpanStyle = style.toSpanStyle(),
    codeSpanStyle: SpanStyle = style.toSpanStyle(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    annotator: MarkdownAnnotator? = null,
): AnnotatedString {
    val content = this
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
    val textNode = parsedTree.children.firstOrNull { node ->
        node.type == MarkdownTokenTypes.TEXT || node.type == MarkdownElementTypes.PARAGRAPH
    }
    if (textNode == null) return buildAnnotatedString { }
    return content.buildMarkdownAnnotatedString(
        textNode = textNode,
        style = style,
        linkTextSpanStyle = linkTextSpanStyle,
        codeSpanStyle = codeSpanStyle,
        annotator = annotator
    )
}

/**
 * Extension function to build an `AnnotatedString` from a Markdown string.
 * This function will parse the Markdown content and apply the given styles to the text.
 *
 * It only supports TEXT and PARAGRAPH nodes.
 *
 * @param textNode The AST node representing the text.
 * @param style The base text style to apply.
 * @param linkTextSpanStyle The style to apply to link text.
 * @param codeSpanStyle The style to apply to code spans.
 * @param annotator An optional annotator for additional processing.
 * @return The constructed `AnnotatedString`.
 */
fun String.buildMarkdownAnnotatedString(
    textNode: ASTNode,
    style: TextStyle,
    linkTextSpanStyle: SpanStyle = style.toSpanStyle(),
    codeSpanStyle: SpanStyle = style.toSpanStyle(),
    annotator: MarkdownAnnotator? = null,
): AnnotatedString = buildAnnotatedString {
    pushStyle(style.toSpanStyle())
    buildMarkdownAnnotatedString(this@buildMarkdownAnnotatedString, textNode, linkTextSpanStyle, codeSpanStyle, annotator)
    pop()
}

@Deprecated("Use the non composable `appendMarkdownLink` function instead. This function will be removed in a future release.")
@Composable
fun AnnotatedString.Builder.appendMarkdownLink(content: String, node: ASTNode) = appendMarkdownLink(
    content = content,
    node = node,
    linkTextStyle = LocalMarkdownTypography.current.linkTextSpanStyle,
    codeStyle = LocalMarkdownTypography.current.codeSpanStyle,
    annotator = LocalMarkdownAnnotator.current
)

/**
 * Appends a Markdown link to the `AnnotatedString.Builder`.
 *
 * @param content The content string.
 * @param node The AST node representing the link.
 * @param linkTextStyle The style to apply to the link text.
 * @param codeStyle The style to apply to code spans within the link.
 * @param annotator An optional annotator for additional processing.
 */
fun AnnotatedString.Builder.appendMarkdownLink(
    content: String,
    node: ASTNode,
    linkTextStyle: SpanStyle,
    codeStyle: SpanStyle,
    annotator: MarkdownAnnotator? = null,
) {
    val linkText = node.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.children?.innerList()
    if (linkText == null) {
        append(node.getUnescapedTextInNode(content))
        return
    }
    val destination = node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getUnescapedTextInNode(content)
    val linkLabel = node.findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getUnescapedTextInNode(content)
    val annotation = destination ?: linkLabel
    if (annotation != null) pushStringAnnotation(MARKDOWN_TAG_URL, annotation)
    pushStyle(linkTextStyle)
    buildMarkdownAnnotatedString(content, linkText, linkTextStyle, codeStyle, annotator)
    pop()
    if (annotation != null) pop()
}

@Deprecated(
    "Use the non composable `appendAutoLink` function instead. This function will be removed in a future release.",
    ReplaceWith("appendAutoLink(content, node, LocalMarkdownTypography.current.linkTextSpanStyle)", "com.mikepenz.markdown.compose.LocalMarkdownTypography")
)
@Composable
fun AnnotatedString.Builder.appendAutoLink(content: String, node: ASTNode) {
    appendAutoLink(content, node, LocalMarkdownTypography.current.linkTextSpanStyle)
}

/**
 * Appends an auto-detected link to the `AnnotatedString.Builder`.
 *
 * @param content The content string.
 * @param node The AST node representing the auto link.
 * @param linkTextStyle The style to apply to the link text.
 */
fun AnnotatedString.Builder.appendAutoLink(
    content: String,
    node: ASTNode,
    linkTextStyle: SpanStyle,
) {
    val targetNode = node.children.firstOrNull {
        it.type.name == MarkdownElementTypes.AUTOLINK.name
    } ?: node
    val destination = targetNode.getUnescapedTextInNode(content)
    pushStringAnnotation(MARKDOWN_TAG_URL, (destination))
    pushStyle(linkTextStyle)
    append(destination)
    pop()
}

@Deprecated("Use the non composable `buildMarkdownAnnotatedString` function instead. This function will be removed in a future release.")
@Composable
fun AnnotatedString.Builder.buildMarkdownAnnotatedString(content: String, node: ASTNode) = buildMarkdownAnnotatedString(
    content = content,
    node = node,
    linkTextStyle = LocalMarkdownTypography.current.linkTextSpanStyle,
    codeStyle = LocalMarkdownTypography.current.codeSpanStyle,
    annotator = LocalMarkdownAnnotator.current
)

/**
 * Builds an [AnnotatedString] with the contents of the given Markdown [ASTNode] node.
 *
 * This method automatically constructs the string with child components like:
 * - Paragraph
 * - Image
 * - Strong
 * - ...
 */
fun AnnotatedString.Builder.buildMarkdownAnnotatedString(
    content: String,
    node: ASTNode,
    linkTextStyle: SpanStyle,
    codeStyle: SpanStyle,
    annotator: MarkdownAnnotator? = null,
) {
    buildMarkdownAnnotatedString(content, node.children, linkTextStyle, codeStyle, annotator)
}

@Deprecated("Use the non composable `buildMarkdownAnnotatedString` function instead. This function will be removed in a future release.")
@Composable
fun AnnotatedString.Builder.buildMarkdownAnnotatedString(content: String, children: List<ASTNode>) = buildMarkdownAnnotatedString(
    content = content,
    children = children,
    linkTextStyle = LocalMarkdownTypography.current.linkTextSpanStyle,
    codeStyle = LocalMarkdownTypography.current.codeSpanStyle,
    annotator = LocalMarkdownAnnotator.current
)

/**
 * Builds an [AnnotatedString] with the contents of the given Markdown [ASTNode] node.
 *
 * This method automatically constructs the string with child components like:
 * - Paragraph
 * - Image
 * - Strong
 * - ...
 */
fun AnnotatedString.Builder.buildMarkdownAnnotatedString(
    content: String,
    children: List<ASTNode>,
    linkTextStyle: SpanStyle,
    codeStyle: SpanStyle,
    annotator: MarkdownAnnotator? = null,
) {
    val annotate = annotator?.annotate
    var skipIfNext: Any? = null
    children.forEach { child ->
        if (skipIfNext == null || skipIfNext != child.type) {
            if (annotate == null || !annotate(content, child)) {
                val parentType = child.parent?.type

                when (child.type) {
                    // Element types
                    MarkdownElementTypes.PARAGRAPH -> buildMarkdownAnnotatedString(content, child, linkTextStyle, codeStyle, annotator)
                    MarkdownElementTypes.IMAGE -> child.findChildOfTypeRecursive(
                        MarkdownElementTypes.LINK_DESTINATION
                    )?.let {
                        appendInlineContent(MARKDOWN_TAG_IMAGE_URL, it.getUnescapedTextInNode(content))
                    }

                    MarkdownElementTypes.EMPH -> {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        buildMarkdownAnnotatedString(content, child, linkTextStyle, codeStyle, annotator)
                        pop()
                    }

                    MarkdownElementTypes.STRONG -> {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        buildMarkdownAnnotatedString(content, child, linkTextStyle, codeStyle, annotator)
                        pop()
                    }

                    GFMElementTypes.STRIKETHROUGH -> {
                        pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                        buildMarkdownAnnotatedString(content, child, linkTextStyle, codeStyle, annotator)
                        pop()
                    }

                    MarkdownElementTypes.CODE_SPAN -> {
                        pushStyle(codeStyle)
                        append(' ')
                        buildMarkdownAnnotatedString(content, child.children.innerList(), linkTextStyle, codeStyle, annotator)
                        append(' ')
                        pop()
                    }

                    MarkdownElementTypes.AUTOLINK -> appendAutoLink(content, child, linkTextStyle)
                    MarkdownElementTypes.INLINE_LINK -> appendMarkdownLink(content, child, linkTextStyle, codeStyle, annotator)
                    MarkdownElementTypes.SHORT_REFERENCE_LINK -> appendMarkdownLink(content, child, linkTextStyle, codeStyle, annotator)
                    MarkdownElementTypes.FULL_REFERENCE_LINK -> appendMarkdownLink(content, child, linkTextStyle, codeStyle, annotator)

                    // Token Types
                    MarkdownTokenTypes.TEXT -> append(child.getUnescapedTextInNode(content))
                    GFMTokenTypes.GFM_AUTOLINK -> if (child.parent == MarkdownElementTypes.LINK_TEXT) {
                        append(child.getUnescapedTextInNode(content))
                    } else appendAutoLink(content, child, linkTextStyle)

                    MarkdownTokenTypes.SINGLE_QUOTE -> append('\'')
                    MarkdownTokenTypes.DOUBLE_QUOTE -> append('\"')
                    MarkdownTokenTypes.LPAREN -> append('(')
                    MarkdownTokenTypes.RPAREN -> append(')')
                    MarkdownTokenTypes.LBRACKET -> append('[')
                    MarkdownTokenTypes.RBRACKET -> append(']')
                    MarkdownTokenTypes.LT -> append('<')
                    MarkdownTokenTypes.GT -> append('>')
                    MarkdownTokenTypes.COLON -> append(':')
                    MarkdownTokenTypes.EXCLAMATION_MARK -> append('!')
                    MarkdownTokenTypes.BACKTICK -> append('`')
                    MarkdownTokenTypes.HARD_LINE_BREAK -> append("\n\n")
                    MarkdownTokenTypes.EMPH -> if (parentType != MarkdownElementTypes.EMPH && parentType != MarkdownElementTypes.STRONG) append('*')
                    MarkdownTokenTypes.EOL -> append('\n')
                    MarkdownTokenTypes.WHITE_SPACE -> if (length > 0) {
                        append(' ')
                    }

                    MarkdownTokenTypes.BLOCK_QUOTE -> {
                        skipIfNext = MarkdownTokenTypes.WHITE_SPACE
                    }
                }
            }
        } else {
            skipIfNext = null
        }
    }
}
