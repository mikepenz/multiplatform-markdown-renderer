package com.mikepenz.markdown.model

import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

interface MarkdownAnimations {
    /**
     * Modifier used to animate [MarkdownText] size changes.
     * This is mainly the case if inline images are loaded and their placeholder has a different size from the final image.
     */
    val animateTextSize: Modifier.() -> Modifier
}

@Immutable
class DefaultMarkdownAnimation(
    override val animateTextSize: Modifier.() -> Modifier,
) : MarkdownAnimations

@Composable
fun markdownAnimations(
    /**
     * Modifier used to animate [MarkdownText] size changes.
     * By default, this uses [animateContentSize].
     *
     * It's possible to modify the animation or alternatively return` {this} to not animate at all.
     */
    animateTextSize: Modifier.() -> Modifier = { animateContentSize() },
): MarkdownAnimations = DefaultMarkdownAnimation(
    animateTextSize
)
