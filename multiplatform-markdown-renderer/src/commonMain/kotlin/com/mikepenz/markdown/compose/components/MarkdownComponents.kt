package com.mikepenz.markdown.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.LocalReferenceLinkHandler
import com.mikepenz.markdown.compose.elements.*
import com.mikepenz.markdown.model.MarkdownTypography
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode

typealias MarkdownComponent = @Composable ColumnScope.(MarkdownComponentModel) -> Unit

typealias CustomMarkdownComponent = @Composable ColumnScope.(IElementType, MarkdownComponentModel) -> Unit

/**
 * Model holding data relevant for a component
 */
data class MarkdownComponentModel(
    val content: String,
    val node: ASTNode,
    val typography: MarkdownTypography,
)

private fun MarkdownComponentModel.getTextInNode() = node.getTextInNode(content)

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
    setextHeading1: MarkdownComponent = CurrentComponentsBridge.setextHeading1,
    setextHeading2: MarkdownComponent = CurrentComponentsBridge.setextHeading2,
    blockQuote: MarkdownComponent = CurrentComponentsBridge.blockQuote,
    paragraph: MarkdownComponent = CurrentComponentsBridge.paragraph,
    orderedList: MarkdownComponent = CurrentComponentsBridge.orderedList,
    unorderedList: MarkdownComponent = CurrentComponentsBridge.unorderedList,
    image: MarkdownComponent = CurrentComponentsBridge.image,
    linkDefinition: MarkdownComponent = CurrentComponentsBridge.linkDefinition,
    horizontalRule: MarkdownComponent = CurrentComponentsBridge.horizontalRule,
    custom: CustomMarkdownComponent? = CurrentComponentsBridge.custom,
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
    setextHeading1 = setextHeading1,
    setextHeading2 = setextHeading2,
    blockQuote = blockQuote,
    paragraph = paragraph,
    orderedList = orderedList,
    unorderedList = unorderedList,
    image = image,
    linkDefinition = linkDefinition,
    horizontalRule = horizontalRule,
    custom = custom,
)

/**
 * Interface defining all supported components.
 */
@Stable
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
    val setextHeading1: MarkdownComponent
    val setextHeading2: MarkdownComponent
    val blockQuote: MarkdownComponent
    val paragraph: MarkdownComponent
    val orderedList: MarkdownComponent
    val unorderedList: MarkdownComponent
    val image: MarkdownComponent
    val linkDefinition: MarkdownComponent
    val horizontalRule: MarkdownComponent
    val custom: CustomMarkdownComponent?
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
    override val setextHeading1: MarkdownComponent,
    override val setextHeading2: MarkdownComponent,
    override val blockQuote: MarkdownComponent,
    override val paragraph: MarkdownComponent,
    override val orderedList: MarkdownComponent,
    override val unorderedList: MarkdownComponent,
    override val image: MarkdownComponent,
    override val linkDefinition: MarkdownComponent,
    override val horizontalRule: MarkdownComponent,
    override val custom: CustomMarkdownComponent?,
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
    val setextHeading1: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h1, MarkdownTokenTypes.SETEXT_CONTENT)
    }
    val setextHeading2: MarkdownComponent = {
        MarkdownHeader(it.content, it.node, it.typography.h2, MarkdownTokenTypes.SETEXT_CONTENT)
    }
    val blockQuote: MarkdownComponent = {
        MarkdownBlockQuote(it.content, it.node)
    }
    val paragraph: MarkdownComponent = {
        MarkdownParagraph(it.content, it.node, style = it.typography.paragraph)
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
        val linkLabel =
            it.node.findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(it.content)
                ?.toString()
        if (linkLabel != null) {
            val destination = it.node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                ?.getTextInNode(it.content)?.toString()
            LocalReferenceLinkHandler.current.store(linkLabel, destination)
        }
    }
    val horizontalRule: MarkdownComponent = {
        MarkdownDivider(Modifier.fillMaxWidth())
    }
    val custom: CustomMarkdownComponent? = null
}