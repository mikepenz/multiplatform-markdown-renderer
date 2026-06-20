package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.State

@Composable
fun FlowMarkdownPage(
    modifier: Modifier = Modifier,
) {
    val viewModel = remember { FlowMarkdownViewModel() }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispose()
        }
    }

    val retainState by viewModel.retainState.collectAsState()
    val autoUpdate by viewModel.autoUpdate.collectAsState()
    val updateCount by viewModel.updateCount.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().safeContentPadding()
    ) {

        Text(
            text = "Flow<String>.asMarkdownState() Demo",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Retain State:")
            Switch(retainState, onCheckedChange = { viewModel.setRetainState(it) })
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto Update:")
            Switch(autoUpdate, onCheckedChange = { viewModel.setAutoUpdate(it) })
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.updateContent() }) {
                Text("Update Content")
            }

            Button(onClick = { viewModel.reset() }) {
                Text("Reset")
            }
        }

        Text("Updates: $updateCount", color = MaterialTheme.colorScheme.onBackground)

        // Demonstrate Flow<String>.asMarkdownState()
        val markdownState by viewModel.markdownState.collectAsState()

        // Remember scroll state outside the when block to preserve it across state changes
        val scrollState = rememberScrollState()

        when (markdownState) {
            is State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is State.Success -> {
                Markdown(
                    state = markdownState,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                )
            }
            is State.Error -> {
                val errorState = markdownState as State.Error
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${errorState.result.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
