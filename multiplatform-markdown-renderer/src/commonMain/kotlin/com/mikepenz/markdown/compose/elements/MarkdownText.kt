package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
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
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.toSize
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.mikepenz.markdown.compose.LocalImageWidth
import com.mikepenz.markdown.compose.LocalMarkdownAnimations
import com.mikepenz.markdown.compose.LocalMarkdownAnnotator
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
import com.mikepenz.markdown.model.MarkdownAnnotatorConfig
import com.mikepenz.markdown.utils.MARKDOWN_TAG_IMAGE_URL
import com.mikepenz.markdown.utils.findChildOfTypeRecursive
import com.mikepenz.markdown.utils.getUnescapedTextInNode
import kotlinx.collections.immutable.toPersistentMap
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
fun MarkdownText(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
) {
    MarkdownText(AnnotatedString(content), node, modifier, style, sourceContent = content)
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

    MarkdownText(content = styledText, node = node, modifier = modifier, style = style, sourceContent = content)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    extendedSpans: ExtendedSpans? = LocalMarkdownExtendedSpans.current.extendedSpans?.invoke(),
    sourceContent: String? = null,
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

    MarkdownText(extendedStyledText, node, extendedModifier, style, onTextLayout, sourceContent)
}

@Composable
fun MarkdownText(
    content: AnnotatedString,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    onTextLayout: ((TextLayoutResult, Color?) -> Unit)?,
    sourceContent: String? = null,
) {
    val baseColor = LocalMarkdownColors.current.text
    val animations = LocalMarkdownAnimations.current
    val transformer = LocalImageTransformer.current
    val inlineContent = LocalMarkdownInlineContent.current
    val inlineImageWidth = LocalImageWidth.current
    val density = LocalDensity.current
    val annotatorConfig = LocalMarkdownAnnotator.current.config

    val layoutResult: MutableState<TextLayoutResult?> = remember { mutableStateOf(null) }
    val containerSize = remember { mutableStateOf(Size.Unspecified) }
    val imageSizeByLink = remember { mutableStateMapOf<String, Size>() }

    // Resolved line height in pixels; used to decide whether an image
    // should be inline or promoted to a block element.
    val lineHeightPx = with(density) {
        val lh = if (style.lineHeight.isSpecified) style.lineHeight else style.fontSize
        if (lh.isSpecified) lh.toPx() else 0f
    }

    val imageSizeByLinkSnapshot = imageSizeByLink.toPersistentMap()
    val inlineImageAsBlock = annotatorConfig.inlineImageAsBlock
    val (resolvedInlineContent, blockImageRanges) = remember(
        node,
        inlineContent.inlineContent,
        content,
        containerSize.value,
        transformer,
        inlineImageWidth,
        imageSizeByLinkSnapshot,
        lineHeightPx,
        inlineImageAsBlock
    ) {
        val blocks = mutableListOf<BlockImageRange>()
        val map = inlineContent.inlineContent + buildImageInlineContent(
            content = content,
            node = node,
            transformer = transformer,
            density = density,
            containerSize = containerSize.value,
            inlineImageWidth = inlineImageWidth,
            imageSizeByLink = imageSizeByLinkSnapshot,
            lineHeightPx = lineHeightPx,
            inlineImageAsBlock = inlineImageAsBlock,
            onBlockImage = { url, start, end -> blocks += BlockImageRange(url, start, end) },
            imageSizeChanged = { link, size -> imageSizeByLink += (link to size) },
        )
        map to blocks.sortedBy { it.start }
    }

    val containerModifier: @Composable (Modifier) -> Modifier = { base ->
        base.onPlaced {
            it.parentLayoutCoordinates?.also { coordinates ->
                containerSize.value = coordinates.size.toSize()
            }
        }
    }

    val textSegment: @Composable (AnnotatedString, Modifier) -> Unit = { segment, segmentModifier ->
        MarkdownBasicText(
            text = segment,
            modifier = segmentModifier.let { animations.animateTextSize(it) },
            style = style,
            inlineContent = resolvedInlineContent,
            onTextLayout = {
                layoutResult.value = it
                onTextLayout?.invoke(it, baseColor)
            }
        )
    }

    if (blockImageRanges.isEmpty()) {
        textSegment(content, containerModifier(modifier))
    } else {
        val components = LocalMarkdownComponents.current
        val typography = LocalMarkdownTypography.current
        val imageNodesByUrl = remember(node, sourceContent) {
            if (sourceContent == null) emptyMap() else collectImageNodesByUrl(node, sourceContent)
        }
        Column(modifier = containerModifier(modifier)) {
            var cursor = 0
            blockImageRanges.forEach { range ->
                if (range.start > cursor) {
                    textSegment(content.subSequence(cursor, range.start), Modifier)
                }
                val imageNode = imageNodesByUrl[range.url]
                if (sourceContent != null && imageNode != null) {
                    components.image(MarkdownComponentModel(sourceContent, imageNode, typography))
                } else {
                    BlockFallbackImage(range.url)
                }
                cursor = range.end
            }
            if (cursor < content.length) {
                textSegment(content.subSequence(cursor, content.length), Modifier)
            }
        }
    }
}

