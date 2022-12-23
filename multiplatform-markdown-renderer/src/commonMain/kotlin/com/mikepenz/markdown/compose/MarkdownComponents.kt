package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.elements.*
import com.mikepenz.markdown.compose.elements.MarkdownBlockQuote
import com.mikepenz.markdown.compose.elements.MarkdownCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.compose.elements.MarkdownHeader
import com.mikepenz.markdown.compose.elements.MarkdownParagraph
import com.mikepenz.markdown.compose.elements.MarkdownText
import com.mikepenz.markdown.model.MarkdownTypography
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode

typealias MarkdownComponent = @Composable (MarkdownComponentModel) -> Unit

data class MarkdownComponentModel(
    val content: String,
    val node: ASTNode,
    val typography: MarkdownTypography,
)

fun MarkdownComponentModel.getTextInNode() = node.getTextInNode(content)

@Composable
fun markdownComponents(
    text: MarkdownComponent = CurrentComponentsBridge.text,
    eol: MarkdownComponent = CurrentComponentsBridge.eol,
    codeFence: MarkdownComponent = CurrentComponentsBridge.codeFence,
    codeBlock: MarkdownComponent = CurrentComponentsBridge.codeBlock,
    heading1: MarkdownComponent = CurrentComponentsBridge.heading1,
    heading2: MarkdownComponent = CurrentComponentsBridge.heading2,
    heading3: MarkdownComponent = CurrentComponentsBridge.heading3,
    heading4: MarkdownComponent = CurrentComponentsBridge.heading4,
    heading5: MarkdownComponent = CurrentComponentsBridge.heading5,
    heading6: MarkdownComponent = CurrentComponentsBridge.heading6,
    blockQuote: MarkdownComponent = CurrentComponentsBridge.blockQuote,
    paragraph: MarkdownComponent = CurrentComponentsBridge.paragraph,
    orderedList: MarkdownComponent = CurrentComponentsBridge.orderedList,
    unorderedList: MarkdownComponent = CurrentComponentsBridge.unorderedList,
    image: MarkdownComponent = CurrentComponentsBridge.image,
    linkDefinition: MarkdownComponent = CurrentComponentsBridge.linkDefinition,
): MarkdownComponents = DefaultMarkdownComponents(
    text = text,
    eol = eol,
    codeFence = codeFence,
    codeBlock = codeBlock,
    heading1 = heading1,
    heading2 = heading2,
    heading3 = heading3,
    heading4 = heading4,
    heading5 = heading5,
    heading6 = heading6,
    blockQuote = blockQuote,
    paragraph = paragraph,
    orderedList = orderedList,
    unorderedList = unorderedList,
    image = image,
    linkDefinition = linkDefinition,
)


interface MarkdownComponents {
    val text: MarkdownComponent
    val eol: MarkdownComponent
    val codeFence: MarkdownComponent
    val codeBlock: MarkdownComponent
    val heading1: MarkdownComponent
    val heading2: MarkdownComponent
    val heading3: MarkdownComponent
    val heading4: MarkdownComponent
    val heading5: MarkdownComponent
    val heading6: MarkdownComponent
    val blockQuote: MarkdownComponent
    val paragraph: MarkdownComponent
    val orderedList: MarkdownComponent
    val unorderedList: MarkdownComponent
    val image: MarkdownComponent
    val linkDefinition: MarkdownComponent
}

private class DefaultMarkdownComponents(
    override val text: MarkdownComponent,
    override val eol: MarkdownComponent,
    override val codeFence: MarkdownComponent,
    override val codeBlock: MarkdownComponent,
    override val heading1: MarkdownComponent,
    override val heading2: MarkdownComponent,
    override val heading3: MarkdownComponent,
    override val heading4: MarkdownComponent,
    override val heading5: MarkdownComponent,
    override val heading6: MarkdownComponent,
    override val blockQuote: MarkdownComponent,
    override val paragraph: MarkdownComponent,
    override val orderedList: MarkdownComponent,
    override val unorderedList: MarkdownComponent,
    override val image: MarkdownComponent,
    override val linkDefinition: MarkdownComponent,
) : MarkdownComponents


/**
 * Adapts the universal signature @Composable (MarkdownComponentModel) -> Unit to the existing components.
 */
object CurrentComponentsBridge {
    val text: MarkdownComponent = {
        MarkdownText(it.getTextInNode().toString())
    }
    val eol: MarkdownComponent = { }
    val codeFence: MarkdownComponent = {
        MarkdownCodeFence(it.content, it.node)
    }
    val codeBlock: MarkdownComponent = {
        MarkdownCodeBlock(it.content, it.node)
    }
    val heading1: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h1)
    }
    val heading2: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h2)
    }
    val heading3: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h3)
    }
    val heading4: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h4)
    }
    val heading5: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h5)
    }
    val heading6: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h6)
    }
    val blockQuote: MarkdownComponent = {
        MarkdownBlockQuote(it.content, it.node)
    }
    val paragraph: MarkdownComponent = {
        MarkdownParagraph(it.content, it.node, it.typography.paragraph)
    }
    val orderedList: MarkdownComponent = {
        Column(modifier = Modifier) {
            MarkdownOrderedList(it.content, it.node, style = it.typography.ordered)
        }
    }
    val unorderedList: MarkdownComponent = {
        Column(modifier = Modifier) {
            MarkdownBulletList(it.content, it.node, style = it.typography.bullet)
        }
    }
    val image: MarkdownComponent = {
        MarkdownImage(it.content, it.node)
    }
    val linkDefinition: MarkdownComponent = {
        val linkLabel = it.node
            .findChildOfType(MarkdownElementTypes.LINK_LABEL)
            ?.getTextInNode(it.content)
            ?.toString()
        if (linkLabel != null) {
            val destination =
                it.node
                    .findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                    ?.getTextInNode(it.content)
                    ?.toString()
            LocalReferenceLinkHandler.current.store(linkLabel, destination)
        }
    }
}