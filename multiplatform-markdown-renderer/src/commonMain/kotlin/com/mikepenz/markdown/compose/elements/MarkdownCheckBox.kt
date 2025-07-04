package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
fun MarkdownCheckBox(
    content: String,
    node: ASTNode,
    style: TextStyle,
    checkedIndicator: @Composable (Boolean, Modifier) -> Unit = { checked, modifier ->
        MarkdownText(
            content = "[${if (checked) "x" else " "}] ",
            modifier = modifier,
            style = style.copy(fontFamily = FontFamily.Monospace)
        )
    },
) {
    val checked = node.getTextInNode(content).contains("[x]")
    Row(
        modifier = Modifier.semantics {
            role = Role.Checkbox
            toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
        }
    ) {
        checkedIndicator(checked, Modifier.padding(end = 4.dp))
    }
}
