package com.mikepenz.markdown.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                TopAppBar(onClick = { showLicenses = !showLicenses })
            },
            modifier = modifier
        ) { padding ->
            if (showLicenses) {
                LicensesPage(libraries = libraries, padding = padding)
            } else {
                Column(modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Dark mode enabled", color = MaterialTheme.colorScheme.onBackground)
                        Switch(checked = darkMode, onCheckedChange = { darkMode = !darkMode })
                    }
                    MarkDownPage()
                }
            }
        }
    }
}
