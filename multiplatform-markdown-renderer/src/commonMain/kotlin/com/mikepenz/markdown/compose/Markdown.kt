package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.ImageTransformerImpl
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.markdownColor
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.model.markdownTypography
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
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    imageTransformer: ImageTransformer = ImageTransformerImpl(),
    components: MarkdownComponents = markdownComponents(),
) {
    CompositionLocalProvider(
        LocalReferenceLinkHandler provides ReferenceLinkHandlerImpl(),
        LocalMarkdownPadding provides padding,
        LocalMarkdownColors provides colors,
        LocalMarkdownTypography provides typography,
        LocalImageTransformer provides imageTransformer
    ) {
        Column(modifier) {
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
            parsedTree.children.forEach { node ->
                if (!node.handleElement(components, content)) {
                    node.children.forEach { child ->
                        child.handleElement(components, content)
                    }
                }
            }
        }
    }
}

@Composable
private fun ASTNode.handleElement(components: MarkdownComponents, content: String): Boolean {
    val model = MarkdownComponentModel(
        content = content,
        node = this,
        typography = LocalMarkdownTypography.current
    )
    var handled = true
    Spacer(Modifier.height(LocalMarkdownPadding.current.block))
    when (type) {
        TEXT -> components.text(model)
        EOL -> components.eol(model)
        CODE_FENCE -> components.codeFence(model)
        CODE_BLOCK -> components.codeBlock(model)
        ATX_1 -> components.heading1(model)
        ATX_2 -> components.heading2(model)
        ATX_3 -> components.heading3(model)
        ATX_4 -> components.heading4(model)
        ATX_5 -> components.heading5(model)
        ATX_6 -> components.heading6(model)
        BLOCK_QUOTE -> components.blockQuote(model)
        PARAGRAPH -> components.paragraph(model)
        ORDERED_LIST -> components.orderedList(model)
        UNORDERED_LIST -> components.unorderedList(model)
        IMAGE -> components.image(model)
        LINK_DEFINITION -> components.linkDefinition(model)
        else -> {
            handled = components.custom?.invoke(type, model) != null
        }
    }

    return handled
}