package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.sample.icon.Debug
import com.mikepenz.markdown.sample.icon.Github
import com.mikepenz.markdown.sample.icon.OpenSourceInitiative

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopAppBar(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    debugClick: () -> Unit,
    onClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    TopAppBar(
        title = { Text("Markdown") },
        actions = {
            IconButton(onClick = debugClick) {
                Icon(
                    imageVector = Debug,
                    contentDescription = "Debug Recompositions"
                )
            }
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = OpenSourceInitiative,
                    contentDescription = "Open Source"
                )
            }
            IconButton(onClick = {
                uriHandler.openUri("https://github.com/mikepenz/multiplatform-markdown-renderer")
            }) {
                Icon(
                    imageVector = Github,
                    contentDescription = "GitHub"
                )
            }
            Switch(
                checked = isDarkMode,
                onCheckedChange = onThemeToggle,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    )
}
