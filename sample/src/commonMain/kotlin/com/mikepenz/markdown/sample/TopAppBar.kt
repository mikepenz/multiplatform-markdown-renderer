package com.mikepenz.markdown.sample

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.markdown.sample.icon.Github
import com.mikepenz.markdown.sample.icon.OpenSourceInitiative

@Composable
internal fun TopAppBar(
    onClick: () -> Unit
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
