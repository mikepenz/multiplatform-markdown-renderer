package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.model.State

/**
 * Renders the parsed markdown content in a [LazyColumn].
 *
 * @param state The success markdown state.
 * @param components The MarkdownComponents instance containing the components to use.
 * @param modifier The modifier to be applied to the container.
 * @param contentPadding The padding to be applied to the scrolling container.
 */
@Composable
fun LazyMarkdownSuccess(
    state: State.Success,
    components: MarkdownComponents,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(state.node.children) { node ->
            MarkdownElement(node, components, state.content, skipLinkDefinition = state.linksLookedUp)
        }
    }
}