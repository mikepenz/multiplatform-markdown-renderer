package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.*
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.drawBehind
import com.mikepenz.markdown.model.rememberMarkdownImageState
import com.mikepenz.markdown.utils.*
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
    val annotator = LocalMarkdownAnnotator.current
    val linkTextSpanStyle = LocalMarkdownTypography.current.linkTextSpanStyle
    val codeSpanStyle = LocalMarkdownTypography.current.codeSpanStyle
    val childNode = contentChildType?.let { node.findChildOfType(it) } ?: node

    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(content, childNode, linkTextSpanStyle, codeSpanStyle, annotator)
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
    val uriHandler = LocalUriHandler.current
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val imageState = rememberMarkdownImageState()

    val hasUrl = content.getStringAnnotations(MARKDOWN_TAG_URL, 0, content.length).any()
    val textModifier = if (hasUrl) modifier.pointerInput(Unit) {
        awaitEachGesture {
            val pointer = awaitFirstDown()
            val pos = pointer.position // current position

            val foundReference = layoutResult.value?.let { layoutResult ->
                val position = layoutResult.getOffsetForPosition(pos)
                content.getStringAnnotations(MARKDOWN_TAG_URL, position, position).reversed().firstOrNull()
                    ?.let { referenceLinkHandler.find(it.item) }
            }

            if (foundReference != null) {
                pointer.consume() // consume if we clicked on a link

                val up = waitForUpOrCancellation()
                if (up != null) {
                    up.consume()

                    // wait for finger up to navigate to the link
                    try {
                        uriHandler.openUri(foundReference)
                    } catch (t: Throwable) {
                        println("Could not open the provided url: $foundReference")
                    }
                }
            }
        }
    } else modifier

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
        modifier = textModifier
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

