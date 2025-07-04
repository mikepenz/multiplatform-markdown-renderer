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

    // Extract alt text from the markdown for accessibility
    val altText = node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_TEXT)?.getUnescapedTextInNode(content)?.toString()

    LocalImageTransformer.current.transform(link)?.let { imageData ->
        // Use alt text as content description if available, otherwise use the one from imageData
        val accessibilityDescription = altText ?: imageData.contentDescription

        Image(
            painter = imageData.painter,
            contentDescription = accessibilityDescription,
            modifier = imageData.modifier,
            alignment = imageData.alignment,
            contentScale = imageData.contentScale,
            alpha = imageData.alpha,
            colorFilter = imageData.colorFilter
        )
    }
}
