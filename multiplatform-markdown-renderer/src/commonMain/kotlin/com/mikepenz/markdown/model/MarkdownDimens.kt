package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface MarkdownDimens {
    val dividerThickness: Dp
    val codeBackgroundCornerSize: Dp
    val blockQuoteThickness: Dp
    val blockQuote: Dp
}

@Immutable
private class DefaultMarkdownDimens(
    override val dividerThickness: Dp,
    override val codeBackgroundCornerSize: Dp,
    override val blockQuoteThickness: Dp,
    override val blockQuote: Dp
) : MarkdownDimens

@Composable
fun markdownDimens(
    dividerThickness: Dp = 1.dp,
    codeBackgroundCornerSize: Dp = 8.dp,
    blockQuoteThickness: Dp = 1.dp,
    blockQuote: Dp = 12.dp
): MarkdownDimens = DefaultMarkdownDimens(
    dividerThickness = dividerThickness,
    codeBackgroundCornerSize = codeBackgroundCornerSize,
    blockQuoteThickness = blockQuoteThickness,
    blockQuote = blockQuote,
)
