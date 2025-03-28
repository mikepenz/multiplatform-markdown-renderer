package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.MarkdownAnimations
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownDimens
import com.mikepenz.markdown.model.MarkdownExtendedSpans
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownResult
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.NoOpImageTransformerImpl
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownDimens
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.markdownPadding
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
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.HORIZONTAL_RULE
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMElementTypes.TABLE
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

/**
 * Renders the markdown content.
 *
 * @param content The markdown content to be rendered.
 * @param colors The colors to be used for rendering.
 * @param typography The typography to be used for rendering.
 * @param modifier The modifier to be applied to the container.
 * @param padding The padding to be applied to the container.
 * @param dimens The dimensions to be used for rendering.
 * @param flavour The flavour descriptor for parsing the markdown. By default uses GFM flavour.
 * @param parser The parser to be used for parsing the markdown. By default uses the flavour supplied.
 * @param imageTransformer The image transformer to be used for rendering images.
 * @param annotator The annotator to be used for rendering annotations.
 * @param extendedSpans The extended spans to be used for rendering.
 * @param components The components to be used for rendering.
 * @param animations The animations to be used for rendering.
 * @param immediate If true, the content will be parsed immediately. Otherwise, it will be parsed asynchronously, and show a loading and error state.
 * @param loading A composable function to be displayed while loading the content.
 * @param error A composable function to be displayed in case of an error. Only really possible if assertions are enabled on the parser)
 */
@Composable
fun Markdown(
    content: String,
    colors: MarkdownColors,
    typography: MarkdownTypography,
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    immediate: Boolean = false,
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
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
        val result = if (immediate) {
            remember(content, flavour, parser) { MarkdownResult.Success(parser.buildMarkdownTreeFromString(content)) }
        } else {
            val markdownResult by parseMarkdown(content = content, flavour = flavour)
            markdownResult
        }

        when (result) {
            is MarkdownResult.Error -> error(modifier)
            is MarkdownResult.Loading -> loading(modifier)
            is MarkdownResult.Success -> MarkdownSuccess(content = content, node = result.result, components = components, modifier = modifier)
        }
    }
}

/**
 * Renders the parsed markdown content.
 *
 * @param content The original markdown content.
 * @param node The ASTNode representing the parsed markdown.
 * @param components The MarkdownComponents instance containing the components to use.
 */
@Composable
fun MarkdownSuccess(
    content: String,
    node: ASTNode,
    components: MarkdownComponents,
    modifier: Modifier = Modifier,
) {
    val skipLinkDefinition = false
    Column(modifier) {
        node.children.forEach { node ->
            if (!handleElement(node, components, content, skipLinkDefinition)) {
                node.children.forEach { child ->
                    handleElement(child, components, content, skipLinkDefinition)
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
    skipLinkDefinition: Boolean = true,
): Boolean {
    val model = MarkdownComponentModel(
        content = content,
        node = node,
        typography = LocalMarkdownTypography.current,
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
        LINK_DEFINITION -> if (!skipLinkDefinition) components.linkDefinition(this@handleElement, model)
        HORIZONTAL_RULE -> components.horizontalRule(this@handleElement, model)
        TABLE -> components.table(this@handleElement, model)
        else -> {
            handled = components.custom?.invoke(this@handleElement, node.type, model) != null
        }
    }

    if (!handled) {
        node.children.forEach { child ->
            handleElement(child, components, content, includeSpacer, skipLinkDefinition)
        }
    }

    return handled
}

/**
 * Handles the link definition node.
 * This is a recursive function that traverses the AST tree and runs the link definition component.
 *
 * @param node The ASTNode representing the link definition.
 * @param components The MarkdownComponents instance containing the components to use.
 * @param content The original markdown content.
 */
@Composable
internal fun ColumnScope.handleLinkDefinition(
    node: ASTNode,
    components: MarkdownComponents,
    content: String,
) {
    val model = MarkdownComponentModel(
        content = content,
        node = node,
        typography = LocalMarkdownTypography.current,
    )
    if (node.type == LINK_DEFINITION) {
        components.linkDefinition(this@handleLinkDefinition, model)
    } else {
        node.children.forEach { child -> handleLinkDefinition(child, components, content) }
    }
}

/**
 * Parses the given markdown content and returns a [State] of [MarkdownResult].
 */
@Composable
fun parseMarkdown(
    content: String,
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
): State<MarkdownResult> {
    // Creates a State<T> with Result.Loading as initial value
    // If content or flavour changes, the running producer will cancel and re-launch.
    return produceState<MarkdownResult>(initialValue = MarkdownResult.Loading, content, flavour, parser) {
        // Update State with either an Error or Success result. This will trigger a recomposition where this State is read
        value = try {
            MarkdownResult.Success(parser.buildMarkdownTreeFromString(content))
        } catch (_: Throwable) {
            MarkdownResult.Error
        }
    }
}