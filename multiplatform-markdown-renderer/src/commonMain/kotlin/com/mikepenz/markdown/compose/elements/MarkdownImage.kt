package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.utils.findChildOfTypeRecursive
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
fun MarkdownImage(content: String, node: ASTNode) {

    val link = node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)?.toString() ?: return

    LocalImageTransformer.current.transform(link)?.let { imageData ->
        Image(
            painter = imageData.painter,
            contentDescription = imageData.contentDescription,
            alignment = imageData.alignment,
            modifier = imageData.modifier
        )
    }
}
