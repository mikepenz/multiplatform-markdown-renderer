package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

internal interface MarkdownImageState {
    val imageSize: TextUnitSize
    fun setContainerSize(intSize: IntSize)
    fun setImageSize(size: Size)
}

internal class MarkdownImageStateImpl(private val density: Density) : MarkdownImageState {

    private var parentSize by mutableStateOf(Size.Unspecified)

    private var intrinsicImageSize by mutableStateOf(Size.Unspecified)

    override val imageSize by derivedStateOf {
        with(density) {
            if (parentSize.isUnspecified) {
                TextUnitSize(180.sp, 180.sp)
            } else if (intrinsicImageSize.isUnspecified) {
                TextUnitSize(parentSize.width.toSp(), 180.sp)
            } else {
                val width = minOf(intrinsicImageSize.width, parentSize.width)

                val height = if (intrinsicImageSize.width < parentSize.width) {
                    intrinsicImageSize.height
                } else {
                    (intrinsicImageSize.height * parentSize.width) / intrinsicImageSize.width
                }

                TextUnitSize(width.toSp(), height.toSp())
            }
        }
    }

    override fun setContainerSize(intSize: IntSize) {
        parentSize = intSize.toSize()
    }

    override fun setImageSize(size: Size) {
        intrinsicImageSize = size
    }
}

@Composable
internal fun rememberMarkdownImageState(): MarkdownImageState {
    val density = LocalDensity.current
    return remember(density) {
        MarkdownImageStateImpl(density)
    }
}