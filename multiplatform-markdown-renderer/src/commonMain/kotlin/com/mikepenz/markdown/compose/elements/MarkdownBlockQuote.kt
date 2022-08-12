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
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownBlockQuote(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.quote
) {
    Box(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = style.color,
                    strokeWidth = 2f,
                    start = Offset(12.dp.value, 0f),
                    end = Offset(12.dp.value, size.height)
                )
            }
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {
        val text = buildAnnotatedString {
            pushStyle(style.toSpanStyle())
            append(node.getTextInNode(content).toString())
            pop()
        }
        Text(text)
    }
}
