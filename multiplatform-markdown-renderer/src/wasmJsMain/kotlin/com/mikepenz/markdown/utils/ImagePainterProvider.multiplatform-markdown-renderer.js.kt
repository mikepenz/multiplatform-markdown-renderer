package com.mikepenz.markdown.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal actual fun imagePainter(url: String): Painter? {
    return null
}

@Composable
internal actual fun painterIntrinsicSize(painter: Painter): Size {
    return painter.intrinsicSize
}