package com.mikepenz.markdown.latex

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.latex.model.toMathSize
import com.mikepenz.markdown.latex.renderer.DisplayListRenderer
import com.mikepenz.markdown.latex.renderer.rememberKaTeXFontMap

/**
 * Renders a [DisplayList] onto a Compose [Canvas] using the common [DisplayListRenderer].
 */
@Composable
fun MathCanvas(
    displayList: DisplayList,
    fontSize: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val fontMap = rememberKaTeXFontMap()
    val localDensity = LocalDensity.current
    val fontSizePx = with(localDensity) { fontSize.sp.toPx() }

    val mathSize = displayList.toMathSize(fontSize)
    val widthDp = with(localDensity) { mathSize.width.sp.toDp() }
    val heightDp = with(localDensity) { mathSize.totalHeight.sp.toDp() }

    val renderer = remember(displayList, fontSizePx, color, textMeasurer, fontMap) {
        DisplayListRenderer(displayList, fontSizePx, textMeasurer, fontMap, color)
    }
    Canvas(modifier = modifier.size(widthDp, heightDp)) {
        renderer.draw(this)
    }
}
