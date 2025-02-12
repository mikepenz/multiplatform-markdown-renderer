package com.mikepenz.markdown.m3.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText

@Composable
fun MarkdownCheckBox(checked: Boolean) {
    Row {
        Checkbox(checked = checked, onCheckedChange = null)
        // Add a space at the end to separate it from subsequent text.
        MarkdownBasicText(
            text = AnnotatedString(" "),
            style = LocalMarkdownTypography.current.bullet,
            color = LocalMarkdownColors.current.text,
        )
    }
}
