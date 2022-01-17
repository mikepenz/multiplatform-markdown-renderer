package com.mikepenz.markdown

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * A composable that receives some source code as [String] and renders it in the UI.
 *
 * @param code The source code content to render in the UI.
 * @param modifier The modifier to be applied to the Markdown.
 */
@Composable
fun Code(
    code: String,
    modifier: Modifier = Modifier,
    colors: MarkdownColors,
) {
    val scroll = rememberScrollState(0)

    Surface(
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            code,
            modifier = Modifier
                .horizontalScroll(scroll)
                .padding(8.dp),
            style = MaterialTheme.typography.body2.copy(fontFamily = FontFamily.Monospace, color = colors.textColor)
        )
    }
}
