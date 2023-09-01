package com.mikepenz.markdown.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter

interface ImageTransformer {
    @Composable
    fun transform(link: String): ImageData?

    @Composable
    fun intrinsicSize(painter: Painter): Size {
        return painter.intrinsicSize
    }
}

@Immutable
data class ImageData(
    val painter: Painter,
    val contentDescription: String? = "Image",
    val alignment: Alignment = Alignment.CenterStart,
    val modifier: Modifier = Modifier.fillMaxWidth()
)