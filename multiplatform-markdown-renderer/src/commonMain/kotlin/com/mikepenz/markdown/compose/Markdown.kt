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
import com.mikepenz.markdown.model.MarkdownInlineContent
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
import com.mikepenz.markdown.model.markdownInlineContent
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.model.rememberMarkdownState
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser


/**
 * Renders the provided markdown content string using Jetpack Compose.
 *
 * This is the primary entry point for rendering markdown content in your application.
 * It handles parsing the markdown string, applying styling, and rendering the content
 * with appropriate components.
 *
 * The rendering process follows these steps:
 * 1. Parse the markdown content using the provided parser
 * 2. Create a state object to manage the rendering process
 * 3. Apply styling and customization options
 * 4. Render the content using the appropriate components
 *
 * Example usage:
 * ```
 * Markdown(
 *     content = "# Hello World\n\nThis is a **bold** statement.",
 *     colors = markdownColor(),
 *     typography = markdownTypography()
 * )
 * ```
 *
 * @param content The markdown content string to be rendered.
 * @param colors The color scheme to be used for rendering different markdown elements.
 * @param typography The typography settings to be used for text rendering.
 * @param modifier The modifier to be applied to the container. Defaults to [Modifier.fillMaxSize].
 * @param padding The padding configuration to be applied to different markdown elements.
 * @param dimens The dimension settings for spacing and sizing of markdown elements.
 * @param flavour The markdown flavor descriptor for parsing. By default uses GitHub Flavored Markdown (GFM).
 * @param parser The parser instance to be used for parsing the markdown. By default creates a new parser with the provided flavor.
 * @param imageTransformer The transformer for handling and loading images within markdown content.
 * @param annotator The annotator for adding custom annotations to the rendered content.
 * @param extendedSpans The configuration for extended text spans beyond standard markdown.
 * @param inlineContent The configuration for inline custom content within markdown.
 * @param components The custom components to be used for rendering different markdown elements.
 * @param referenceLinkHandler The handler for resolving reference-style links in markdown.
 * @param animations The animation configurations for interactive elements.
 * @param loading A composable function to be displayed while the content is being parsed and prepared.
 * @param success A composable function to be displayed with the successfully parsed markdown content.
 *                It receives the state, components, and modifier as parameters.
 *                By default, this renders the content in a [Column].
 * @param error A composable function to be displayed in case of parsing errors.
 *              This is only triggered if assertions are enabled on the parser.
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
    inlineContent: MarkdownInlineContent = markdownInlineContent(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
) {
    val markdownState = rememberMarkdownState(
        content = content,
        flavour = flavour,
        parser = parser,
        referenceLinkHandler = referenceLinkHandler,
    )

    Markdown(
        markdownState = markdownState,
        colors = colors,
        typography = typography,
        modifier = modifier,
        padding = padding,
        dimens = dimens,
        imageTransformer = imageTransformer,
        annotator = annotator,
        extendedSpans = extendedSpans,
        inlineContent = inlineContent,
        components = components,
        animations = animations,
        loading = loading,
        success = success,
        error = error
    )
}

/**
 * Renders markdown content using a pre-created [MarkdownState] object.
 *
 * This overload is useful when you need more control over the state management
 * of your markdown content, such as when you want to:
 * - Pre-parse markdown content
 * - Share the same state across multiple composables
 * - Implement custom state handling logic
 *
 * The state object manages the parsing and preparation of the markdown content,
 * while this function handles the rendering with appropriate styling and components.
 *
 * Example usage:
 * ```
 * val markdownState = rememberMarkdownState(content = "# Hello World")
 *
 * Markdown(
 *     markdownState = markdownState,
 *     colors = markdownColor(),
 *     typography = markdownTypography()
 * )
 * ```
 *
 * @param markdownState A [MarkdownState] object that manages the parsing and state of the markdown content.
 * @param colors The color scheme to be used for rendering different markdown elements.
 * @param typography The typography settings to be used for text rendering.
 * @param modifier The modifier to be applied to the container. Defaults to [Modifier.fillMaxSize].
 * @param padding The padding configuration to be applied to different markdown elements.
 * @param dimens The dimension settings for spacing and sizing of markdown elements.
 * @param imageTransformer The transformer for handling and loading images within markdown content.
 * @param annotator The annotator for adding custom annotations to the rendered content.
 * @param extendedSpans The configuration for extended text spans beyond standard markdown.
 * @param inlineContent The configuration for inline custom content within markdown.
 * @param components The custom components to be used for rendering different markdown elements.
 * @param animations The animation configurations for interactive elements.
 * @param loading A composable function to be displayed while the content is being parsed and prepared.
 * @param success A composable function to be displayed with the successfully parsed markdown content.
 *                It receives the state, components, and modifier as parameters.
 *                By default, this renders the content in a [Column].
 * @param error A composable function to be displayed in case of parsing errors.
 *              This is only triggered if assertions are enabled on the parser.
 *
 * @see rememberMarkdownState
 */
