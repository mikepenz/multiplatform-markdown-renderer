package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.elements.MarkdownBlockQuote
import com.mikepenz.markdown.compose.elements.MarkdownBulletList
import com.mikepenz.markdown.compose.elements.MarkdownCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.compose.elements.MarkdownHeader
import com.mikepenz.markdown.compose.elements.MarkdownImage
import com.mikepenz.markdown.compose.elements.MarkdownOrderedList
import com.mikepenz.markdown.compose.elements.MarkdownParagraph
import com.mikepenz.markdown.compose.elements.MarkdownText
import com.mikepenz.markdown.model.*
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

@Composable
fun Markdown(
    content: String,
    colors: MarkdownColors = markdownColor(),
    typography: MarkdownTypography = markdownTypography(),
    padding: MarkdownPadding = markdownPadding(),
    modifier: Modifier = Modifier.fillMaxSize(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor()
) {
    CompositionLocalProvider(
        LocalReferenceLinkHandler provides ReferenceLinkHandlerImpl(),
        LocalMarkdownPadding provides padding,
        LocalMarkdownColors provides colors,
        LocalMarkdownTypography provides typography,
    ) {
        Column(modifier) {
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
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
    val typography = LocalMarkdownTypography.current
    var handled = true
    Spacer(Modifier.height(LocalMarkdownPadding.current.block))
    when (type) {
        MarkdownTokenTypes.TEXT -> MarkdownText(getTextInNode(content).toString())
        MarkdownTokenTypes.EOL -> {}
        MarkdownElementTypes.CODE_FENCE -> MarkdownCodeFence(content, this)
        MarkdownElementTypes.CODE_BLOCK -> MarkdownCodeBlock(content, this)
        MarkdownElementTypes.ATX_1 -> MarkdownHeader(content, this, typography.h1)
        MarkdownElementTypes.ATX_2 -> MarkdownHeader(content, this, typography.h2)
        MarkdownElementTypes.ATX_3 -> MarkdownHeader(content, this, typography.h3)
        MarkdownElementTypes.ATX_4 -> MarkdownHeader(content, this, typography.h4)
        MarkdownElementTypes.ATX_5 -> MarkdownHeader(content, this, typography.h5)
        MarkdownElementTypes.ATX_6 -> MarkdownHeader(content, this, typography.h6)
        MarkdownElementTypes.BLOCK_QUOTE -> MarkdownBlockQuote(content, this)
        MarkdownElementTypes.PARAGRAPH -> MarkdownParagraph(content, this, style = typography.paragraph)
        MarkdownElementTypes.ORDERED_LIST -> Column(modifier = Modifier) {
            MarkdownOrderedList(content, this@handleElement, style = typography.ordered)
        }
        MarkdownElementTypes.UNORDERED_LIST -> Column(modifier = Modifier) {
            MarkdownBulletList(content, this@handleElement, style = typography.bullet)
        }
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
