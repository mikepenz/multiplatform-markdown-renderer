package com.mikepenz.markdown.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.*

/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
val LocalBulletListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { _, _, _ -> "• " }
}

/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
val LocalOrderedListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { _, _, index -> "${index + 1}. " }
}

/**
 * Local [ReferenceLinkHandler] provider
 */
val LocalReferenceLinkHandler = staticCompositionLocalOf<ReferenceLinkHandler> {
    error("CompositionLocal ReferenceLinkHandler not present")
}

/**
 * Local [MarkdownColors] provider
 */
val LocalMarkdownColors = compositionLocalOf<MarkdownColors> {
    error("No local MarkdownColors")
}

/**
 * Local [MarkdownTypography] provider
 */
val LocalMarkdownTypography = compositionLocalOf<MarkdownTypography> {
    error("No local MarkdownTypography")
}

/**
 * Local [MarkdownPadding] provider
 */
val LocalMarkdownPadding = staticCompositionLocalOf<MarkdownPadding> {
    error("No local Padding")
}

/**
 * Local [MarkdownDimens] provider
 */
val LocalMarkdownDimens = compositionLocalOf<MarkdownDimens> {
    error("No local MarkdownDimens")
}

/**
 * Local [ImageTransformer] provider
 */
val LocalImageTransformer = staticCompositionLocalOf<ImageTransformer> {
    error("No local ImageTransformer")
}

/**
 * Local [MarkdownAnnotator] provider
 */
val LocalMarkdownAnnotator = compositionLocalOf<MarkdownAnnotator> {
    return@compositionLocalOf DefaultMarkdownAnnotator(null)
}

/**
 * Local [MarkdownExtendedSpans] provider
 */
val LocalMarkdownExtendedSpans = compositionLocalOf<MarkdownExtendedSpans> {
    return@compositionLocalOf DefaultMarkdownExtendedSpans(null)
}

/**
 * Local [MarkdownComponents] provider
 */
val LocalMarkdownComponents = compositionLocalOf<MarkdownComponents> {
    return@compositionLocalOf markdownComponents()
}

/**
 * Local [MarkdownAnimations] provider
 */
val LocalMarkdownAnimations = compositionLocalOf<MarkdownAnimations> {
    error("No local MarkdownAnimations")
}

/**
 * Local [MarkdownTable] provider
 */
val LocalMarkdownTable = compositionLocalOf<MarkdownComponent> {
    error("No local MarkdownTable")
}
