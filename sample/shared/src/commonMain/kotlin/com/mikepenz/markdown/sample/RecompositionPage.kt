package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownDimens
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.markdownInlineContent
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.utils.MarkdownLogger

@Composable
fun RecompositionPage(
    modifier: Modifier = Modifier,
) {
    DisposableEffect(Unit) {
        MarkdownLogger.enabled = true
        onDispose {
            MarkdownLogger.enabled = false
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().safeContentPadding()
    ) {
        var retainState by remember {
            mutableStateOf(true)
        }
        var includeCounter by remember {
            mutableStateOf(false)
        }
        var counter by remember {
            mutableIntStateOf(0)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Include Counter:")
            Switch(includeCounter, onCheckedChange = { includeCounter = it })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Retain State:")
            Switch(retainState, onCheckedChange = { retainState = it })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { counter++ }) {
                Text("Increase")
            }

            Button(onClick = { counter-- }) {
                Text("Decrease")
            }
        }

        Text("Count: $counter", color = MaterialTheme.colorScheme.onBackground)
        
        val markdownState = rememberMarkdownState(
            if (includeCounter) counter else 0,
            retainState = retainState
        ) {
            if (includeCounter) {
                "## My markdown text $counter"
            } else {
                "## My markdown text"
            }
        }
        Markdown(
            markdownState = markdownState,
            dimens = markdownDimens(),
            colors = markdownColor(),
            padding = markdownPadding(),
            typography = markdownTypography(),
            inlineContent = markdownInlineContent(),
            animations = markdownAnimations(),
            extendedSpans = markdownExtendedSpans(),
            modifier = Modifier,
        )
    }
}