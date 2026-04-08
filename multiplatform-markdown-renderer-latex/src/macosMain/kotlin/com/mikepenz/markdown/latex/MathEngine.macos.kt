package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mikepenz.markdown.latex.model.DisplayList

internal actual val isAsyncFontLoading: Boolean = false

private class MacosMathEngine : MathEngine {
    override suspend fun parse(latex: String): DisplayList {
        throw UnsupportedOperationException(
            "macOS LaTeX rendering is not yet supported."
        )
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine = remember { MacosMathEngine() }
