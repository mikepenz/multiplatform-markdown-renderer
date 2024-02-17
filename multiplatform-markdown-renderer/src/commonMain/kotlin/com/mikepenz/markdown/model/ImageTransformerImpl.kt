package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import com.mikepenz.markdown.utils.imagePainter
import com.mikepenz.markdown.utils.painterIntrinsicSize

class ImageTransformerImpl : ImageTransformer {

    @Composable
    override fun transform(link: String): ImageData? {
        return imagePainter(link)?.let { ImageData(it) }
    }

    @Composable
    override fun intrinsicSize(painter: Painter): Size {
        return painterIntrinsicSize(painter)
    }
}