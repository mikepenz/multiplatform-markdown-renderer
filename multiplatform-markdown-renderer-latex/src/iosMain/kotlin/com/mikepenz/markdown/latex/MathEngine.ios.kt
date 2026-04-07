package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.latex.model.latexJson
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ratex.ffi.ratex_free_display_list
import ratex.ffi.ratex_get_last_error
import ratex.ffi.ratex_parse_and_layout

private class IosMathEngine : MathEngine {
    @OptIn(ExperimentalForeignApi::class)
    private suspend fun parseJson(latex: String): String = withContext(Dispatchers.IO) {
        val ptr = ratex_parse_and_layout(latex)
            ?: throw RuntimeException(ratex_get_last_error()?.toKString() ?: "RaTeX parse error")
        try {
            ptr.toKString()
        } finally {
            ratex_free_display_list(ptr)
        }
    }

    override suspend fun parse(latex: String): DisplayList {
        return latexJson.decodeFromString<DisplayList>(parseJson(latex))
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine = remember { IosMathEngine() }
