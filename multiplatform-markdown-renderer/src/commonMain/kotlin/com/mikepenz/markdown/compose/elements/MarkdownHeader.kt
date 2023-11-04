package com.mikepenz.markdown.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
internal fun MarkdownHeader(
    content: String,
    node: ASTNode,
    style: TextStyle
) {

    node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.let {
        val styledText = buildAnnotatedString {
            pushStyle(style.toSpanStyle())
            buildMarkdownAnnotatedString(content, it)
            pop()
        }

        MarkdownText(styledText, style = style)
    }
}
