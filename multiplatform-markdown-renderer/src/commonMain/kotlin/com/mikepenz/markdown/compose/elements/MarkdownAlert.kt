package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import com.mikepenz.markdown.compose.LocalMarkdownA11yLabels
import com.mikepenz.markdown.compose.LocalMarkdownAlertIcons
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.MarkdownElement
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.model.MarkdownAlertType
import org.intellij.markdown.MarkdownTokenTypes.Companion.BLOCK_QUOTE
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.WHITE_SPACE
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

/**
 * Renders a GitHub alert (`> [!NOTE]`, `> [!WARNING]`, …) as an accent bar, a title row carrying an
 * icon, and the alert's block content.
 *
 * The [node] is a `GFMElementTypes.ALERT`, whose children are the blockquote marker, the
 * `ALERT_TITLE` token, and the block content — the markers between blocks are skipped here.
 *
 * @param content The original markdown content string.
 * @param node The `ALERT` node to render.
 * @param type The resolved alert type, driving the default title, icon and accent color.
 * @param title The title rendered next to the icon. Override to localize.
 * @param icon The icon rendered before the title, or `null` to render the title alone.
 * @param accent The color of the bar, icon and title.
 * @param style The style of the title.
 */
@Composable
fun MarkdownAlert(
    content: String,
    node: ASTNode,
    type: MarkdownAlertType,
    title: String = type.title,
    icon: ImageVector? = LocalMarkdownAlertIcons.current.iconFor(type),
    accent: Color = LocalMarkdownColors.current.alert.colorFor(type),
    style: TextStyle = LocalMarkdownTypography.current.alertTitle,
) {
    val dimens = LocalMarkdownDimens.current.alert
    val padding = LocalMarkdownPadding.current.alert
    val markdownComponents = LocalMarkdownComponents.current
    val a11yLabels = LocalMarkdownA11yLabels.current
    val bar = padding.bar
    val barThickness = dimens.barThickness

    Column(
        modifier = Modifier
            .semantics { contentDescription = a11yLabels.alert(title) }
            .drawBehind {
                val x = bar.calculateStartPadding(LayoutDirection.Ltr).toPx()
                drawLine(
                    color = accent,
                    strokeWidth = barThickness.toPx(),
                    start = Offset(x, bar.calculateTopPadding().toPx()),
                    end = Offset(x, size.height - bar.calculateBottomPadding().toPx()),
                )
            }
            .padding(padding.container)
    ) {
        Column(modifier = Modifier.padding(padding.content)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Image(
                        painter = rememberVectorPainter(icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(accent),
                        modifier = Modifier.size(dimens.iconSize),
                    )
                    Spacer(Modifier.width(padding.iconSpacing))
                }
                MarkdownBasicText(
                    text = title,
                    style = style,
                    color = if (style.color.isSpecified) style.color else accent,
                )
            }

            Spacer(Modifier.height(padding.titleSpacing))

            // The parser leaves the `> ` markers in the tree: a leading BLOCK_QUOTE *token*, and an
            // `EOL WHITE_SPACE` pair before every continuation line. Dropping those, plus everything up
            // to and including ALERT_TITLE, leaves exactly the alert's block content. Blocks are always
            // separated by a blank line, so a uniform gap between them is enough. Note the marker token
            // shares its name with `MarkdownElementTypes.BLOCK_QUOTE` — a nested quote — which renders.
            var pastTitle = false
            var seenContent = false
            node.children.forEach { child ->
                when {
                    !pastTitle -> if (child.type == GFMTokenTypes.ALERT_TITLE) pastTitle = true
                    child.type == EOL || child.type == WHITE_SPACE || child.type == BLOCK_QUOTE -> Unit
                    else -> {
                        if (seenContent) Spacer(Modifier.height(padding.titleSpacing))
                        seenContent = true
                        // Stable key by source offset gives each child its own slot and
                        // keeps recompositions isolated when a sibling changes.
                        key(child.startOffset) {
                            MarkdownElement(
                                node = child,
                                components = markdownComponents,
                                content = content,
                                includeSpacer = false,
                            )
                        }
                    }
                }
            }
        }
    }
}
