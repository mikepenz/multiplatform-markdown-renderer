// Copyright 2023, Saket Narayan
// SPDX-License-Identifier: Apache-2.0
// https://github.com/saket/extended-spans
package com.mikepenz.markdown.compose.extendedspans

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.mikepenz.markdown.compose.extendedspans.internal.deserializeToColor
import com.mikepenz.markdown.compose.extendedspans.internal.serialize

/**
 * Draws round rectangles behind text annotated using `SpanStyle(background = …)`.
 *
 * [topMargin] and [bottomMargin] are placeholder values that will be automatically calculated from font metrics
 * in the future once Compose UI starts exposing them ([Issue tracker](https://issuetracker.google.com/u/1/issues/237428541)).
 * In the meantime, you can calculate these depending upon your text's font size and line height.
 */
class RoundedCornerSpanPainter(
    private val cornerRadius: TextUnit = 8.sp,
    private val stroke: Stroke? = null,
    private val padding: TextPaddingValues = TextPaddingValues(horizontal = 2.sp, vertical = 2.sp),
    private val topMargin: TextUnit = 1.sp,
    private val bottomMargin: TextUnit = 1.sp,
) : ExtendedSpanPainter() {
    private val path = Path()

    override fun decorate(
        span: SpanStyle,
        start: Int,
        end: Int,
        text: AnnotatedString,
        builder: AnnotatedString.Builder,
    ): SpanStyle {
        return if (span.background.isUnspecified) {
            span
        } else {
            builder.addStringAnnotation(TAG, annotation = span.background.serialize(), start = start, end = end)
            span.copy(background = Color.Unspecified)
        }
    }

    override fun decorate(
        linkAnnotation: LinkAnnotation,
        start: Int,
        end: Int,
        text: AnnotatedString,
        builder: AnnotatedString.Builder,
    ): LinkAnnotation {
        val defaultStyle = linkAnnotation.styles?.style
        // return fast if background is not set
        if (defaultStyle == null || defaultStyle.background.isUnspecified == true) return linkAnnotation
        val updated = decorate(defaultStyle, start, end, text, builder)
        return if (linkAnnotation is LinkAnnotation.Url) {
            LinkAnnotation.Url(linkAnnotation.url, TextLinkStyles(updated), linkAnnotation.linkInteractionListener)
        } else if (linkAnnotation is LinkAnnotation.Clickable) {
            LinkAnnotation.Clickable(linkAnnotation.tag, TextLinkStyles(updated), linkAnnotation.linkInteractionListener)
        } else {
            throw IllegalStateException("Unsupported LinkAnnotation type: $linkAnnotation")
        }
    }

    override fun drawInstructionsFor(layoutResult: TextLayoutResult): SpanDrawInstructions {
        val text = layoutResult.layoutInput.text
        val annotations = text.getStringAnnotations(TAG, start = 0, end = text.length)
        val linkAnnotations = text.getLinkAnnotations(start = 0, end = text.length)

        return SpanDrawInstructions {
            val cornerRadius = CornerRadius(cornerRadius.toPx())

            fun drawForAnnotation(start: Int, end: Int, color: Color?) {
                val backgroundColor = color!!
                val boxes = layoutResult.getBoundingBoxes(
                    startOffset = start,
                    endOffset = end,
                    flattenForFullParagraphs = true
                )
                boxes.fastForEachIndexed { index, box ->
                    path.rewind()
                    path.addRoundRect(
                        RoundRect(
                            rect = box.copy(
                                left = box.left - padding.horizontal.toPx(),
                                right = box.right + padding.horizontal.toPx(),
                                top = box.top - padding.vertical.toPx() + topMargin.toPx(),
                                bottom = box.bottom + padding.vertical.toPx() - bottomMargin.toPx(),
                            ),
                            topLeft = if (index == 0) cornerRadius else CornerRadius.Zero,
                            bottomLeft = if (index == 0) cornerRadius else CornerRadius.Zero,
                            topRight = if (index == boxes.lastIndex) cornerRadius else CornerRadius.Zero,
                            bottomRight = if (index == boxes.lastIndex) cornerRadius else CornerRadius.Zero
                        )
                    )
                    drawPath(
                        path = path,
                        color = backgroundColor,
                        style = Fill
                    )
                    if (stroke != null) {
                        drawPath(
                            path = path,
                            color = stroke.color(backgroundColor),
                            style = Stroke(
                                width = stroke.width.toPx(),
                            )
                        )
                    }
                }
            }

            annotations.fastForEach { annotation ->
                drawForAnnotation(annotation.start, annotation.end, annotation.item.deserializeToColor())
            }
            linkAnnotations.fastForEach { annotation ->
                drawForAnnotation(annotation.start, annotation.end, annotation.item.styles?.style?.background)
            }
        }
    }

    data class Stroke(
        val color: (background: Color) -> Color,
        val width: TextUnit = 1.sp,
    ) {
        constructor(color: Color, width: TextUnit = 1.sp) : this(
            color = { color },
            width = width
        )
    }

    data class TextPaddingValues(
        val horizontal: TextUnit = 0.sp,
        val vertical: TextUnit = 0.sp,
    )

    companion object {
        private const val TAG = "rounded_corner_span"
    }
}