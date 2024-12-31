package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.*
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
import org.intellij.markdown.MarkdownElementTypes.SETEXT_1
import org.intellij.markdown.MarkdownElementTypes.SETEXT_2
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.TABLE
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.HORIZONTAL_RULE
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

@Composable
fun Markdown(
    content: String,
    colors: MarkdownColors,
    typography: MarkdownTypography,
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
) {
    CompositionLocalProvider(
        LocalReferenceLinkHandler provides ReferenceLinkHandlerImpl(),
        LocalMarkdownPadding provides padding,
        LocalMarkdownDimens provides dimens,
        LocalMarkdownColors provides colors,
        LocalMarkdownTypography provides typography,
        LocalImageTransformer provides imageTransformer,
        LocalMarkdownAnnotator provides annotator,
        LocalMarkdownExtendedSpans provides extendedSpans,
        LocalMarkdownComponents provides components,
        LocalMarkdownAnimations provides animations,
    ) {
        Column(modifier) {
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
            parsedTree.children.forEach { node ->
                if (!handleElement(node, components, content)) {
                    node.children.forEach { child ->
                        handleElement(child, components, content)
                    }
                }
            }
        }
    }
}

@Composable
internal fun ColumnScope.handleElement(
    node: ASTNode,
    components: MarkdownComponents,
    content: String,
    includeSpacer: Boolean = true,
): Boolean {
    val model = MarkdownComponentModel(
        content = content,
        node = node,
        typography = LocalMarkdownTypography.current
    )
    var handled = true
    if (includeSpacer) Spacer(Modifier.height(LocalMarkdownPadding.current.block))
    when (node.type) {
        TEXT -> components.text(this@handleElement, model)
        EOL -> components.eol(this@handleElement, model)
        CODE_FENCE -> components.codeFence(this@handleElement, model)
        CODE_BLOCK -> components.codeBlock(this@handleElement, model)
        ATX_1 -> components.heading1(this@handleElement, model)
        ATX_2 -> components.heading2(this@handleElement, model)
        ATX_3 -> components.heading3(this@handleElement, model)
        ATX_4 -> components.heading4(this@handleElement, model)
        ATX_5 -> components.heading5(this@handleElement, model)
        ATX_6 -> components.heading6(this@handleElement, model)
        SETEXT_1 -> components.setextHeading1(this@handleElement, model)
        SETEXT_2 -> components.setextHeading2(this@handleElement, model)
        BLOCK_QUOTE -> components.blockQuote(this@handleElement, model)
        PARAGRAPH -> components.paragraph(this@handleElement, model)
        ORDERED_LIST -> components.orderedList(this@handleElement, model)
        UNORDERED_LIST -> components.unorderedList(this@handleElement, model)
        IMAGE -> components.image(this@handleElement, model)
        LINK_DEFINITION -> components.linkDefinition(this@handleElement, model)
        HORIZONTAL_RULE -> components.horizontalRule(this@handleElement, model)
        TABLE -> components.table(this@handleElement, model)
        else -> {
            handled = components.custom?.invoke(this@handleElement, node.type, model) != null
        }
    }

    if (!handled) {
        node.children.forEach { child ->
            handleElement(child, components, content, includeSpacer)
        }
    }

    return handled
}
