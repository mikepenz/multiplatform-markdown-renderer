package com.mikepenz.markdown

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.sample.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        App()
    }
}
