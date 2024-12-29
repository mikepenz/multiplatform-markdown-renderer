package com.mikepenz.markdown.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.mikepenz.markdown.compose.LocalMarkdownAnnotator
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import com.mikepenz.markdown.utils.codeSpanStyle
import com.mikepenz.markdown.utils.linkTextSpanStyle
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
fun MarkdownHeader(
    content: String,
    node: ASTNode,
    style: TextStyle,
    contentChildType: IElementType = MarkdownTokenTypes.ATX_CONTENT,
) {
    val annotator = LocalMarkdownAnnotator.current
    val linkTextSpanStyle = LocalMarkdownTypography.current.linkTextSpanStyle
    val codeSpanStyle = LocalMarkdownTypography.current.codeSpanStyle

    node.findChildOfType(contentChildType)?.let {
        val styledText = buildAnnotatedString {
            pushStyle(style.toSpanStyle())
            buildMarkdownAnnotatedString(content, it, linkTextSpanStyle, codeSpanStyle, annotator)
            pop()
        }

        MarkdownText(styledText, style = style)
    }
}
