package com.mikepenz.markdown.m2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.MarkdownSuccess
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m2.elements.MarkdownCheckBox
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
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser


/**
 * Renders the markdown content using Material 2 styles.
 *
 * @param content The markdown content to be rendered.
 * @param colors The [MarkdownColors] to use for styling.
 * @param typography The [MarkdownTypography] to use for text styles.
 * @param modifier The [Modifier] to apply to the component.
 * @param padding The [MarkdownPadding] to use for padding.
 * @param dimens The [MarkdownDimens] to use for dimensions.
 * @param flavour The [MarkdownFlavourDescriptor] to use for parsing.
 * @param parser The [MarkdownParser] to use for parsing.
 * @param imageTransformer The [ImageTransformer] to use for transforming images.
 * @param annotator The [MarkdownAnnotator] to use for annotating links.
 * @param extendedSpans The [MarkdownExtendedSpans] to use for extended spans.
 * @param components The [MarkdownComponents] to use for custom components.
 * @param animations The [MarkdownAnimations] to use for animations.
 * @param referenceLinkHandler The reference link handler to be used for handling links.
 * @param loading Composable function to display while loading.
 * @param success A composable function to be displayed with the markdown content. It receives the modifier, state and components as parameters. By default this is a [Column].
 * @param error Composable function to display on error.
 */
@Composable
fun Markdown(
    content: String,
    colors: MarkdownColors = markdownColor(),
    typography: MarkdownTypography = markdownTypography(),
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) }),
    animations: MarkdownAnimations = markdownAnimations(),
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
) = com.mikepenz.markdown.compose.Markdown(
    content = content,
    colors = colors,
    typography = typography,
    modifier = modifier,
    padding = padding,
    dimens = dimens,
    flavour = flavour,
    parser = parser,
    imageTransformer = imageTransformer,
    annotator = annotator,
    extendedSpans = extendedSpans,
    components = components,
    animations = animations,
    referenceLinkHandler = referenceLinkHandler,
    loading = loading,
    success = success,
    error = error,
)

/**
 * Renders the markdown content using Material 2 styles.
 *
 * @param state The [MarkdownState] to use for parsing.
 * @param colors The [MarkdownColors] to use for styling.
 * @param typography The [MarkdownTypography] to use for text styles.
 * @param modifier The [Modifier] to apply to the component.
 * @param padding The [MarkdownPadding] to use for padding.
 * @param dimens The [MarkdownDimens] to use for dimensions.
 * @param imageTransformer The [ImageTransformer] to use for transforming images.
 * @param annotator The [MarkdownAnnotator] to use for annotating links.
 * @param extendedSpans The [MarkdownExtendedSpans] to use for extended spans.
 * @param components The [MarkdownComponents] to use for custom components.
 * @param animations The [MarkdownAnimations] to use for animations.
 * @param loading Composable function to display while loading.
 * @param success A composable function to be displayed with the markdown content. It receives the modifier, state and components as parameters. By default this is a [Column].
 * @param error Composable function to display on error.
 */
@Composable
fun Markdown(
    state: MarkdownState,
    colors: MarkdownColors = markdownColor(),
    typography: MarkdownTypography = markdownTypography(),
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) }),
    animations: MarkdownAnimations = markdownAnimations(),
    loading: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
    success: @Composable (state: State.Success, components: MarkdownComponents, modifier: Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(state = state, components = components, modifier = modifier)
    },
    error: @Composable (modifier: Modifier) -> Unit = { Box(modifier) },
) = com.mikepenz.markdown.compose.Markdown(
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
    error = error,
)