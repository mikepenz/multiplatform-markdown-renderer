package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.MarkdownAnimations
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownDimens
import com.mikepenz.markdown.model.MarkdownExtendedSpans
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownState
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.NoOpImageTransformerImpl
import com.mikepenz.markdown.model.ReferenceLinkHandler
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownDimens
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.model.rememberMarkdownState
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
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
 * @param referenceLinkHandler The reference link handler to be used for handling links.
 * @param animations The animations to be used for rendering.
 * @param loading A composable function to be displayed while loading the content.
 * @param success A composable function to be displayed with the markdown content. It receives the modifier, state and components as parameters. By default this is a [Column].
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
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
) {
    val state = rememberMarkdownState(
        content = content,
        flavour = flavour,
        parser = parser,
        referenceLinkHandler = referenceLinkHandler,
    )

    Markdown(
        state = state,
        colors = colors,
        typography = typography,
        modifier = modifier,
        padding = padding,
        dimens = dimens,
        imageTransformer = imageTransformer,
        annotator = annotator,
        extendedSpans = extendedSpans,
        components = components,
        animations = animations,
        loading = loading,
        success = success,
        error = error
    )
}

/**
 * Renders the markdown content.
 *
 * @param state The markdown state to be rendered.
 * @param colors The colors to be used for rendering.
 * @param typography The typography to be used for rendering.
 * @param modifier The modifier to be applied to the container.
 * @param padding The padding to be applied to the container.
 * @param dimens The dimensions to be used for rendering.
 * @param imageTransformer The image transformer to be used for rendering images.
 * @param annotator The annotator to be used for rendering annotations.
 * @param extendedSpans The extended spans to be used for rendering.
 * @param components The components to be used for rendering.
 * @param animations The animations to be used for rendering.
 * @param loading A composable function to be displayed while loading the content.
 * @param success A composable function to be displayed with the markdown content. It receives the modifier, state and components as parameters. By default this is a [Column].
 * @param error A composable function to be displayed in case of an error. Only really possible if assertions are enabled on the parser)
 */
@Composable
fun Markdown(
    state: MarkdownState,
    colors: MarkdownColors,
    typography: MarkdownTypography,
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) {} },
) {
    val markdownState by state.state.collectAsState()
    CompositionLocalProvider(
        LocalReferenceLinkHandler provides markdownState.referenceLinkHandler,
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
        when (val markdown = markdownState) {
            is State.Error -> error(modifier)
            is State.Loading -> loading(modifier)
            is State.Success -> success(markdown, components, modifier)
        }
    }
}

/**
 * Renders the parsed markdown content.
 *
 * @param state The success markdown state.
 * @param components The MarkdownComponents instance containing the components to use.
 * @param modifier The modifier to be applied to the container.
 */
@Composable
fun MarkdownSuccess(
    state: State.Success,
    components: MarkdownComponents,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        state.node.children.forEach { node ->
            if (!handleElement(node, components, state.content, skipLinkDefinition = state.linksLookedUp)) {
                node.children.forEach { child ->
                    handleElement(child, components, state.content, skipLinkDefinition = state.linksLookedUp)
                }
            }
        }
    }
}