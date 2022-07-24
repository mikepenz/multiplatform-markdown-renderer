package com.mikepenz.markdown.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.mikepenz.markdown.model.*

val LocalBulletListHandler = compositionLocalOf<BulletHandler> {
    error("No local BulletListHandler")
}

val LocalOrderedListHandler = compositionLocalOf<BulletHandler> {
    error("No local OrderedListHandler")
}

val LocalMarkdownColors = compositionLocalOf<MarkdownColors> {
    error("No local MarkdownColors")
}

val LocalMarkdownTypography = compositionLocalOf<MarkdownTypography> {
    error("No local MarkdownTypography")
}

val LocalReferenceLinkHandler = staticCompositionLocalOf<ReferenceLinkHandler> {
    error("No local ReferenceLinkHandler")
}

val LocalMarkdownPadding = staticCompositionLocalOf<MarkdownPadding> {
    error("No local Padding")
}
