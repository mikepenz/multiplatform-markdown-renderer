package com.mikepenz.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Colors
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.utils.LocalReferenceLinkHandler
import com.mikepenz.markdown.utils.ReferenceLinkHandlerImpl
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser


/**
 * A composable that receives markdown content as [String] and renders it in the UI.
 *
 * @param content The markdown content to render in the UI.
 * @param modifier The modifier to be applied to the Markdown.
 * @param flavour The [MarkdownFlavourDescriptor] used to parse the markdown.
 */
@Composable
fun Markdown(
    content: String,
    modifier: Modifier = Modifier,
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor()
) {
    Column(modifier) {
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)

        CompositionLocalProvider(LocalReferenceLinkHandler provides ReferenceLinkHandlerImpl()) {
            parsedTree.children.forEach { node ->
                if (!node.handleElement(content)) {
                    node.children.forEach { child ->
                        child.handleElement(content)
                    }
                }
            }
        }
    }
}


@Composable
private fun ASTNode.handleElement(content: String): Boolean {
    var handled = true
    when (type) {
        MarkdownTokenTypes.TEXT -> Text(getTextInNode(content).toString())
        MarkdownTokenTypes.EOL -> Spacer(Modifier.padding(4.dp))
        MarkdownElementTypes.CODE_FENCE -> MarkdownCodeFence(content, this)
        MarkdownElementTypes.ATX_1 -> MarkdownHeader(content, this, MaterialTheme.typography.h2)
        MarkdownElementTypes.ATX_2 -> MarkdownHeader(content, this, MaterialTheme.typography.h3)
        MarkdownElementTypes.ATX_3 -> MarkdownHeader(content, this, MaterialTheme.typography.h4)
        MarkdownElementTypes.ATX_4 -> MarkdownHeader(content, this, MaterialTheme.typography.h5)
        MarkdownElementTypes.ATX_5 -> MarkdownHeader(content, this, MaterialTheme.typography.h6)
        MarkdownElementTypes.ATX_6 -> MarkdownHeader(content, this, MaterialTheme.typography.h6)
        MarkdownElementTypes.BLOCK_QUOTE -> MarkdownBlockQuote(content, this)
        MarkdownElementTypes.PARAGRAPH -> MarkdownParagraph(content, this)
        MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(content, this)
        MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(content, this)
        MarkdownElementTypes.IMAGE -> MarkdownImage(content, this)
        MarkdownElementTypes.LINK_DEFINITION -> {
            val linkLabel = findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(content)?.toString()
            if (linkLabel != null) {
                val destination = findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)?.toString()
                LocalReferenceLinkHandler.current.store(linkLabel, destination)
            }
        }
        else -> handled = false
    }
    return handled
}

@Composable
private fun MarkdownCodeFence(content: String, node: ASTNode, modifier: Modifier = Modifier) {
    // CODE_FENCE_START, FENCE_LANG, {content}, CODE_FENCE_END
    // val lang = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG) // unused for now
    val start = node.children[2].startOffset
    val end = node.children[node.children.size - 2].endOffset
    Code(content.subSequence(start, end).trim().toString(), modifier)
}

@Composable
private fun MarkdownHeader(content: String, node: ASTNode, style: TextStyle = LocalTextStyle.current) {
    node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.let {
        Text(
            it.getTextInNode(content).trim().toString(),
            style = style,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Composable
private fun MarkdownBlockQuote(content: String, node: ASTNode, modifier: Modifier = Modifier) {
    val color = MaterialTheme.colors.onBackground
    Box(modifier = modifier
        .drawBehind {
            drawLine(
                color = color,
                strokeWidth = 2f,
                start = Offset(12.dp.value, 0f),
                end = Offset(12.dp.value, size.height)
            )
        }
        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
        val text = buildAnnotatedString {
            pushStyle(MaterialTheme.typography.body1.toSpanStyle().plus(SpanStyle(fontStyle = FontStyle.Italic)))
            append(node.getTextInNode(content).toString())
            pop()
        }
        Text(text, modifier)
    }
}

@Composable
private fun MarkdownParagraph(content: String, node: ASTNode) {
    val styledText = buildAnnotatedString {
        pushStyle(MaterialTheme.typography.body1.toSpanStyle())
        buildMarkdownAnnotatedString(content, node, MaterialTheme.colors)
        pop()
    }

    MarkdownText(styledText, style = MaterialTheme.typography.body1)
}

private fun AnnotatedString.Builder.appendMarkdownLink(content: String, node: ASTNode, colors: Colors) {
    val linkText = node.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.children?.innerList() ?: return
    val destination = node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)?.toString()
    val linkLabel = node.findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(content)?.toString()
    (destination ?: linkLabel)?.let { pushStringAnnotation(TAG_URL, it) }
    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold))
    buildMarkdownAnnotatedString(content, linkText, colors)
    pop()
}

private fun AnnotatedString.Builder.appendAutoLink(content: String, node: ASTNode, colors: Colors) {
    val destination = node.getTextInNode(content).toString()
    pushStringAnnotation(TAG_URL, (destination))
    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold))
    append(destination)
    pop()
}

private fun AnnotatedString.Builder.buildMarkdownAnnotatedString(content: String, node: ASTNode, colors: Colors) {
    buildMarkdownAnnotatedString(content, node.children, colors)
}

