package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.sp
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
    language: String?,
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val titleBackgroundColor = LocalMarkdownColors.current.codeTitleBackground
    val codeBackgroundColor = LocalMarkdownColors.current.codeBackground
    val titleTextColor = LocalMarkdownColors.current.codeTitleText
    val codeBackgroundCornerSize = LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock

    MarkdownCodeBackground(
        titleBackgroundColor = titleBackgroundColor,
        codeBackgroundColor = codeBackgroundColor,
        titleTextColor = titleTextColor,
        titleTextStyle = style,
        language = language,
        shape = RoundedCornerShape(codeBackgroundCornerSize),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        MarkdownBasicText(
            code, color = LocalMarkdownColors.current.codeText, modifier = Modifier.horizontalScroll(rememberScrollState()).padding(codeBlockPadding), style = style
        )
    }
}

@Composable
fun MarkdownCodeFence(
    content: String,
    node: ASTNode,
    block: @Composable (String, String?) -> Unit = { code, language -> MarkdownCode(code, language) },
) {
    // CODE_FENCE_START, FENCE_LANG, {content // CODE_FENCE_CONTENT // x-times}, CODE_FENCE_END
    // CODE_FENCE_START, EOL, {content // CODE_FENCE_CONTENT // x-times}, EOL
    // CODE_FENCE_START, EOL, {content // CODE_FENCE_CONTENT // x-times}

    val language = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG)?.getTextInNode(content)?.toString()
    if (node.children.size >= 3) {
        val start = node.children[2].startOffset
        val end = node.children[(node.children.size - 2).coerceAtLeast(2)].endOffset
        block(content.subSequence(start, end).toString().replaceIndent(), language)
    } else {
        // invalid code block, skipping
    }
}

@Composable
fun MarkdownCodeBlock(
    content: String,
    node: ASTNode,
    block: @Composable (String, String?) -> Unit = { code, language -> MarkdownCode(code, language) },
) {
    val start = node.children[0].startOffset
    val end = node.children[node.children.size - 1].endOffset
    val language = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG)?.getTextInNode(content)?.toString()
    block(content.subSequence(start, end).toString().replaceIndent(), language)
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
    Box(modifier = modifier.shadow(elevation, shape, clip = false).then(if (border != null) Modifier.border(border, shape) else Modifier).background(color = color, shape = shape)
        .clip(shape).semantics(mergeDescendants = false) {
            isTraversalGroup = true
        }.pointerInput(Unit) {}, propagateMinConstraints = true
    ) {
        content()
    }
}

@Composable
fun MarkdownCodeBackground(
    titleBackgroundColor: Color,
    codeBackgroundColor: Color,
    titleTextColor: Color,
    titleTextStyle: TextStyle,
    language: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.shadow(elevation, shape, clip = false)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clip(shape).semantics(mergeDescendants = false) {
                isTraversalGroup = true
            }.pointerInput(Unit) {}) {
        language?.let {
            Row(modifier = Modifier.fillMaxWidth().background(color = titleBackgroundColor).padding(8.dp)) {
                MarkdownBasicText(
                    text = "[$it]",
                    color = titleTextColor,
                    modifier = Modifier,
                    style = titleTextStyle.copy(fontSize = 12.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                // Add copy button
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(color = codeBackgroundColor),
            propagateMinConstraints = true
        ) {
            content()
        }
    }
}