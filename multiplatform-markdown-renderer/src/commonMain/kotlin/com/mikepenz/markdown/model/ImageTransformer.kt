package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter

interface ImageTransformer {

    @Composable
    fun transform(link: String): Painter?

    @Composable
    fun intrinsicSize(painter: Painter): Size {
        return painter.intrinsicSize
    }
}