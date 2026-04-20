package com.mikepenz.markdown.latex

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.model.MarkdownInlineContent
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.latex.model.toMathSize
import com.mikepenz.markdown.utils.MARKDOWN_TAG_INLINE_MATH

/**
 * Creates a [MarkdownInlineContent] that renders inline math formulas via [MathEngine].
 * Scans the [AnnotatedString] for `MARKDOWN_TAG_INLINE_MATH` tags, measures each formula,
 * and builds the [InlineTextContent] map — all inside a @Composable context where locals are available.
 *
 * Usage:
 * ```
 * Markdown(
 *     content = markdownText,
 *     inlineContent = mathInlineContent(),
 *     components = markdownComponents(blockMath = latexBlockMath),
 *     ...
 * )
 * ```
 */
fun mathInlineContent(): MarkdownInlineContent = MathInlineContent

@Immutable
private object MathInlineContent : MarkdownInlineContent {
    @Composable
    override fun inlineContent(content: AnnotatedString): Map<String, InlineTextContent> {
        val mathEngine = rememberMathEngine()
        val color = LocalMarkdownColors.current.text
        val fontSize = LocalMarkdownTypography.current.paragraph.fontSize.value

        // Find all inline math annotations
        val mathTags = remember(content) {
            content.getStringAnnotations(0, content.length)
                .filter { it.item.startsWith(MARKDOWN_TAG_INLINE_MATH) }
        }

        if (mathTags.isEmpty()) return emptyMap()

        // Parse each formula once
        val parsed by produceState(initialValue = emptyMap(), key1 = mathTags) {
            val result = mutableMapOf<String, DisplayList>()
            for (tag in mathTags) {
                val latex = content.subSequence(tag.start, tag.end).text
                try {
                    result[tag.item] = mathEngine.parse(latex)
                } catch (_: Exception) {
                    // skip failed formulas
                }
            }
            value = result
        }

        // Build InlineTextContent map — parsed DisplayList used for both sizing and rendering
        return remember(mathTags, parsed, color, fontSize) {
            buildMap {
                for (tag in mathTags) {
                    val dl = parsed[tag.item] ?: continue
                    val size = dl.toMathSize(fontSize)
                    put(tag.item, InlineTextContent(
                        Placeholder(
                            width = size.width.sp,
                            height = size.totalHeight.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                        )
                    ) { _ ->
                        MathCanvas(
                            displayList = dl,
                            fontSize = fontSize,
                            color = color,
                            modifier = Modifier.fillMaxSize(),
                        )
                    })
                }
            }
        }
    }
}