private fun AnnotatedString.Builder.buildMarkdownAnnotatedString(content: String, children: List<ASTNode>, colors: Colors) {
    children.forEach { child ->
        when (child.type) {
            MarkdownElementTypes.PARAGRAPH -> buildMarkdownAnnotatedString(content, child, colors)
            MarkdownElementTypes.IMAGE -> child.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.let {
                appendInlineContent(TAG_IMAGE_URL, it.getTextInNode(content).toString())
            }
            MarkdownElementTypes.EMPH -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                buildMarkdownAnnotatedString(content, child, colors)
                pop()
            }
            MarkdownElementTypes.STRONG -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                buildMarkdownAnnotatedString(content, child, colors)
                pop()
            }
            MarkdownElementTypes.CODE_SPAN -> {
                pushStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = colors.onBackground.copy(alpha = 0.1f)))
                append(' ')
                buildMarkdownAnnotatedString(content, child.children.innerList(), colors)
                append(' ')
                pop()
            }
            MarkdownElementTypes.AUTOLINK -> appendAutoLink(content, child, colors)
            MarkdownElementTypes.INLINE_LINK -> appendMarkdownLink(content, child, colors)
            MarkdownElementTypes.SHORT_REFERENCE_LINK -> appendMarkdownLink(content, child, colors)
            MarkdownElementTypes.FULL_REFERENCE_LINK -> appendMarkdownLink(content, child, colors)
            MarkdownTokenTypes.TEXT -> append(child.getTextInNode(content).toString())
            GFMTokenTypes.GFM_AUTOLINK -> if (child.parent == MarkdownElementTypes.LINK_TEXT) {
                append(child.getTextInNode(content).toString())
            } else appendAutoLink(content, child, colors)
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
            MarkdownTokenTypes.EOL -> append('\n')
            MarkdownTokenTypes.WHITE_SPACE -> append(' ')
        }
    }
}

@Composable
private fun MarkdownImage(content: String, node: ASTNode) {
    val link = node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)?.toString() ?: return

    Spacer(Modifier.padding(4.dp))

    imagePainter(link)?.let { painter ->
        Image(
            painter = painter,
            contentDescription = "Image", // TODO
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(Modifier.padding(4.dp))
}

@Composable
private fun MarkdownText(text: AnnotatedString, style: TextStyle, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(text = text,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { pos ->
                layoutResult.value?.let { layoutResult ->
                    val position = layoutResult.getOffsetForPosition(pos)
                    text.getStringAnnotations(position, position)
                        .firstOrNull { a -> a.tag == TAG_URL }
                        ?.let { a ->
                            uriHandler.openUri(referenceLinkHandler.find(a.item))
                        }
                }
            }
        },
        style = style,
        inlineContent = mapOf(
            TAG_IMAGE_URL to InlineTextContent(
                Placeholder(180.sp, 180.sp, PlaceholderVerticalAlign.Bottom) // TODO, identify flexible scaling!
            ) {
                Spacer(Modifier.padding(4.dp))

                imagePainter(it)?.let { painter ->
                    Image(
                        painter = painter,
                        contentDescription = "Image", // TODO
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.padding(4.dp))
            }
        ),
        onTextLayout = { layoutResult.value = it }
    )
}

@Composable
internal expect fun imagePainter(url: String): Painter?

@Composable
private fun MarkdownBulletList(content: String, node: ASTNode, modifier: Modifier = Modifier, level: Int = 0) {
    MarkdownListItems(content, node, modifier, level) { child ->
        Row(Modifier.fillMaxWidth()) {
            Text("${child.findChildOfType(MarkdownTokenTypes.LIST_BULLET)?.getTextInNode(content)} ")
            val text = buildAnnotatedString {
                pushStyle(MaterialTheme.typography.body1.toSpanStyle())
                buildMarkdownAnnotatedString(content, child, MaterialTheme.colors)
                pop()
            }
            MarkdownText(text, MaterialTheme.typography.body1, modifier.padding(bottom = 4.dp))
        }
    }
}

@Composable
private fun MarkdownOrderedList(content: String, node: ASTNode, modifier: Modifier = Modifier, level: Int = 0) {
    MarkdownListItems(content, node, modifier, level) { child ->
        Row(Modifier.fillMaxWidth()) {
            Text("${child.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)?.getTextInNode(content)} ")
            val text = buildAnnotatedString {
                pushStyle(MaterialTheme.typography.body1.toSpanStyle())
                buildMarkdownAnnotatedString(content, child, MaterialTheme.colors)
                pop()
            }
            MarkdownText(text, MaterialTheme.typography.body1, modifier.padding(bottom = 4.dp))
        }
    }
}

@Composable
private fun MarkdownListItems(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    level: Int = 0,
    item: @Composable (child: ASTNode) -> Unit
) {
    Column(modifier = modifier.padding(top = 8.dp, bottom = 8.dp, start = (8.dp) * level)) {
        node.children.forEach { child ->
            when (child.type) {
                MarkdownElementTypes.LIST_ITEM -> item(child)
                MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(content, child, modifier, level + 1)
                MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(content, child, modifier, level + 1)
            }
        }
    }
}