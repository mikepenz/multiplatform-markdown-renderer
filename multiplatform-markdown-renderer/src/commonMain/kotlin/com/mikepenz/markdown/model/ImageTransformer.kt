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
     * The expected placeholderSize for an inline image identified by [link].
     */
    fun placeholderConfig(
        link: String,
        density: Density,
        containerSize: Size,
        imageSize: Size,
        imageSizeChanged: ((link: String, Size) -> Unit)? = null,
    ): PlaceholderConfig {
        fun scale(size: Size, factor: Float): Size {
            return Size(size.width * factor, size.height * factor)
        }

        // Work entirely in DP for consistent unit handling
        // containerSize and imageSize are in PX, convert to DP
        val maxSideDp = DEFAULT_IMAGE_SIDE_LENGTH_DP

        val sizeInDp = with(density) {
            if (containerSize.isUnspecified) {
                // No container size known, use default
                Size(maxSideDp, maxSideDp)
            } else if (imageSize.isUnspecified) {
                // No image size known yet, use container-constrained max side
                val containerWidthDp = containerSize.width.toDp().value
                val containerHeightDp = containerSize.height.toDp().value
                val actualSideLength = minOf(maxSideDp, containerHeightDp, containerWidthDp)
                Size(actualSideLength, actualSideLength)
            } else {
                // Both sizes known, calculate scaled size to fit container
                val imageWidthDp = imageSize.width.toDp().value
                val imageHeightDp = imageSize.height.toDp().value
                val containerWidthDp = containerSize.width.toDp().value
                val containerHeightDp = containerSize.height.toDp().value

                val actualHeight = minOf(imageHeightDp, containerHeightDp, maxSideDp)
                val actualWidth = minOf(imageWidthDp, containerWidthDp, maxSideDp)

                if (actualWidth < imageWidthDp || actualHeight < imageHeightDp) {
                    // Image needs scaling down to fit
                    val lowestRatio = minOf(actualWidth / imageWidthDp, actualHeight / imageHeightDp)
                    scale(Size(imageWidthDp, imageHeightDp), lowestRatio)
                } else {
                    Size(actualWidth, actualHeight)
                }
            }
        }

        return PlaceholderConfig(sizeInDp)
    }

    companion object {
        const val DEFAULT_IMAGE_SIDE_LENGTH_DP = 200f
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
