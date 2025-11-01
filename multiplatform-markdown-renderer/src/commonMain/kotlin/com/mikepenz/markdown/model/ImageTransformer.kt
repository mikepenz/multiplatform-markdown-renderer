package com.mikepenz.markdown.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density

interface ImageTransformer {
    /**
     * Will retrieve the [ImageData] from an image link/url
     */
    @Composable
    fun transform(link: String): ImageData?

    /**
     * Returns the detected intrinsic size of the painter
     */
    @Composable
    fun intrinsicSize(painter: Painter): Size {
        return painter.intrinsicSize
    }

    /**
     * The expected placeholderSize. Note: The same size is shared for all inline images within a single MarkdownText item.
     */
    fun placeholderConfig(density: Density, containerSize: Size, intrinsicImageSize: Size): PlaceholderConfig {
        return PlaceholderConfig(with(density) {
            if (containerSize.isUnspecified) {
                Size(180f, 180f)
            } else if (intrinsicImageSize.isUnspecified) {
                Size(containerSize.width.toSp().value, 180f)
            } else {
                val width = minOf(intrinsicImageSize.width, containerSize.width)
                val height = if (intrinsicImageSize.width < containerSize.width) {
                    intrinsicImageSize.height
                } else {
                    (intrinsicImageSize.height * containerSize.width) / intrinsicImageSize.width
                }
                Size(width.toSp().value, height.toSp().value)
            }
        })
    }
}

@Immutable
data class PlaceholderConfig(
    val size: Size,
    val verticalAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.Bottom,
)

@Immutable
data class ImageData(
    val painter: Painter,
    val modifier: Modifier = Modifier.fillMaxWidth(),
    val contentDescription: String? = "Image",
    val alignment: Alignment = Alignment.CenterStart,
    val contentScale: ContentScale = ContentScale.Fit,
    val alpha: Float = DefaultAlpha,
    val colorFilter: ColorFilter? = null,
)
