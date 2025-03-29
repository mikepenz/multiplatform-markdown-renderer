package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun LicensesPage(
    libraries: Libs?,
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LibrariesContainer(
        libraries = libraries,
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
