package com.mikepenz.markdown.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface MarkdownPadding {
    val block: Dp
    val list: Dp
    val listItemBottom: Dp
    val indentList: Dp
    val codeBlock: PaddingValues
    val blockQuote: PaddingValues
    val blockQuoteText: PaddingValues
    val blockQuoteBar: PaddingValues.Absolute
}

@Immutable
private class DefaultMarkdownPadding(
    override val block: Dp,
    override val list: Dp,
    override val listItemBottom: Dp,
    override val indentList: Dp,
    override val codeBlock: PaddingValues,
    override val blockQuote: PaddingValues,
    override val blockQuoteText: PaddingValues,
    override val blockQuoteBar: PaddingValues.Absolute,
) : MarkdownPadding

@Composable
fun markdownPadding(
    block: Dp = 2.dp,
    list: Dp = 8.dp,
    listItemBottom: Dp = 4.dp,
    indentList: Dp = 8.dp,
    codeBlock: PaddingValues = PaddingValues(8.dp),
    blockQuote: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
    blockQuoteText: PaddingValues = PaddingValues(vertical = 4.dp),
    blockQuoteBar: PaddingValues.Absolute = PaddingValues.Absolute(left = 4.dp, top = 2.dp, right = 4.dp, bottom = 2.dp),
): MarkdownPadding = DefaultMarkdownPadding(
    block = block,
    list = list,
    listItemBottom = listItemBottom,
    indentList = indentList,
    codeBlock = codeBlock,
    blockQuote = blockQuote,
    blockQuoteText = blockQuoteText,
    blockQuoteBar = blockQuoteBar,
)
