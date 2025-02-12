package com.mikepenz.markdown.m2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m2.elements.MarkdownCheckBox
import com.mikepenz.markdown.model.*
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor

@Composable
fun Markdown(
    content: String,
    colors: MarkdownColors = markdownColor(),
    typography: MarkdownTypography = markdownTypography(),
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    components: MarkdownComponents = markdownComponents(
        checkbox = { checked -> MarkdownCheckBox(checked) },
    ),
    animations: MarkdownAnimations = markdownAnimations(),
) = com.mikepenz.markdown.compose.Markdown(
    content = content,
    colors = colors,
    typography = typography,
    modifier = modifier,
    padding = padding,
    dimens = dimens,
    flavour = flavour,
    imageTransformer = imageTransformer,
    annotator = annotator,
    extendedSpans = extendedSpans,
    components = components,
    animations = animations,
)