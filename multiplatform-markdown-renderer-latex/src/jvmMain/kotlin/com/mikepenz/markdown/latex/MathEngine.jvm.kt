package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mikepenz.markdown.latex.model.DisplayList

private class JvmMathEngine : MathEngine {
    override suspend fun parse(latex: String): DisplayList {
        throw UnsupportedOperationException(
            "JVM Desktop LaTeX rendering requires a JNI bridge for libratex_ffi."
        )
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine = remember { JvmMathEngine() }
