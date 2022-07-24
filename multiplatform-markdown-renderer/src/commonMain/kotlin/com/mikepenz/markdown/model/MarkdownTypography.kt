package com.mikepenz.markdown.model

import androidx.compose.ui.text.TextStyle

interface MarkdownTypography {
    val text: TextStyle
    val code: TextStyle
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val h5: TextStyle
    val h6: TextStyle
    val quote: TextStyle
    val paragraph: TextStyle
    val ordered: TextStyle
    val bullet: TextStyle
    val list: TextStyle
}
