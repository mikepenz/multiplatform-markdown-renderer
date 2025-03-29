package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import com.mikepenz.markdown.sample.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun LicensesPage(
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var libs by remember { mutableStateOf<Libs?>(null) }
    LaunchedEffect("aboutlibraries.json") {
        libs = Libs.Builder()
            .withJson(Res.readBytes("files/aboutlibraries.json").decodeToString())
            .build()
    }
    LibrariesContainer(
        libraries = libs,
        modifier = modifier.fillMaxSize(),
        // sample uses material 2 - proxy theme
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = contentColorFor(MaterialTheme.colors.background),
            badgeBackgroundColor = MaterialTheme.colors.primary,
            badgeContentColor = contentColorFor(MaterialTheme.colors.background),
            dialogConfirmButtonColor = MaterialTheme.colors.primary,
        ),
        contentPadding = padding
    )
}
