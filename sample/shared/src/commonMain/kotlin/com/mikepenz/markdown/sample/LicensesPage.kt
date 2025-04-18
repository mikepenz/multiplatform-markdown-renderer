package com.mikepenz.markdown.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.markdown.m3.Markdown

@Composable
internal fun LicensesPage(
    libraries: Libs?,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        licenseDialogBody = { library ->
            val licenseContent = library.licenses.joinToString(separator = "\n\n\n\n") {
                it.licenseContent ?: ""
            }
            Markdown(content = licenseContent)
        }
    )
}