@Composable
fun Markdown(
    markdownState: MarkdownState,
    colors: MarkdownColors,
    typography: MarkdownTypography,
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    inlineContent: MarkdownInlineContent = markdownInlineContent(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
) {
    val state by markdownState.state.collectAsState()
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
        inlineContent = inlineContent,
        components = components,
        animations = animations,
        loading = loading,
        success = success,
        error = error
    )
}


/**
 * Renders markdown content using a [State] object directly.
 *
 * This is the lowest-level overload of the Markdown composable, providing direct control
 * over the rendering state. It's primarily used internally by the other overloads but
 * can be useful for advanced use cases where you need to:
 * - Implement custom state transitions
 * - Handle specific loading/error states manually
 * - Integrate with custom state management systems
 *
 * The function handles different states (Loading, Success, Error) and provides
 * appropriate UI for each state.
 *
 * Example usage:
 * ```
 * val state: State = ... // Your custom state implementation
 *
 * Markdown(
 *     state = state,
 *     colors = markdownColor(),
 *     typography = markdownTypography(),
 *     loading = { LoadingIndicator() },
 *     error = { ErrorMessage() }
 * )
 * ```
 *
 * @param state A [State] object representing the current state of the markdown content (loading, success, or error).
 * @param colors The color scheme to be used for rendering different markdown elements.
 * @param typography The typography settings to be used for text rendering.
 * @param modifier The modifier to be applied to the container. Defaults to [Modifier.fillMaxSize].
 * @param padding The padding configuration to be applied to different markdown elements.
 * @param dimens The dimension settings for spacing and sizing of markdown elements.
 * @param imageTransformer The transformer for handling and loading images within markdown content.
 * @param annotator The annotator for adding custom annotations to the rendered content.
 * @param extendedSpans The configuration for extended text spans beyond standard markdown.
 * @param inlineContent The configuration for inline custom content within markdown.
 * @param components The custom components to be used for rendering different markdown elements.
 * @param animations The animation configurations for interactive elements.
 * @param loading A composable function to be displayed when the state is [State.Loading].
 * @param success A composable function to be displayed when the state is [State.Success].
 *                It receives the state, components, and modifier as parameters.
 *                By default, this renders the content in a [Column].
 * @param error A composable function to be displayed when the state is [State.Error].
 *
 * @see State
 * @see State.Loading
 * @see State.Success
 * @see State.Error
 */
@Composable
fun Markdown(
    state: State,
    colors: MarkdownColors,
    typography: MarkdownTypography,
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    inlineContent: MarkdownInlineContent = markdownInlineContent(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
) {
    CompositionLocalProvider(
        LocalReferenceLinkHandler provides state.referenceLinkHandler,
        LocalMarkdownPadding provides padding,
        LocalMarkdownDimens provides dimens,
        LocalMarkdownColors provides colors,
        LocalMarkdownTypography provides typography,
        LocalImageTransformer provides imageTransformer,
        LocalMarkdownAnnotator provides annotator,
        LocalMarkdownExtendedSpans provides extendedSpans,
        LocalMarkdownInlineContent provides inlineContent,
        LocalMarkdownComponents provides components,
        LocalMarkdownAnimations provides animations,
    ) {
        when (val markdown = state) {
            is State.Error -> error(modifier)
            is State.Loading -> loading(modifier)
            is State.Success -> success(markdown, components, modifier)
        }
    }
}

/**
 * Renders the successfully parsed markdown content in a Column layout.
 *
 * This function is used internally by the [Markdown] composable when the state is [State.Success].
 * It iterates through the parsed markdown nodes and renders each element using the appropriate
 * component from the provided [MarkdownComponents].
 *
 * You can use this function directly if you have a custom state management system and
 * want to render only the success state of markdown content.
 *
 * Example usage:
 * ```
 * val successState: State.Success = ... // Your parsed markdown state
 * val components = markdownComponents()
 *
 * MarkdownSuccess(
 *     state = successState,
 *     components = components,
 *     modifier = Modifier.padding(16.dp)
 * )
 * ```
 *
 * @param state The [State.Success] object containing the parsed markdown content and node tree.
 * @param components The [MarkdownComponents] instance containing the components to use for rendering
 *                   different markdown elements.
 * @param modifier The modifier to be applied to the container Column.
 *
 * @see State.Success
 * @see MarkdownComponents
 * @see MarkdownElement
 */
@Composable
fun MarkdownSuccess(
    state: State.Success,
    components: MarkdownComponents,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        state.node.children.forEach { node ->
            MarkdownElement(node, components, state.content, skipLinkDefinition = state.linksLookedUp)
        }
    }
}
