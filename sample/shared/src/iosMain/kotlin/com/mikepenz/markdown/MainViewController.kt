package com.mikepenz.markdown

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.shared.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun MainViewController() = ComposeUIViewController {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    App(libraries = libraries)
}
