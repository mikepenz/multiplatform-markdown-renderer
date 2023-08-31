package com.mikepenz.markdown.compose.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.LocalReferenceLinkHandler
import com.mikepenz.markdown.model.rememberMarkdownImageState
import com.mikepenz.markdown.utils.TAG_IMAGE_URL
import com.mikepenz.markdown.utils.TAG_URL


@Composable
internal fun MarkdownText(
    content: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text
) {
    MarkdownText(AnnotatedString(content), modifier, style)
}

@Composable
internal fun MarkdownText(
    content: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text
) {
    val uriHandler = LocalUriHandler.current
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val imageState = rememberMarkdownImageState()

    val hasUrl = content.getStringAnnotations(TAG_URL, 0, content.length).any()
    val textModifier = if (hasUrl) modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val position = layoutResult.getOffsetForPosition(pos)
                content.getStringAnnotations(TAG_URL, position, position)
                    .firstOrNull()
                    ?.let { uriHandler.openUri(referenceLinkHandler.find(it.item)) }
            }
        }
    } else modifier


    Text(
        text = content,
        modifier = textModifier
            .onPlaced {
                imageState.setContainerSize(it.parentLayoutCoordinates!!.size)
            }
            .animateContentSize(),
        style = style,
        color = LocalMarkdownColors.current.text,
        inlineContent = mapOf(TAG_IMAGE_URL to InlineTextContent(
            Placeholder(
                width = imageState.imageSize.width,
                height = imageState.imageSize.height,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
            )
        ) { link ->

            val transformer = LocalImageTransformer.current

            transformer.transform(link)?.let {

                val intrinsicSize = transformer.intrinsicSize(it)

                LaunchedEffect(intrinsicSize) {
                    imageState.setImageSize(intrinsicSize)
                }

                Image(
                    painter = it,
                    contentDescription = "Image",
                    alignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }),
        onTextLayout = { layoutResult.value = it }
    )
}
