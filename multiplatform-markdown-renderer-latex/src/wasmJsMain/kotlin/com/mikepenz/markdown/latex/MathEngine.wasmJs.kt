@file:OptIn(ExperimentalWasmJsInterop::class)

package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.latex.model.latexJson
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.Promise
import kotlinx.coroutines.await

@JsFun(
    """
async () => {
    if (!globalThis.__ratexMod) {
        const mod = await import('ratex-wasm');
        await mod.initRatex();
        globalThis.__ratexMod = mod;
    }
}
"""
)
private external fun jsInitRatex(): Promise<JsAny?>

@JsFun("(latex) => globalThis.__ratexMod.renderLatex(latex)")
private external fun jsRenderLatex(latex: String): String

internal actual val isAsyncFontLoading: Boolean = true

private class WasmJsMathEngine : MathEngine {
    private var initialized = false

    override suspend fun parse(latex: String): DisplayList {
        if (!initialized) {
            jsInitRatex().await<JsAny?>()
            initialized = true
        }
        val json = jsRenderLatex(latex)
        return latexJson.decodeFromString<DisplayList>(json)
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine = remember { WasmJsMathEngine() }
