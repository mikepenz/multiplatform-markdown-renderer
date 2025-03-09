package com.mikepenz.markdown.ui.annotation

import android.content.res.Configuration
import android.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "light", showBackground = true, backgroundColor = Color.WHITE.toLong(), heightDp = 300)
@Preview(name = "dark", showBackground = true, backgroundColor = Color.BLACK.toLong(), heightDp = 300, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkLightPreview