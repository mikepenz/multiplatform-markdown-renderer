package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.TextUnit

// Q. does it makes sense(performance wise) to pack it into a single long?
@Immutable
internal class TextUnitSize(
    val width: TextUnit, val height: TextUnit
)