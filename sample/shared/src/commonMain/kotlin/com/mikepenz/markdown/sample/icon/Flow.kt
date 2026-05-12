package com.mikepenz.markdown.sample.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Flow: ImageVector
    get() {
        if (_Flow != null) return _Flow!!

        _Flow = ImageVector.Builder(
            name = "Flow",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Three wavy horizontal lines representing flow
            path(
                fill = SolidColor(Color.Black)
            ) {
                // Top wave
                moveTo(2f, 6f)
                curveTo(4f, 4f, 6f, 4f, 8f, 6f)
                curveTo(10f, 8f, 12f, 8f, 14f, 6f)
                curveTo(16f, 4f, 18f, 4f, 20f, 6f)
                lineTo(20f, 8f)
                curveTo(18f, 6f, 16f, 6f, 14f, 8f)
                curveTo(12f, 10f, 10f, 10f, 8f, 8f)
                curveTo(6f, 6f, 4f, 6f, 2f, 8f)
                close()
                
                // Middle wave
                moveTo(2f, 11f)
                curveTo(4f, 9f, 6f, 9f, 8f, 11f)
                curveTo(10f, 13f, 12f, 13f, 14f, 11f)
                curveTo(16f, 9f, 18f, 9f, 20f, 11f)
                lineTo(20f, 13f)
                curveTo(18f, 11f, 16f, 11f, 14f, 13f)
                curveTo(12f, 15f, 10f, 15f, 8f, 13f)
                curveTo(6f, 11f, 4f, 11f, 2f, 13f)
                close()
                
                // Bottom wave
                moveTo(2f, 16f)
                curveTo(4f, 14f, 6f, 14f, 8f, 16f)
                curveTo(10f, 18f, 12f, 18f, 14f, 16f)
                curveTo(16f, 14f, 18f, 14f, 20f, 16f)
                lineTo(20f, 18f)
                curveTo(18f, 16f, 16f, 16f, 14f, 18f)
                curveTo(12f, 20f, 10f, 20f, 8f, 18f)
                curveTo(6f, 16f, 4f, 16f, 2f, 18f)
                close()
            }
        }.build()

        return _Flow!!
    }

private var _Flow: ImageVector? = null
