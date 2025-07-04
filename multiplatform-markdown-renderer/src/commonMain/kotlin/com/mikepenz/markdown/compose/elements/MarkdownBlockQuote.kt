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
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.MarkdownElement
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownBlockQuote(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.quote,
) {
    val blockQuoteColor = if (style.color.isSpecified) {
        style.color
    } else {
        LocalMarkdownColors.current.text
    }
    val blockQuoteThickness = LocalMarkdownDimens.current.blockQuoteThickness
    val blockQuote = LocalMarkdownPadding.current.blockQuote
    val blockQuoteText = LocalMarkdownPadding.current.blockQuoteText
    val blockQuoteBar = LocalMarkdownPadding.current.blockQuoteBar
    val markdownComponents = LocalMarkdownComponents.current

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
            .semantics {
                role = Role.Group
            }
    ) {
        val blockQuoteLineHeightInDp = with(LocalDensity.current) { LocalMarkdownTypography.current.quote.fontSize.toDp() }
        var priorNestedQuote = false
        node.children.onEachIndexed { index, child ->
            if (child.type == MarkdownElementTypes.BLOCK_QUOTE) {
                // if block quote is nested, and comes after non block quote, add padding
                if (!priorNestedQuote && index != 0) Spacer(Modifier.height(blockQuoteText.calculateBottomPadding()))
                MarkdownBlockQuote(content = content, node = child, style = style)
                priorNestedQuote = true
            } else if (child.type == EOL) {
                Spacer(Modifier.height(blockQuoteLineHeightInDp))
            } else {
                // if first item either completely, or after a nested quote, add top padding
                if (index == 0 || priorNestedQuote) Spacer(Modifier.height(blockQuoteText.calculateTopPadding()))
                priorNestedQuote = false
                MarkdownElement(
                    node = child,
                    components = markdownComponents,
                    content = content,
                    includeSpacer = false
                )
                // if last item, add bottom padding
                if (index == node.children.lastIndex) Spacer(Modifier.height(blockQuoteText.calculateBottomPadding()))
            }
        }
    }
}
