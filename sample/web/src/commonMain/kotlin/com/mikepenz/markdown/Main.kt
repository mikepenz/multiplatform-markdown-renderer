package com.mikepenz.markdown

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.web.resources.Res

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        val libraries by produceLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
        App(libraries)
    }
}
