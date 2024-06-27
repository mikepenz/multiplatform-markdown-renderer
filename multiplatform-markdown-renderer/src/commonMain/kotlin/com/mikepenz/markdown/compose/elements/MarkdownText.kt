package com.mikepenz.markdown.compose.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownExtendedSpans
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.LocalReferenceLinkHandler
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.drawBehind
import com.mikepenz.markdown.model.rememberMarkdownImageState
import com.mikepenz.markdown.utils.MARKDOWN_TAG_IMAGE_URL
import com.mikepenz.markdown.utils.MARKDOWN_TAG_URL


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
    content: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    extendedSpans: ExtendedSpans? = LocalMarkdownExtendedSpans.current.extendedSpans?.invoke()
) {
    // extend the annotated string with extended spans styles if provided
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

    // call drawBehind with the `exended-spans` if provided
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
    val uriHandler = LocalUriHandler.current
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val imageState = rememberMarkdownImageState()

    val hasUrl = content.getStringAnnotations(MARKDOWN_TAG_URL, 0, content.length).any()
    val textModifier = if (hasUrl) modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val position = layoutResult.getOffsetForPosition(pos)
                content.getStringAnnotations(MARKDOWN_TAG_URL, position, position).reversed().firstOrNull()
                    ?.let {
                        val foundReference = referenceLinkHandler.find(it.item)
                        try {
                            uriHandler.openUri(foundReference)
                        } catch (t: Throwable) {
                            println("Could not open the provided url: $foundReference")
                        }
                    }
            }
        }
    } else modifier

    MarkdownBasicText(
        text = content,
        modifier = textModifier
            .onPlaced {
                it.parentLayoutCoordinates?.also { coordinates ->
                    imageState.setContainerSize(coordinates.size)
                }
            }
            .animateContentSize(),
        style = style,
        color = LocalMarkdownColors.current.text,
        inlineContent = mapOf(MARKDOWN_TAG_IMAGE_URL to InlineTextContent(
            Placeholder(
                width = imageState.imageSize.width.sp,
                height = imageState.imageSize.height.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
            )
        ) { link ->
            val transformer = LocalImageTransformer.current

            transformer.transform(link)?.let { imageData ->
                val intrinsicSize = transformer.intrinsicSize(imageData.painter)

                LaunchedEffect(intrinsicSize) {
                    imageState.setImageSize(intrinsicSize)
                }

                Image(
                    painter = imageData.painter,
                    contentDescription = imageData.contentDescription,
                    alignment = imageData.alignment,
                    modifier = imageData.modifier
                )
            }
        }),
        onTextLayout = {
            layoutResult.value = it
            onTextLayout?.invoke(it)
        }
    )
}
