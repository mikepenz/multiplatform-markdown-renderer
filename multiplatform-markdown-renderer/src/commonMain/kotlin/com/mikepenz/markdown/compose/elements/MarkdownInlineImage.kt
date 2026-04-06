package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.LocalImageTransformer
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownInlineImage(link: String, node: ASTNode) {
    val transformer = LocalImageTransformer.current
    transformer.transform(link)?.let { imageData ->
        Image(
            painter = imageData.painter,
            contentDescription = imageData.contentDescription,
            modifier = Modifier.fillMaxSize().then(imageData.modifier),
            alignment = imageData.alignment,
            contentScale = imageData.contentScale,
            alpha = imageData.alpha,
            colorFilter = imageData.colorFilter
        )
    }
}
