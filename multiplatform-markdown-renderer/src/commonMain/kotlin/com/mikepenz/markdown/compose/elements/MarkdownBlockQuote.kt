package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.LayoutDirection
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
fun MarkdownBlockQuote(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.quote
) {
    val blockQuoteColor = LocalMarkdownColors.current.text
    val blockQuoteThickness = LocalMarkdownDimens.current.blockQuoteThickness
    val blockQuote = LocalMarkdownPadding.current.blockQuote
    val blockQuoteBar = LocalMarkdownPadding.current.blockQuoteBar
    Box(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = blockQuoteColor,
                    strokeWidth = blockQuoteThickness.value,
                    start = Offset(blockQuoteBar.calculateLeftPadding(LayoutDirection.Ltr).value, 0f),
                    end = Offset(blockQuoteBar.calculateRightPadding(LayoutDirection.Ltr).value, size.height)
                )
            }
            .padding(blockQuote)
    ) {
        val text = buildAnnotatedString {
            pushStyle(style.toSpanStyle())
            append(node.getTextInNode(content).toString())
            pop()
        }
        Text(
            text = text,
            style = style,
            color = LocalMarkdownColors.current.text,
        )
    }
}
