package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mikepenz.markdown.latex.model.DisplayList

private class WasmJsMathEngine : MathEngine {
    override suspend fun parse(latex: String): DisplayList {
        throw UnsupportedOperationException(
            "wasmJs LaTeX rendering is not yet supported."
        )
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine = remember { WasmJsMathEngine() }
