package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.utils.findChildOfTypeRecursive
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownImage(content: String, node: ASTNode) {

    val link = node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)?.toString() ?: return

    LocalImageTransformer.current.transform(link)?.let { painter ->
        Image(
            painter = painter,
            contentDescription = "Markdown Image", // TODO
            alignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
