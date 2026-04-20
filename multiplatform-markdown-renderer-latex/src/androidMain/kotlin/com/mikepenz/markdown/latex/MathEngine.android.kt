package com.mikepenz.markdown.latex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ratex.RaTeXEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mikepenz.markdown.latex.model.DisplayItem as CommonDisplayItem
import com.mikepenz.markdown.latex.model.DisplayList as CommonDisplayList
import com.mikepenz.markdown.latex.model.PathCommand as CommonPathCommand
import com.mikepenz.markdown.latex.model.RaTeXColor as CommonRaTeXColor
import io.ratex.DisplayItem as NativeDisplayItem
import io.ratex.DisplayList as NativeDisplayList
import io.ratex.PathCommand as NativePathCommand
import io.ratex.RaTeXColor as NativeRaTeXColor

internal actual val isAsyncFontLoading: Boolean = false

private class AndroidMathEngine : MathEngine {
    override suspend fun parse(latex: String): CommonDisplayList {
        val native = withContext(Dispatchers.Default) {
            RaTeXEngine.parse(latex)
        }
        return native.toCommon()
    }
}

@Composable
actual fun rememberMathEngine(): MathEngine {
    return remember { AndroidMathEngine() }
}

private fun NativeDisplayList.toCommon() = CommonDisplayList(
    width = width,
    height = height,
    depth = depth,
    items = items.map(NativeDisplayItem::toCommon),
)

private fun NativeDisplayItem.toCommon(): CommonDisplayItem = when (this) {
    is NativeDisplayItem.GlyphPath -> CommonDisplayItem.GlyphPath(
        x = x, y = y, scale = scale, font = font, charCode = charCode,
        commands = commands.map(NativePathCommand::toCommon), color = color.toCommon(),
    )
    is NativeDisplayItem.Line -> CommonDisplayItem.Line(
        x = x, y = y, width = width, thickness = thickness, color = color.toCommon(),
    )
    is NativeDisplayItem.Rect -> CommonDisplayItem.Rect(
        x = x, y = y, width = width, height = height, color = color.toCommon(),
    )
    is NativeDisplayItem.Path -> CommonDisplayItem.Path(
        x = x, y = y, commands = commands.map(NativePathCommand::toCommon),
        fill = fill, color = color.toCommon(),
    )
}

private fun NativePathCommand.toCommon(): CommonPathCommand = when (this) {
    is NativePathCommand.MoveTo -> CommonPathCommand.MoveTo(x, y)
    is NativePathCommand.LineTo -> CommonPathCommand.LineTo(x, y)
    is NativePathCommand.CubicTo -> CommonPathCommand.CubicTo(x1, y1, x2, y2, x, y)
    is NativePathCommand.QuadTo -> CommonPathCommand.QuadTo(x1, y1, x, y)
    NativePathCommand.Close -> CommonPathCommand.Close
}

private fun NativeRaTeXColor.toCommon() = CommonRaTeXColor(r, g, b, a)
