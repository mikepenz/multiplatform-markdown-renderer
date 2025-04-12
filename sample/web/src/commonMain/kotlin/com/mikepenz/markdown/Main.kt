package com.mikepenz.markdown

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.web.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    CanvasBasedWindow("Markdown Sample", canvasElementId = "markdownCanvas") {
        val libraries by rememberLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
        App(libraries)
    }
}
