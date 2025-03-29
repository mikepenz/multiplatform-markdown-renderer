package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.handleElement
import org.intellij.markdown.MarkdownElementTypes.IMAGE
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMElementTypes.HEADER
import org.intellij.markdown.flavours.gfm.GFMElementTypes.ROW
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.CELL
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.TABLE_SEPARATOR

@Composable
fun MarkdownTable(
    content: String,
    node: ASTNode,
    style: TextStyle,
    annotatorSettings: AnnotatorSettings = annotatorSettings(),
    headerBlock: @Composable (String, ASTNode, Dp, TextStyle) -> Unit = { content, header, tableWidth, style ->
        MarkdownTableHeader(
            content = content, header = header, tableWidth = tableWidth, style = style, annotatorSettings = annotatorSettings,
        )
    },
    rowBlock: @Composable (String, ASTNode, Dp, TextStyle) -> Unit = { content, header, tableWidth, style ->
        MarkdownTableRow(
            content = content, header = header, tableWidth = tableWidth, style = style, annotatorSettings = annotatorSettings,
        )
    },
) {
    val tableMaxWidth = LocalMarkdownDimens.current.tableMaxWidth
    val tableCellWidth = LocalMarkdownDimens.current.tableCellWidth
    val tableCornerSize = LocalMarkdownDimens.current.tableCornerSize

    val columnsCount = remember(node) { node.findChildOfType(HEADER)?.children?.count { it.type == CELL } ?: 0 }
    val tableWidth = columnsCount * tableCellWidth

    val backgroundCodeColor = LocalMarkdownColors.current.tableBackground
    BoxWithConstraints(
        modifier = Modifier.background(backgroundCodeColor, RoundedCornerShape(tableCornerSize)).widthIn(max = tableMaxWidth)
    ) {
        val scrollable = maxWidth <= tableWidth
        Column(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState()).requiredWidth(tableWidth)
            } else Modifier.fillMaxWidth()
        ) {
            node.children.forEach {
                when (it.type) {
                    HEADER -> headerBlock(content, it, tableWidth, style)
                    ROW -> rowBlock(content, it, tableWidth, style)
                    TABLE_SEPARATOR -> MarkdownDivider()
                }
            }
        }
    }
}

@Composable
fun MarkdownTableHeader(
    content: String,
    header: ASTNode,
    tableWidth: Dp,
    style: TextStyle,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    annotatorSettings: AnnotatorSettings = annotatorSettings(),
) {
    val markdownComponents = LocalMarkdownComponents.current
    val tableCellPadding = LocalMarkdownDimens.current.tableCellPadding
    Row(
        verticalAlignment = verticalAlignment, modifier = Modifier.widthIn(tableWidth).height(IntrinsicSize.Max)
    ) {
        header.children.filter { it.type == CELL }.forEach { cell ->
            Column(
                modifier = Modifier.padding(tableCellPadding).weight(1f),
            ) {
                if (cell.children.any { it.type == IMAGE }) {
                    handleElement(node = cell, components = markdownComponents, content = content, includeSpacer = false)
                } else {
                    MarkdownTableBasicText(
                        content = content,
                        cell = cell,
                        style = style.copy(fontWeight = FontWeight.Bold),
                        maxLines = maxLines,
                        overflow = overflow,
                        annotatorSettings = annotatorSettings
                    )
                }
            }
        }
    }
}

@Composable
fun MarkdownTableRow(
    content: String,
    header: ASTNode,
    tableWidth: Dp,
    style: TextStyle,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    annotatorSettings: AnnotatorSettings = annotatorSettings(),
) {
    val markdownComponents = LocalMarkdownComponents.current
    val tableCellPadding = LocalMarkdownDimens.current.tableCellPadding
    Row(
        verticalAlignment = verticalAlignment, modifier = Modifier.widthIn(tableWidth)
    ) {
        header.children.filter { it.type == CELL }.forEach { cell ->
            Column(
                modifier = Modifier.padding(tableCellPadding).weight(1f),
            ) {
                if (cell.children.any { it.type == IMAGE }) {
                    handleElement(node = cell, components = markdownComponents, content = content, includeSpacer = false)
                } else {
                    MarkdownTableBasicText(content = content, cell = cell, style = style, maxLines = maxLines, overflow = overflow, annotatorSettings = annotatorSettings)
                }
            }
        }
    }
}


@Composable
fun MarkdownTableBasicText(
    content: String,
    cell: ASTNode,
    style: TextStyle,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    annotatorSettings: AnnotatorSettings = annotatorSettings(),
) {
    @Suppress("DEPRECATION") MarkdownBasicText(
        text = content.buildMarkdownAnnotatedString(
            textNode = cell,
            style = style,
            annotatorSettings = annotatorSettings,
        ),
        style = style,
        color = LocalMarkdownColors.current.tableText,
        maxLines = maxLines,
        overflow = overflow,
    )
}