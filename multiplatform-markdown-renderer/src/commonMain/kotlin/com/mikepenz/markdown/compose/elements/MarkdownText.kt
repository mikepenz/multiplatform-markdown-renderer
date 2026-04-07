package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalImageWidth
import com.mikepenz.markdown.compose.LocalMarkdownAnimations
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.LocalMarkdownExtendedSpans
import com.mikepenz.markdown.compose.LocalMarkdownInlineContent
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.drawBehind
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.ImageWidth
import com.mikepenz.markdown.utils.MARKDOWN_TAG_IMAGE_URL
import kotlinx.collections.immutable.toPersistentMap
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
fun MarkdownText(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
) {
    MarkdownText(AnnotatedString(content), node, modifier, style)
}

@Composable
fun MarkdownText(
    content: String,
    node: ASTNode,
    style: TextStyle,
    modifier: Modifier = Modifier,
    contentChildType: IElementType? = null,
    annotatorSettings: AnnotatorSettings = annotatorSettings(),
) {
    val childNode = contentChildType?.run(node::findChildOfType) ?: node
    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(
            content = content,
            node = childNode,
            annotatorSettings = annotatorSettings
        )
        pop()
    }

    MarkdownText(styledText, node, modifier = modifier, style = style)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    node: ASTNode,
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
    val onTextLayout: ((TextLayoutResult, Color?) -> Unit)? = extendedSpans?.let {
        { layoutResult, color ->
            extendedSpans.onTextLayout(layoutResult, color)
        }
    }

    // call drawBehind with the `extended-spans` if provided
    val extendedModifier = if (extendedSpans != null) {
        modifier.drawBehind(extendedSpans)
    } else modifier

    MarkdownText(extendedStyledText, node, extendedModifier, style, onTextLayout)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    onTextLayout: ((TextLayoutResult, Color?) -> Unit)?,
) {
    val baseColor = LocalMarkdownColors.current.text
    val animations = LocalMarkdownAnimations.current
    val transformer = LocalImageTransformer.current
    val inlineContent = LocalMarkdownInlineContent.current
    val inlineImageWidth = LocalImageWidth.current
    val density = LocalDensity.current

    val layoutResult: MutableState<TextLayoutResult?> = remember { mutableStateOf(null) }
    val containerSize = remember { mutableStateOf(Size.Unspecified) }
    val imageSizeByLink = remember { mutableStateMapOf<String, Size>() }

    val userInlineContent = inlineContent.inlineContent(content)

    MarkdownBasicText(
        text = content,
        modifier = modifier
            .onPlaced {
                it.parentLayoutCoordinates?.also { coordinates ->
                    containerSize.value = coordinates.size.toSize()
                }
            }
            .let {
                animations.animateTextSize(it)
            },
        style = style,
        inlineContent = imageSizeByLink.toPersistentMap().let { imageSizeByLinkSnapshot ->
            remember(node, userInlineContent, content, containerSize.value, transformer, inlineImageWidth, imageSizeByLinkSnapshot) {
                userInlineContent + buildImageInlineContent(
                    content,
                    node,
                    transformer,
                    density,
                    containerSize.value,
                    inlineImageWidth,
                    imageSizeByLinkSnapshot,
                    imageSizeChanged = { link, size -> imageSizeByLink += (link to size) }
                )
            }
        },
        onTextLayout = {
            layoutResult.value = it
            onTextLayout?.invoke(it, baseColor)
        }
    )
}

private fun buildImageInlineContent(
    content: AnnotatedString,
    node: ASTNode,
    transformer: ImageTransformer,
    density: Density,
    containerSize: Size,
    inlineImageWidth: ImageWidth,
    imageSizeByLink: Map<String, Size>,
    defaultImageSize: Size = Size.Unspecified,
    imageSizeChanged: ((link: String, Size) -> Unit)? = null,
): Map<String, InlineTextContent> {
    return content.getStringAnnotations(0, content.length)
        .filter { it.item.startsWith("${MARKDOWN_TAG_IMAGE_URL}_") }
        .distinctBy { it.item }
        .associate { annotation ->
            val url = annotation.item.removePrefix("${MARKDOWN_TAG_IMAGE_URL}_")

            // Try to get stored size, or use default
            val imageSize = imageSizeByLink[url] ?: defaultImageSize

            val config = transformer.placeholderConfig(url, density, containerSize, inlineImageWidth, imageSize, imageSizeChanged)
            // Config size is in DP, convert to SP for Placeholder TextUnit
            annotation.item to InlineTextContent(
                Placeholder(
                    width = with(density) { config.size.width.dp.toSp() },
                    height = with(density) { config.size.height.dp.toSp() },
                    placeholderVerticalAlign = config.verticalAlign
                )
            ) { link ->
                // Render the image and observe its intrinsic size
                MarkdownInlineImageWithSize(
                    link = link,
                    node = node,
                    transformer = transformer,
                    density = density,
                    onSizeDetected = { detectedSize ->
                        // Update size cache when image loads
                        imageSizeChanged?.invoke(url, detectedSize)
                    }
                )
            }
        }
}

@Composable
private fun MarkdownInlineImageWithSize(
    link: String,
    node: ASTNode,
    transformer: ImageTransformer,
    density: Density,
    onSizeDetected: (Size) -> Unit,
) {
    val imageData = transformer.transform(link)

    // Detect intrinsic size when painter becomes available
    val intrinsicSize = imageData?.let { transformer.intrinsicSize(it.painter) } ?: Size.Unspecified
    if (intrinsicSize != Size.Unspecified) {
        SideEffect { onSizeDetected(intrinsicSize) }
    }

    // Delegate to the customizable component
    LocalMarkdownComponents.current.inlineImage(
        MarkdownComponentModel(link, node, LocalMarkdownTypography.current)
    )
}
