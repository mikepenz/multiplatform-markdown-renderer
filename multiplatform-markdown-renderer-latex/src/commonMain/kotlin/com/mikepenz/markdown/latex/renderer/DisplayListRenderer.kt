package com.mikepenz.markdown.latex.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.latex.model.DisplayItem
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.latex.model.PathCommand
import com.mikepenz.markdown.latex.model.RaTeXColor

/**
 * Renders a [DisplayList] onto a Compose [DrawScope].
 * All em-unit coordinates are multiplied by [fontSizePx] to get pixel coordinates.
 */
internal class DisplayListRenderer(
    private val displayList: DisplayList,
    private val fontSizePx: Float,
    private val textMeasurer: TextMeasurer,
    private val fontMap: Map<String, FontFamily>,
    private val colorOverride: Color = Color.Unspecified,
) {
    private val glyphCache = HashMap<GlyphCacheKey, TextLayoutResult>()

    fun draw(drawScope: DrawScope) {
        for (item in displayList.items) {
            when (item) {
                is DisplayItem.GlyphPath -> drawScope.drawGlyph(item)
                is DisplayItem.Line -> drawScope.drawLine(item)
                is DisplayItem.Rect -> drawScope.drawRect(item)
                is DisplayItem.Path -> drawScope.drawPath(item)
            }
        }
    }

    private fun Double.em() = (this * fontSizePx).toFloat()

    private fun resolveColor(original: RaTeXColor): Color =
        colorOverride.takeOrElse { original.toComposeColor() }

    private fun DrawScope.drawGlyph(glyph: DisplayItem.GlyphPath) {
        val fontFamily = fontMap[glyph.font] ?: fontMap[glyph.font.removePrefix("KaTeX_")] ?: return
        val charCode = glyph.charCode
        if (charCode !in 0..0x10FFFF) return

        val str = charCode.toCodePointString()
        val color = colorOverride.takeOrElse { glyph.color.toComposeColor() }
        val targetPx = fontSizePx * glyph.scale
        val fontSizeSp = (targetPx / (density * fontScale)).toFloat()

        val key = GlyphCacheKey(fontFamily, charCode, fontSizeSp, color)
        val result = glyphCache.getOrPut(key) {
            val style = TextStyle(
                fontFamily = fontFamily,
                fontSize = fontSizeSp.sp,
                color = color,
            )
            textMeasurer.measure(str, style)
        }
        drawText(
            textLayoutResult = result,
            topLeft = Offset(
                (glyph.x * fontSizePx).toFloat(),
                (glyph.y * fontSizePx).toFloat() - result.firstBaseline,
            ),
        )
    }

    private fun DrawScope.drawLine(l: DisplayItem.Line) {
        val halfT = (l.thickness * fontSizePx / 2).toFloat()
        drawRect(
            color = resolveColor(l.color),
            topLeft = Offset(l.x.em(), l.y.em() - halfT),
            size = Size(l.width.em(), halfT * 2),
        )
    }

    private fun DrawScope.drawRect(r: DisplayItem.Rect) {
        drawRect(
            color = resolveColor(r.color),
            topLeft = Offset(r.x.em(), r.y.em()),
            size = Size(r.width.em(), r.height.em()),
        )
    }

    private fun DrawScope.drawPath(p: DisplayItem.Path) {
        val path = buildComposePath(p.commands, p.x.em(), p.y.em())
        drawPath(
            path = path,
            color = resolveColor(p.color),
            style = if (p.fill) Fill else Stroke(),
        )
    }

    private fun buildComposePath(commands: List<PathCommand>, dx: Float, dy: Float): Path {
        val path = Path()
        for (cmd in commands) {
            when (cmd) {
                is PathCommand.MoveTo -> path.moveTo(dx + cmd.x.em(), dy + cmd.y.em())
                is PathCommand.LineTo -> path.lineTo(dx + cmd.x.em(), dy + cmd.y.em())
                is PathCommand.CubicTo -> path.cubicTo(
                    dx + cmd.x1.em(), dy + cmd.y1.em(),
                    dx + cmd.x2.em(), dy + cmd.y2.em(),
                    dx + cmd.x.em(), dy + cmd.y.em(),
                )
                is PathCommand.QuadTo -> path.quadraticTo(
                    dx + cmd.x1.em(), dy + cmd.y1.em(),
                    dx + cmd.x.em(), dy + cmd.y.em(),
                )
                PathCommand.Close -> path.close()
            }
        }
        return path
    }
}

private data class GlyphCacheKey(
    val fontFamily: FontFamily,
    val charCode: Int,
    val fontSizeSp: Float,
    val color: Color,
)

/** Convert a Unicode code point to a String (supports supplementary planes). */
private fun Int.toCodePointString(): String {
    return if (this < 0x10000) {
        this.toChar().toString()
    } else {
        val high = ((this - 0x10000) shr 10) + 0xD800
        val low = ((this - 0x10000) and 0x3FF) + 0xDC00
        charArrayOf(high.toChar(), low.toChar()).concatToString()
    }
}
