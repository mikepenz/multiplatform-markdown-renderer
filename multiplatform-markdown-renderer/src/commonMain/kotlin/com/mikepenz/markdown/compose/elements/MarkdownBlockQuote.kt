package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.handleElement
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
fun MarkdownBlockQuote(
    content: String,
    node: ASTNode,
    markdownComponents: MarkdownComponents,
    style: TextStyle = LocalMarkdownTypography.current.quote,
) {
    val blockQuoteColor = LocalMarkdownColors.current.text
    val blockQuoteThickness = LocalMarkdownDimens.current.blockQuoteThickness
    val blockQuote = LocalMarkdownPadding.current.blockQuote
    val blockQuoteText = LocalMarkdownPadding.current.blockQuoteText
    val blockQuoteBar = LocalMarkdownPadding.current.blockQuoteBar

    Column(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = blockQuoteColor,
                    strokeWidth = blockQuoteThickness.toPx(),
                    start = Offset(blockQuoteBar.calculateStartPadding(LayoutDirection.Ltr).toPx(), blockQuoteBar.calculateTopPadding().toPx()),
                    end = Offset(blockQuoteBar.calculateStartPadding(LayoutDirection.Ltr).toPx(), size.height - blockQuoteBar.calculateBottomPadding().toPx())
                )
            }
            .padding(blockQuote)
    ) {
        val nonBlockquotes = node.children.filter { it.type != MarkdownElementTypes.BLOCK_QUOTE }
        val nestedQuote = node.findChildOfType(MarkdownElementTypes.BLOCK_QUOTE)

        if (nonBlockquotes.isNotEmpty()) {
            Column(modifier = Modifier.padding(blockQuoteText)) {
                nonBlockquotes.onEach { quote ->
                    handleElement(quote, markdownComponents, content, false)
                }
            }

            if (nestedQuote != null) Spacer(Modifier.height(8.dp))
        }

        if (nestedQuote != null) {
            MarkdownBlockQuote(
                content = content,
                node = nestedQuote,
                markdownComponents = markdownComponents,
                style = style
            )
        }
    }
}
