package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.elements.*
import com.mikepenz.markdown.model.*
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ATX_4
import org.intellij.markdown.MarkdownElementTypes.ATX_5
import org.intellij.markdown.MarkdownElementTypes.ATX_6
import org.intellij.markdown.MarkdownElementTypes.BLOCK_QUOTE
import org.intellij.markdown.MarkdownElementTypes.CODE_BLOCK
import org.intellij.markdown.MarkdownElementTypes.CODE_FENCE
import org.intellij.markdown.MarkdownElementTypes.IMAGE
import org.intellij.markdown.MarkdownElementTypes.LINK_DEFINITION
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.PARAGRAPH
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
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
        TEXT -> MarkdownText(getTextInNode(content).toString())
        EOL -> {}
        CODE_FENCE -> MarkdownCodeFence(content, this)
        CODE_BLOCK -> MarkdownCodeBlock(content, this)
        ATX_1 -> MarkdownHeader(content, this, typography.h1)
        ATX_2 -> MarkdownHeader(content, this, typography.h2)
        ATX_3 -> MarkdownHeader(content, this, typography.h3)
        ATX_4 -> MarkdownHeader(content, this, typography.h4)
        ATX_5 -> MarkdownHeader(content, this, typography.h5)
        ATX_6 -> MarkdownHeader(content, this, typography.h6)
        BLOCK_QUOTE -> MarkdownBlockQuote(content, this)
        PARAGRAPH -> MarkdownParagraph(content, this, style = typography.paragraph)
        ORDERED_LIST -> Column(modifier = Modifier) {
            MarkdownOrderedList(content, this@handleElement, style = typography.ordered)
        }

        UNORDERED_LIST -> Column(modifier = Modifier) {
            MarkdownBulletList(content, this@handleElement, style = typography.bullet)
        }

        IMAGE -> MarkdownImage(content, this)
        LINK_DEFINITION -> {
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
