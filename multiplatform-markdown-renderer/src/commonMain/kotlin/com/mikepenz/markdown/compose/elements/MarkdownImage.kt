package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalReferenceLinkHandler
import com.mikepenz.markdown.utils.resolveImageLink
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownImage(content: String, node: ASTNode) {

    val link = node.resolveImageLink(content, LocalReferenceLinkHandler.current) ?: return

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
