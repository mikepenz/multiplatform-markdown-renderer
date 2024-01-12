package com.mikepenz.markdown.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownParagraph(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.paragraph
) {
    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(content, node)
        pop()
    }
    MarkdownText(styledText, modifier = modifier, style = style)
}
