package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal data class MarkdownSample(
    val title: String,
    val content: @Composable (Modifier) -> Unit,
)

private val SAMPLES = listOf(
    MarkdownSample("Playground") { MarkDownPage(modifier = it, file = "files/sample.md") },
    MarkdownSample("Streaming") { StreamingMarkDownPage(modifier = it, file = "files/sample.md") },
    MarkdownSample("Tables") { MarkDownPage(modifier = it, file = "files/sample-tables.md") },
    MarkdownSample("Images") { MarkDownPage(modifier = it, file = "files/sample-images.md") },
    MarkdownSample("Flow") { FlowMarkdownPage(modifier = it) },
    MarkdownSample("Recompositions") { RecompositionPage(modifier = it) },
)

@Composable
internal fun SamplesPage(modifier: Modifier = Modifier) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val samples = remember { SAMPLES }

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryScrollableTabRow(selectedTabIndex = selected, edgePadding = 0.dp) {
            samples.forEachIndexed { index, sample ->
                Tab(
                    selected = index == selected,
                    onClick = { selected = index },
                    text = { Text(sample.title) },
                )
            }
        }
        samples[selected].content(Modifier.fillMaxSize())
    }
}
