package com.mikepenz.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal actual fun imagePainter(url: String): Painter {
    return ColorPainter(Color.Red)
}
