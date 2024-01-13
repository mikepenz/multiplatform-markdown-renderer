package com.mikepenz.markdown.compose.elements.material

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
internal fun Text(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    // TL:DR: profile before you change any line of code in this method
    //
    // The call to LocalContentAlpha.current looks like it can be avoided by only calling it in the
    // last else block but, in 1.5, this causes a control flow group to be created because it would
    // be a conditional call to a composable function. The call is currently made unconditionally
    // since the call to LocalContentAlpha.current does not create a group (it is a read-only
    // composable) and looking up the value in the composition locals map is currently faster than
    // creating a group to avoid it.
    //
    // Similar notes regarding lambda allocations. It appears there's a path to optimize for
    // zero-allocations in the style-provided color route, but this either introduces a group or a
    // box depending on how it's coded. It's also possible that allocating a final ColorProducer
    // subclass with no capture may be a successful optimization, but it appeared slower in initial
    // profiling.
    //
    // If changing ANY LINE OF CODE, please confirm that it's faster or the same speed using
    // profilers and benchmarks.
    val overrideColorOrUnspecified: Color = if (color.isSpecified) {
        color
    } else if (style.color.isSpecified) {
        style.color
    } else {
        Color.Black // TODO
    }

    BasicText(
        text = text,
        modifier = modifier,
        style = style.merge(
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        ),
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        color = { overrideColorOrUnspecified }
    )
}

@Composable
internal fun Text(
    text: AnnotatedString,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    // TL:DR: profile before you change any line of code in this method
    //
    // The call to LocalContentAlpha.current looks like it can be avoided by only calling it in the
    // last else block but, in 1.5, this causes a control flow group to be created because it would
    // be a conditional call to a composable function. The call is currently made unconditionally
    // since the call to LocalContentAlpha.current does not create a group (it is a read-only
    // composable) and looking up the value in the composition locals map is currently faster than
    // creating a group to avoid it.
    //
    // Similar notes regarding lambda allocations. It appears there's a path to optimize for
    // zero-allocations in the style-provided color route, but this either introduces a group or a
    // box depending on how it's coded. It's also possible that allocating a final ColorProducer
    // subclass with no capture may be a successful optimization, but it appeared slower in initial
    // profiling.
    //
    // If changing ANY LINE OF CODE, please confirm that it's faster or the same speed using
    // profilers and benchmarks.

    val overrideColorOrUnspecified = if (color.isSpecified) {
        color
    } else if (style.color.isSpecified) {
        style.color
    } else {
        Color.Black // TODO
    }

    BasicText(
        text = text,
        modifier = modifier,
        style = style.merge(
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        ),
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        color = { overrideColorOrUnspecified }
    )
}
