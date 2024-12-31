package com.mikepenz.markdown.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.mikepenz.markdown.compose.LocalMarkdownAnnotator
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import com.mikepenz.markdown.utils.codeSpanStyle
import com.mikepenz.markdown.utils.linkTextSpanStyle
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownParagraph(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.paragraph,
) {
    val annotator = LocalMarkdownAnnotator.current
    val linkTextSpanStyle = LocalMarkdownTypography.current.linkTextSpanStyle
    val codeSpanStyle = LocalMarkdownTypography.current.codeSpanStyle
    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(content, node, linkTextSpanStyle, codeSpanStyle, annotator)
        pop()
    }

    MarkdownText(
        styledText,
        modifier = modifier,
        style = style,
    )
}
