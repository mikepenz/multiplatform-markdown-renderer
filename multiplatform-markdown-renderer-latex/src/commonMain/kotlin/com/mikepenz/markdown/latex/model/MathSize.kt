package com.mikepenz.markdown.latex.model

internal data class MathSize(val width: Float, val height: Float, val depth: Float) {
    val totalHeight: Float get() = height + depth
}

internal fun DisplayList.toMathSize(fontSize: Float) = MathSize(
    width = (width * fontSize).toFloat(),
    height = (height * fontSize).toFloat(),
    depth = (depth * fontSize).toFloat(),
)
