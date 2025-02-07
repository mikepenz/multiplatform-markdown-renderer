package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize

interface MarkdownImageState {
    val density: Density
    val containerSize: Size
    val intrinsicImageSize: Size

    @Deprecated("Use updateContainerSize instead", ReplaceWith("updateContainerSize(size)"))
    fun setContainerSize(intSize: IntSize)

    @Deprecated("Use updateImageSize instead", ReplaceWith("updateImageSize(size)"))
    fun setImageSize(size: Size)

    @Suppress("DEPRECATION")
    fun updateContainerSize(size: Size) = setContainerSize(size.toIntSize())

    @Suppress("DEPRECATION")
    fun updateImageSize(size: Size) = setImageSize(size)
}

internal class MarkdownImageStateImpl(override val density: Density) : MarkdownImageState {

    override var containerSize by mutableStateOf(Size.Unspecified)

    override var intrinsicImageSize by mutableStateOf(Size.Unspecified)

    @Deprecated("Use updateContainerSize instead", replaceWith = ReplaceWith("updateContainerSize(size)"))
    override fun setContainerSize(intSize: IntSize) = updateContainerSize(intSize.toSize())

    @Deprecated("Use updateImageSize instead", replaceWith = ReplaceWith("updateImageSize(size)"))
    override fun setImageSize(size: Size) = updateImageSize(size)

    override fun updateContainerSize(size: Size) {
        containerSize = size
    }

    override fun updateImageSize(size: Size) {
        intrinsicImageSize = size
    }
}

@Composable
internal fun rememberMarkdownImageState(): MarkdownImageState {
    val density = LocalDensity.current
    return remember(density) { MarkdownImageStateImpl(density) }
}