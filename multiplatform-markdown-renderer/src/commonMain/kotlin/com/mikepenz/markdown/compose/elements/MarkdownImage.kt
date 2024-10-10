package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.utils.findChildOfTypeRecursive
import com.mikepenz.markdown.utils.getUnescapedTextInNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownImage(content: String, node: ASTNode) {

    val link = node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getUnescapedTextInNode(content) ?: return

    LocalImageTransformer.current.transform(link)?.let { imageData ->
        Image(
            painter = imageData.painter,
            contentDescription = imageData.contentDescription,
            modifier = imageData.modifier,
            alignment = imageData.alignment,
            contentScale = imageData.contentScale,
            alpha = imageData.alpha,
            colorFilter = imageData.colorFilter
        )
    }
}
