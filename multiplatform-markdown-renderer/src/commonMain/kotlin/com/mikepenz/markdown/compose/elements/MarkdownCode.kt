package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode

@Composable
private fun MarkdownCode(
    code: String,
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val backgroundCodeColor = LocalMarkdownColors.current.codeBackground
    val codeBackgroundCornerSize = LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock
    MarkdownCodeBackground(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(codeBackgroundCornerSize),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        @Suppress("DEPRECATION")
        MarkdownBasicText(
            text = code,
            color = LocalMarkdownColors.current.codeText,
            style = style,
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(codeBlockPadding),
        )
    }
}

@Composable
fun MarkdownCodeFence(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.code,
    block: @Composable (String, String?, TextStyle) -> Unit = { code, _, style -> MarkdownCode(code = code, style = style) },
) {
    // CODE_FENCE_START, FENCE_LANG, EOL, {content // CODE_FENCE_CONTENT // x-times}, CODE_FENCE_END
    // CODE_FENCE_START, EOL, {content // CODE_FENCE_CONTENT // x-times}, EOL
    // CODE_FENCE_START, EOL, {content // CODE_FENCE_CONTENT // x-times}
    // CODE_FENCE_START, FENCE_LANG, EOL, {content // CODE_FENCE_CONTENT // x-times}

    val language = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG)?.getTextInNode(content)?.toString()
    if (node.children.size >= 3) {
        val start = node.children[2].startOffset
        val minCodeFenceCount = if (language != null) 3 else 2
        val end = node.children[(node.children.size - 2).coerceAtLeast(minCodeFenceCount)].endOffset
        block(content.subSequence(start, end).toString().replaceIndent(), language, style)
    } else {
        // invalid code block, skipping
    }
}

@Composable
fun MarkdownCodeBlock(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.code,
    block: @Composable (String, String?, TextStyle) -> Unit = { code, _, style -> MarkdownCode(code = code, style = style) },
) {
    val start = node.children[0].startOffset
    val end = node.children[node.children.size - 1].endOffset
    val language = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG)?.getTextInNode(content)?.toString()
    block(content.subSequence(start, end).toString().replaceIndent(), language, style)
}

@Composable
fun MarkdownCodeBackground(
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.shadow(elevation, shape, clip = false).then(if (border != null) Modifier.border(border, shape) else Modifier).background(color = color, shape = shape)
            .clip(shape).semantics(mergeDescendants = false) {
                isTraversalGroup = true
            }.pointerInput(Unit) {}, propagateMinConstraints = true
    ) {
        content()
    }
}
