package com.mikepenz.markdown.sample

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.markdown.sample.icon.Github
import com.mikepenz.markdown.sample.icon.OpenSourceInitiative

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopAppBar(
    onClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    TopAppBar(
        title = { Text("Markdown Sample") },
        actions = {
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
        }
    )
}
