package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import com.mikepenz.markdown.latex.model.DisplayList

internal expect val isAsyncFontLoading: Boolean

/**
 * Platform-specific LaTeX parsing engine.
 */
interface MathEngine {
    suspend fun parse(latex: String): DisplayList
}

@Composable
expect fun rememberMathEngine(): MathEngine
