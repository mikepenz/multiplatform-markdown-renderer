package com.mikepenz.markdown

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.mikepenz.markdown.sample.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Markdown Sample", canvasElementId = "markdownCanvas") {
        App()
    }
}
