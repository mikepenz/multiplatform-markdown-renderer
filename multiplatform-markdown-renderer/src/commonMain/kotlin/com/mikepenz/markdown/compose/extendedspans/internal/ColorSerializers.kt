// Copyright 2023, Saket Narayan
// SPDX-License-Identifier: Apache-2.0
// https://github.com/saket/extended-spans
package com.mikepenz.markdown.compose.extendedspans.internal

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.toArgb

fun Color?.serialize(): String {
    return if (this == null || isUnspecified) "null" else "${toArgb()}"
}

fun String.deserializeToColor(): Color? {
    return if (this == "null") null else Color(this.toInt())
}
