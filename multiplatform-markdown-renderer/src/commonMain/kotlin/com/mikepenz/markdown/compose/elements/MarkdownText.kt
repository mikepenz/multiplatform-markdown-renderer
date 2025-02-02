package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalMarkdownAnimations
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownExtendedSpans
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.drawBehind
import com.mikepenz.markdown.model.rememberMarkdownImageState
import com.mikepenz.markdown.utils.MARKDOWN_TAG_IMAGE_URL
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType


@Composable
fun MarkdownText(
    content: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
) {
    MarkdownText(AnnotatedString(content), modifier, style)
}

@Composable
fun MarkdownText(
    content: String,
    node: ASTNode,
    style: TextStyle,
    modifier: Modifier = Modifier,
    contentChildType: IElementType? = null,
) {
    val annotatorSettings = annotatorSettings()
    val childNode = contentChildType?.let { node.findChildOfType(it) } ?: node

    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(content = content, node = childNode, annotatorSettings = annotatorSettings)
        pop()
    }

    MarkdownText(styledText, modifier = modifier, style = style)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    extendedSpans: ExtendedSpans? = LocalMarkdownExtendedSpans.current.extendedSpans?.invoke(),
) {
    // extend the annotated string with `extended-spans` styles if provided
    val extendedStyledText = if (extendedSpans != null) {
        remember(content) {
            extendedSpans.extend(content)
        }
    } else {
        content
    }

    // forward the `onTextLayout` to `extended-spans` if provided
    val onTextLayout: (TextLayoutResult) -> Unit = if (extendedSpans != null) {
        { result ->
            extendedSpans.onTextLayout(result)
        }
    } else {
        {}
    }

    // call drawBehind with the `extended-spans` if provided
    val extendedModifier = if (extendedSpans != null) {
        modifier.drawBehind(extendedSpans)
    } else modifier

    MarkdownText(extendedStyledText, extendedModifier, style, onTextLayout)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    onTextLayout: (TextLayoutResult) -> Unit,
) {
    val animations = LocalMarkdownAnimations.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val imageState = rememberMarkdownImageState()

    val transformer = LocalImageTransformer.current
    val placeholderState by derivedStateOf {
        transformer.placeholderConfig(
            imageState.density,
            imageState.containerSize,
            imageState.intrinsicImageSize
        )
    }

    MarkdownBasicText(
        text = content,
        modifier = modifier
            .onPlaced {
                it.parentLayoutCoordinates?.also { coordinates ->
                    imageState.setContainerSize(coordinates.size)
                }
            }
            .let {
                // for backwards compatibility still check the `animate` property
                @Suppress("DEPRECATION")
                if (placeholderState.animate) animations.animateTextSize(it) else it
            },
        style = style,
        color = LocalMarkdownColors.current.text,
        inlineContent = mapOf(
            MARKDOWN_TAG_IMAGE_URL to InlineTextContent(
                Placeholder(
                    width = placeholderState.size.width.sp,
                    height = placeholderState.size.height.sp,
                    placeholderVerticalAlign = placeholderState.verticalAlign
                )
            ) { link ->
                transformer.transform(link)?.let { imageData ->
                    val intrinsicSize = transformer.intrinsicSize(imageData.painter)
                    LaunchedEffect(intrinsicSize) {
                        imageState.setImageSize(intrinsicSize)
                    }
                    Image(
                        painter = imageData.painter,
                        contentDescription = imageData.contentDescription,
                        modifier = imageData.modifier,
                        alignment = imageData.alignment,
                        contentScale = imageData.contentScale,
                        alpha = imageData.alpha,
                        colorFilter = imageData.colorFilter
                    )
                }
            }
        ),
        onTextLayout = {
            layoutResult.value = it
            onTextLayout.invoke(it)
        }
    )
}

