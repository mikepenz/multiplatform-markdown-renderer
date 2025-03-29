package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun LicensesPage(
    libraries: Libs?,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        contentPadding = padding
    )
}
