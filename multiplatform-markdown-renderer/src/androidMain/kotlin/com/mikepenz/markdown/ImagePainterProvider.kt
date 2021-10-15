package com.mikepenz.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize

@OptIn(ExperimentalCoilApi::class)
@Composable
internal actual fun imagePainter(url: String): Painter? {
    return rememberImagePainter(url, builder = { size(OriginalSize) })
}
