package com.mikepenz.markdown

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.shared.resources.Res

fun MainViewController() = ComposeUIViewController {
    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    App(libraries = libraries)
}
