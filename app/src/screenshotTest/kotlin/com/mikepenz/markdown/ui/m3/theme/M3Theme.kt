package com.mikepenz.markdown.ui.m3.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mikepenz.markdown.ui.Red200
import com.mikepenz.markdown.ui.Red800
import com.mikepenz.markdown.ui.SampleGreen

private val DarkColorScheme = darkColorScheme(
    primary = SampleGreen,
    onPrimary = Color.White,
    secondary = SampleGreen,
    onSecondary = Color.White,
    error = Red200
)

private val LightColorScheme = lightColorScheme(
    primary = SampleGreen,
    onPrimary = Color.White,
    secondary = SampleGreen,
    onSecondary = Color.White,
    error = Red800
)

@Composable
fun SampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
