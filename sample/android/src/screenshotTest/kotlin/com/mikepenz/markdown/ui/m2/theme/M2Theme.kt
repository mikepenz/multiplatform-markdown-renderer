package com.mikepenz.markdown.ui.m2.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.sample.theme.Primary
import com.mikepenz.markdown.sample.theme.Red200
import com.mikepenz.markdown.sample.theme.Red800

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = Primary,
    onPrimary = Color.White,
    secondary = Primary,
    onSecondary = Color.White,
    error = Red200
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = Primary,
    onPrimary = Color.White,
    secondary = Primary,
    secondaryVariant = Primary,
    onSecondary = Color.White,
    error = Red800
)

@Composable
fun SampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
