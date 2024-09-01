package com.mikepenz.markdown.model

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

internal interface MarkdownImageState {
    val density: Density
    val containerSize: Size
    val intrinsicImageSize: Size
    fun setContainerSize(intSize: IntSize)
    fun setImageSize(size: Size)
}

internal class MarkdownImageStateImpl(override val density: Density) : MarkdownImageState {

    override var containerSize by mutableStateOf(Size.Unspecified)

    override var intrinsicImageSize by mutableStateOf(Size.Unspecified)

    override fun setContainerSize(intSize: IntSize) {
        containerSize = intSize.toSize()
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