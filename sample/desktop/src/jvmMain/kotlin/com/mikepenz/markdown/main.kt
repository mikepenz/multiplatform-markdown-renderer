package com.mikepenz.markdown

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.desktop.resources.Res

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        val libraries by produceLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
        App(libraries)
    }
}
