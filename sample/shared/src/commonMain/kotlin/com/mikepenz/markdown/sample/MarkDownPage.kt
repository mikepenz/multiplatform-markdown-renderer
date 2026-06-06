package com.mikepenz.markdown.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.CurrentComponentsBridge
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.elements.MarkdownCheckBox
import com.mikepenz.markdown.model.markdownAnnotatorConfig
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.model.rememberStreamingMarkdownState
import com.mikepenz.markdown.sample.shared.resources.Res
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.time.measureTime

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun MarkDownPage(
    modifier: Modifier = Modifier,
    file: String = "files/sample.md",
) {
    val isDarkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }

    SelectionContainer {
        Markdown(
            markdownState = rememberMarkdownState(file) {
                Res.readBytes(file).decodeToString()
            },
            components = markdownComponents(
                codeBlock = {
                    MarkdownHighlightedCodeBlock(
                        content = it.content,
                        node = it.node,
                        highlightsBuilder = highlightsBuilder,
                        showHeader = true,
                    )
                },
                codeFence = {
                    MarkdownHighlightedCodeFence(
                        content = it.content,
                        node = it.node,
                        highlightsBuilder = highlightsBuilder,
                        showHeader = true,
                    )
                },
                checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) },
            ),
            annotator = rememberCheckAnnotator(markdownAnnotatorConfig(showImageAltTooltip = true)),
            inlineContent = rememberCheckInlineContent(),
            imageTransformer = Coil3ImageTransformerImpl,
            extendedSpans = markdownExtendedSpans {
                val animator = rememberSquigglyUnderlineAnimator()
                remember {
                    ExtendedSpans(
                        RoundedCornerSpanPainter(),
                        SquigglyUnderlineSpanPainter(animator = animator)
                    )
                }
            },
            loading = {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun StreamingMarkDownPage(
    modifier: Modifier = Modifier,
    file: String = "files/sample.md",
) {
    val isDarkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }
    var emittedChunks by remember(file) { mutableIntStateOf(0) }
    var totalChunks by remember(file) { mutableIntStateOf(0) }
    var appendMicros by remember(file) { mutableLongStateOf(0L) }
    val renderStats = remember(file) { StreamingRenderStats() }
    var renderProbe by remember(file) { mutableStateOf(StreamingRenderStatsSnapshot()) }
    val chunkFlow = remember(file) {
        flow {
            val chunks = Res.readBytes(file).decodeToString().chunked(24)
            totalChunks = chunks.size
            chunks.forEach { chunk ->
                emit(chunk)
                delay(120)
            }
        }
    }
    val streamingMarkdownState = rememberStreamingMarkdownState()
    val snapshot by streamingMarkdownState.snapshot.collectAsState()
    val links by streamingMarkdownState.links.collectAsState()
    val components = remember(highlightsBuilder, renderStats) {
        markdownComponents(
            text = counted(renderStats, CurrentComponentsBridge.text),
            eol = counted(renderStats, CurrentComponentsBridge.eol),
            codeFence = counted(renderStats) {
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    highlightsBuilder = highlightsBuilder,
                    showHeader = true,
                )
            },
            codeBlock = counted(renderStats) {
                MarkdownHighlightedCodeBlock(
                    content = it.content,
                    node = it.node,
                    highlightsBuilder = highlightsBuilder,
                    showHeader = true,
                )
            },
            heading1 = counted(renderStats, CurrentComponentsBridge.heading1),
            heading2 = counted(renderStats, CurrentComponentsBridge.heading2),
            heading3 = counted(renderStats, CurrentComponentsBridge.heading3),
            heading4 = counted(renderStats, CurrentComponentsBridge.heading4),
            heading5 = counted(renderStats, CurrentComponentsBridge.heading5),
            heading6 = counted(renderStats, CurrentComponentsBridge.heading6),
            setextHeading1 = counted(renderStats, CurrentComponentsBridge.setextHeading1),
            setextHeading2 = counted(renderStats, CurrentComponentsBridge.setextHeading2),
            blockQuote = counted(renderStats, CurrentComponentsBridge.blockQuote),
            paragraph = counted(renderStats, CurrentComponentsBridge.paragraph),
            orderedList = counted(renderStats, CurrentComponentsBridge.orderedList),
            unorderedList = counted(renderStats, CurrentComponentsBridge.unorderedList),
            image = counted(renderStats, CurrentComponentsBridge.image),
            inlineImage = counted(renderStats, CurrentComponentsBridge.inlineImage),
            horizontalRule = counted(renderStats, CurrentComponentsBridge.horizontalRule),
            table = counted(renderStats, CurrentComponentsBridge.table),
            checkbox = counted(renderStats) {
                MarkdownCheckBox(it.content, it.node, it.typography.text)
            },
        )
    }

    LaunchedEffect(chunkFlow, streamingMarkdownState) {
        chunkFlow.collect { chunk ->
            val elapsed = measureTime {
                streamingMarkdownState.append(chunk)
            }
            emittedChunks += 1
            appendMicros += elapsed.inWholeMicroseconds
        }
    }
    LaunchedEffect(snapshot) {
        renderProbe = renderStats.snapshot()
    }

    SelectionContainer {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Flow chunks: $emittedChunks / $totalChunks | chars: ${streamingMarkdownState.content.length}",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Append cost: ${appendMicros / 1000.0} ms total",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Snapshot: stable=${snapshot.stableAst.size}, tail=${snapshot.unstableAstTail.size}, links=${links.size}",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Render probe: calls=${renderProbe.componentCalls}, uniqueNodes=${renderProbe.uniqueNodes}",
                color = MaterialTheme.colorScheme.onBackground,
            )

            Markdown(
                streamingMarkdownState = streamingMarkdownState,
                components = components,
                annotator = rememberCheckAnnotator(markdownAnnotatorConfig(showImageAltTooltip = true)),
                inlineContent = rememberCheckInlineContent(),
                imageTransformer = Coil3ImageTransformerImpl,
                extendedSpans = markdownExtendedSpans {
                    val animator = rememberSquigglyUnderlineAnimator()
                    remember {
                        ExtendedSpans(
                            RoundedCornerSpanPainter(),
                            SquigglyUnderlineSpanPainter(animator = animator)
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private data class StreamingRenderStatsSnapshot(
    val componentCalls: Int = 0,
    val uniqueNodes: Int = 0,
)

private class StreamingRenderStats {
    private var componentCalls = 0
    private val nodes = mutableSetOf<String>()

    fun record(model: MarkdownComponentModel) {
        componentCalls += 1
        nodes += "${model.node.type}:${model.node.startOffset}:${model.node.endOffset}"
    }

    fun snapshot() = StreamingRenderStatsSnapshot(
        componentCalls = componentCalls,
        uniqueNodes = nodes.size,
    )
}

private fun counted(
    stats: StreamingRenderStats,
    component: MarkdownComponent,
): MarkdownComponent = { model ->
    stats.record(model)
    component(model)
}
