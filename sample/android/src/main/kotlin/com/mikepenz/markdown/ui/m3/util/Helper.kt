package com.mikepenz.markdown.ui.m3.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownInlineContent
import com.mikepenz.markdown.model.rememberMarkdownState
import com.mikepenz.markdown.ui.m3.theme.SampleTheme
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.getTextInNode

@Composable
fun TestMarkdown(content: String) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Markdown(rememberMarkdownState(content))
    }
}

private const val INLINE_PLACEHOLDER = "⦃check⦄"

@Composable
fun TestMarkdownTableInline(content: String) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        val annotator: MarkdownAnnotator = remember {
            markdownAnnotator { text, child ->
                if (child.type == MarkdownTokenTypes.TEXT) {
                    val nodeText = child.getTextInNode(text).toString()
                    val idx = nodeText.indexOf(INLINE_PLACEHOLDER)
                    if (idx < 0) return@markdownAnnotator false
                    if (idx > 0) append(nodeText.substring(0, idx))
                    appendInlineContent(INLINE_PLACEHOLDER, INLINE_PLACEHOLDER)
                    val tail = idx + INLINE_PLACEHOLDER.length
                    if (tail < nodeText.length) append(nodeText.substring(tail))
                    true
                } else false
            }
        }
        val inline = markdownInlineContent(
            content = mapOf(
                INLINE_PLACEHOLDER to InlineTextContent(
                    Placeholder(
                        width = 16.sp,
                        height = 16.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                    ),
                ) { Text("✅") },
            ),
        )
        Markdown(
            rememberMarkdownState(content),
            annotator = annotator,
            inlineContent = inline,
        )
    }
}

@Composable
fun TestMarkdownCodeBlock(content: String) = SampleTheme(isSystemInDarkTheme()) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Markdown(
            rememberMarkdownState(content),
            components = markdownComponents(
                codeBlock = highlightedCodeBlock,
                codeFence = highlightedCodeFence,
            ),
        )
    }
}
