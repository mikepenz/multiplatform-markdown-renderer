package com.mikepenz.markdown.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The paddings of a GitHub alert.
 */
@Immutable
interface MarkdownAlertPadding {
    /** Padding around the whole alert, including its accent bar */
    val container: PaddingValues

    /** Padding above and below the alert's block content */
    val content: PaddingValues

    /** Insets of the alert's accent bar */
    val bar: PaddingValues.Absolute

    /** Gap between the alert icon and its title */
    val iconSpacing: Dp

    /** Gap between the alert title row and the alert's block content */
    val titleSpacing: Dp
}

@Immutable
private data class DefaultMarkdownAlertPadding(
    override val container: PaddingValues,
    override val content: PaddingValues,
    override val bar: PaddingValues.Absolute,
    override val iconSpacing: Dp,
    override val titleSpacing: Dp,
) : MarkdownAlertPadding

fun markdownAlertPadding(
    container: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
    content: PaddingValues = PaddingValues(vertical = 4.dp),
    bar: PaddingValues.Absolute = PaddingValues.Absolute(left = 2.dp, top = 2.dp, right = 4.dp, bottom = 2.dp),
    iconSpacing: Dp = 8.dp,
    titleSpacing: Dp = 4.dp,
): MarkdownAlertPadding = DefaultMarkdownAlertPadding(
    container = container,
    content = content,
    bar = bar,
    iconSpacing = iconSpacing,
    titleSpacing = titleSpacing,
)
