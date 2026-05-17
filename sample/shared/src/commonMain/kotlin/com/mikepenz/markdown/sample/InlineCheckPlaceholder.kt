package com.mikepenz.markdown.sample

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownInlineContent
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownAnnotatorConfig
import com.mikepenz.markdown.model.markdownInlineContent
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.getTextInNode

/** Demo placeholder token (issue #511); rendered as ✅ via inline content. */
const val CHECK_PLACEHOLDER = "⦃check⦄"

@Composable
fun rememberCheckAnnotator(
    config: MarkdownAnnotatorConfig = markdownAnnotatorConfig(),
): MarkdownAnnotator = remember(config) {
    markdownAnnotator(config) { text, child ->
        if (child.type != MarkdownTokenTypes.TEXT) return@markdownAnnotator false
        val nodeText = child.getTextInNode(text).toString()
        val idx = nodeText.indexOf(CHECK_PLACEHOLDER)
        if (idx < 0) return@markdownAnnotator false
        if (idx > 0) append(nodeText.substring(0, idx))
        appendInlineContent(CHECK_PLACEHOLDER, CHECK_PLACEHOLDER)
        val tail = idx + CHECK_PLACEHOLDER.length
        if (tail < nodeText.length) append(nodeText.substring(tail))
        true
    }
}

@Composable
fun rememberCheckInlineContent(): MarkdownInlineContent {
    val map = remember {
        mapOf(
            CHECK_PLACEHOLDER to InlineTextContent(
                Placeholder(
                    width = 16.sp,
                    height = 16.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) @Composable { Text("✅") },
        )
    }
    return markdownInlineContent(content = map)
}
