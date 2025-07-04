package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
interface MarkdownDimens {
    val dividerThickness: Dp
    val codeBackgroundCornerSize: Dp
    val blockQuoteThickness: Dp
    val tableMaxWidth: Dp
    val tableCellWidth: Dp
    val tableCellPadding: Dp
    val tableCornerSize: Dp
}

@Immutable
private class DefaultMarkdownDimens(
    override val dividerThickness: Dp,
    override val codeBackgroundCornerSize: Dp,
    override val blockQuoteThickness: Dp,
    override val tableMaxWidth: Dp,
    override val tableCellWidth: Dp,
    override val tableCellPadding: Dp,
    override val tableCornerSize: Dp,
) : MarkdownDimens {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMarkdownDimens

        if (dividerThickness != other.dividerThickness) return false
        if (codeBackgroundCornerSize != other.codeBackgroundCornerSize) return false
        if (blockQuoteThickness != other.blockQuoteThickness) return false
        if (tableMaxWidth != other.tableMaxWidth) return false
        if (tableCellWidth != other.tableCellWidth) return false
        if (tableCellPadding != other.tableCellPadding) return false
        if (tableCornerSize != other.tableCornerSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dividerThickness.hashCode()
        result = 31 * result + codeBackgroundCornerSize.hashCode()
        result = 31 * result + blockQuoteThickness.hashCode()
        result = 31 * result + tableMaxWidth.hashCode()
        result = 31 * result + tableCellWidth.hashCode()
        result = 31 * result + tableCellPadding.hashCode()
        result = 31 * result + tableCornerSize.hashCode()
        return result
    }
}

@Composable
fun markdownDimens(
    dividerThickness: Dp = 1.dp,
    codeBackgroundCornerSize: Dp = 8.dp,
    blockQuoteThickness: Dp = 2.dp,
    tableMaxWidth: Dp = Dp.Unspecified,
    tableCellWidth: Dp = 160.dp,
    tableCellPadding: Dp = 16.dp,
    tableCornerSize: Dp = 8.dp,
): MarkdownDimens = DefaultMarkdownDimens(
    dividerThickness = dividerThickness,
    codeBackgroundCornerSize = codeBackgroundCornerSize,
    blockQuoteThickness = blockQuoteThickness,
    tableMaxWidth = tableMaxWidth,
    tableCellWidth = tableCellWidth,
    tableCellPadding = tableCellPadding,
    tableCornerSize = tableCornerSize,
)