private fun collectImageNodesByUrl(root: ASTNode, content: String): Map<String, ASTNode> {
    val map = LinkedHashMap<String, ASTNode>()
    fun visit(n: ASTNode) {
        if (n.type == MarkdownElementTypes.IMAGE) {
            val link = n.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getUnescapedTextInNode(content)
            if (link != null && link !in map) map[link] = n
        }
        n.children.forEach { visit(it) }
    }
    visit(root)
    return map
}

private data class BlockImageRange(val url: String, val start: Int, val end: Int)

@Composable
private fun BlockFallbackImage(url: String) {
    LocalImageTransformer.current.transform(url)?.let { imageData ->
        Image(
            painter = imageData.painter,
            contentDescription = imageData.contentDescription,
            modifier = imageData.modifier,
            alignment = imageData.alignment,
            contentScale = imageData.contentScale,
            alpha = imageData.alpha,
            colorFilter = imageData.colorFilter,
        )
    }
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
    lineHeightPx: Float = 0f,
    inlineImageAsBlock: Boolean = true,
    onBlockImage: ((url: String, start: Int, end: Int) -> Unit)? = null,
    imageSizeChanged: ((link: String, Size) -> Unit)? = null,
): Map<String, InlineTextContent> {
    val annotations = content.getStringAnnotations(0, content.length)
        .filter { it.item.startsWith("${MARKDOWN_TAG_IMAGE_URL}_") }

    val byTag = annotations.groupBy { it.item }
    return byTag.mapNotNull { (tag, occurrences) ->
        val url = tag.removePrefix("${MARKDOWN_TAG_IMAGE_URL}_")
        val imageSize = imageSizeByLink[url] ?: defaultImageSize

        // Promote tall images to block rendering: Compose's text engine does
        // not grow line metrics to fit placeholders taller than the line
        // height, which causes overlap with preceding lines.
        val promoteToBlock = inlineImageAsBlock &&
                lineHeightPx > 0f &&
                !imageSize.isUnspecified &&
                imageSize.height > lineHeightPx * MarkdownAnnotatorConfig.BLOCK_FALLBACK_LINE_MULTIPLIER

        if (promoteToBlock) {
            occurrences.forEach { onBlockImage?.invoke(url, it.start, it.end) }
            return@mapNotNull null
        }

        val config = transformer.placeholderConfig(
            url,
            density,
            containerSize,
            inlineImageWidth,
            imageSize,
            imageSizeChanged,
        )

        // Config size is in DP, convert to SP for Placeholder TextUnit
        tag to InlineTextContent(
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
    }.toMap()
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
