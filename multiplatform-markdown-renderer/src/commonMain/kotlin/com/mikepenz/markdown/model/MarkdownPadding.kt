package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface MarkdownPadding {
    val block: Dp
    val list: Dp
    val indentList: Dp
}

@Immutable
private class DefaultMarkdownPadding(
    override val block: Dp,
    override val list: Dp,
    override val indentList: Dp
) : MarkdownPadding

@Composable
fun markdownPadding(
    block: Dp = 2.dp,
    list: Dp = 8.dp,
    indentList: Dp = 8.dp
): MarkdownPadding = DefaultMarkdownPadding(
    block = block,
    list = list,
    indentList = indentList
)
