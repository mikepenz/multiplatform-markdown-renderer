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
}

@Immutable
private class DefaultMarkdownPadding(
    override val block: Dp,
    override val list: Dp,
    override val listItemBottom: Dp,
    override val indentList: Dp,
    override val codeBlock: PaddingValues,
) : MarkdownPadding

@Composable
fun markdownPadding(
    block: Dp = 2.dp,
    list: Dp = 8.dp,
    listItemBottom: Dp = 4.dp,
    indentList: Dp = 8.dp,
    codeBlock: PaddingValues = PaddingValues(8.dp)
): MarkdownPadding = DefaultMarkdownPadding(
    block = block,
    list = list,
    listItemBottom = listItemBottom,
    indentList = indentList,
    codeBlock = codeBlock
)
