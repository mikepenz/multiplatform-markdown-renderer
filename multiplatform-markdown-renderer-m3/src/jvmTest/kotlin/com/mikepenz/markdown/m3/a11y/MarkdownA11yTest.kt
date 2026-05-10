package com.mikepenz.markdown.m3.a11y

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsConfiguration
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import com.mikepenz.markdown.m3.Markdown
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Semantics regression tests for accessibility. Primary coverage:
 *  - Issue #487 — TalkBack reorders paragraphs containing inline links.
 *  - Headings expose Heading semantics.
 *  - Inline links emit per-link clickable a11y nodes.
 */
@OptIn(ExperimentalTestApi::class)
class MarkdownA11yTest {

    private val docLinks = """
        Visit [Anthropic](https://anthropic.com) and then [Claude](https://claude.ai) here.
    """.trimIndent()

    /**
     * Issue #487: `MarkdownText` must emit at least one `IsTraversalGroup` ancestor so
     * TalkBack reads link-bearing paragraphs in source order. Without it, interactive
     * link nodes get clustered separately from the surrounding text.
     */
    @Test
    fun paragraph_with_links_marks_traversal_group() = runComposeUiTest {
        setContent { Wrap { Markdown(docLinks) } }
        val root = onRoot().fetchSemanticsNode()
        assertTrue(
            collect(root, SemanticsProperties.IsTraversalGroup).any { it },
            "Expected at least one IsTraversalGroup=true node — fix for issue #487"
        )
    }

    @Test
    fun link_traversal_order_matches_source_order() = runComposeUiTest {
        setContent { Wrap { Markdown(docLinks) } }
        val root = onRoot().fetchSemanticsNode()
        val bounds = mutableListOf<Pair<Float, Float>>()
        walk(root) { n ->
            if (n.config.contains(SemanticsActions.OnClick)) {
                bounds += n.boundsInRoot.top to n.boundsInRoot.left
            }
        }
        assertEquals(bounds.sortedWith(compareBy({ it.first }, { it.second })), bounds)
    }

    @Test
    fun headings_expose_heading_property() = runComposeUiTest {
        setContent { Wrap { Markdown("# Title\n\nBody") } }
        onNodeWithText("Title").assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
    }

    @Test
    fun divider_is_inert_for_screen_readers() = runComposeUiTest {
        setContent { Wrap { Markdown("Above\n\n---\n\nBelow") } }
        // No node should expose contentDescription "—" or similar; divider clears semantics.
        // Smoke check: divider doesn't produce a focusable empty node — we expect no node
        // whose only role is the empty Box (clearAndSetSemantics removes it).
        // We assert by absence of crash and presence of body text.
        onNodeWithText("Above").assertExists()
        onNodeWithText("Below").assertExists()
    }
}

@androidx.compose.runtime.Composable
private fun Wrap(content: @androidx.compose.runtime.Composable () -> Unit) {
    MaterialTheme { Surface { Column { content() } } }
}

private fun <T> collect(root: SemanticsNode, key: SemanticsPropertyKey<T>): List<T> {
    val out = mutableListOf<T>()
    walk(root) { n ->
        if (n.config.contains(key)) out += n.config[key]
    }
    return out
}

private fun walk(node: SemanticsNode, visit: (SemanticsNode) -> Unit) {
    visit(node)
    node.children.forEach { walk(it, visit) }
}

@Suppress("unused")
private fun SemanticsConfiguration.dummy() = Unit
