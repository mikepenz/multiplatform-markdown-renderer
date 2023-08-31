package com.mikepenz.markdown.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
internal actual fun imagePainter(url: String): Painter? {
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
}

@Composable
internal actual fun painterIntrinsicSize(painter: Painter): androidx.compose.ui.geometry.Size {

    var size by remember(painter) { mutableStateOf(painter.intrinsicSize) }

    if (painter is AsyncImagePainter) {
        LaunchedEffect(painter.state) {
            painter.state.painter?.let {
                size = it.intrinsicSize
            }
        }
    }

    return size
}