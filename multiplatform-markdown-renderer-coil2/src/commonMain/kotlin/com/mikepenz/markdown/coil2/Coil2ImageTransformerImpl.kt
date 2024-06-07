package com.mikepenz.markdown.coil2

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import com.mikepenz.markdown.model.ImageData
import com.mikepenz.markdown.model.ImageTransformer

object Coil2ImageTransformerImpl : ImageTransformer {

    @Composable
    override fun transform(link: String): ImageData? {
        return imagePainter(link)?.let { ImageData(it) }
    }

    @Composable
    override fun intrinsicSize(painter: Painter): Size {
        return painterIntrinsicSize(painter)
    }
}