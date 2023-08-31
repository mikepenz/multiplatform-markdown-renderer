package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import com.mikepenz.markdown.utils.imagePainter
import com.mikepenz.markdown.utils.painterIntrinsicSize

internal object ImageTransformerImpl : ImageTransformer {

    @Composable
    override fun transform(link: String): Painter? {
        return imagePainter(link)
    }

    @Composable
    override fun intrinsicSize(painter: Painter): Size {
        return painterIntrinsicSize(painter)
    }
}