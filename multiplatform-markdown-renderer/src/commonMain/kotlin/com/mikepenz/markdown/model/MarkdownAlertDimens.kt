package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The dimensions of a GitHub alert.
 */
@Immutable
interface MarkdownAlertDimens {
    /** Thickness of the accent bar drawn along the start edge of an alert. */
    val barThickness: Dp

    /** Size of the icon rendered next to an alert title. */
    val iconSize: Dp
}

@Immutable
private data class DefaultMarkdownAlertDimens(
    override val barThickness: Dp,
    override val iconSize: Dp,
) : MarkdownAlertDimens

fun markdownAlertDimens(
    barThickness: Dp = 4.dp,
    iconSize: Dp = 16.dp,
): MarkdownAlertDimens = DefaultMarkdownAlertDimens(
    barThickness = barThickness,
    iconSize = iconSize,
)
