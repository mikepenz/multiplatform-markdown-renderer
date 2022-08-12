package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownHeader(
    content: String,
    node: ASTNode,
    style: TextStyle
) {
    node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.let {
        Text(
            it.getTextInNode(content).trim().toString(),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            style = style
        )
    }
}
