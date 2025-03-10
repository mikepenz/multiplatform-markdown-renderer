package com.mikepenz.markdown

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.sample.App
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        App()
    }
}
