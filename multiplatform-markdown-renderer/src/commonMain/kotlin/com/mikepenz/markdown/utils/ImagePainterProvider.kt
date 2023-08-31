package com.mikepenz.markdown.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal expect fun imagePainter(url: String): Painter?

@Composable
internal expect fun painterIntrinsicSize(painter: Painter): Size