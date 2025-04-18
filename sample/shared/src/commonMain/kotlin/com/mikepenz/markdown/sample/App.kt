package com.mikepenz.markdown.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.markdown.sample.theme.SampleTheme

@Composable
fun App(
    libraries: Libs?,
    modifier: Modifier = Modifier,
) {
    val isSystemInDarkMode = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(isSystemInDarkMode) }
    var showLicenses by remember { mutableStateOf(false) }
    SampleTheme(darkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    isDarkMode = darkMode,
                    onThemeToggle = { darkMode = !darkMode },
                    onClick = { showLicenses = !showLicenses }
                )
            },
            modifier = modifier
        ) { contentPadding ->
            if (showLicenses) {
                LicensesPage(libraries = libraries, contentPadding = contentPadding)
            } else {
                MarkDownPage(modifier = Modifier.padding(contentPadding))
            }
        }
    }
}
