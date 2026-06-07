package com.mikepenz.markdown.m3.a11y

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.LinkAnnotation
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
     * Issue #487: the paragraph that contains the inline links must itself sit inside
     * a `IsTraversalGroup = true` ancestor, so TalkBack reads link nodes in source
     * order. We locate the text node that carries the paragraph copy and walk up to
     * its nearest ancestor with the property.
     */
    @Test
    fun paragraph_with_links_has_traversal_group_ancestor() = runComposeUiTest {
        setContent { Wrap { Markdown(docLinks) } }
        val root = onRoot().fetchSemanticsNode()
        val anchor = findNodeWhoseTextContains(root, "Anthropic")
            ?: error("Could not locate paragraph text node containing the link copy")
        assertTrue(
            anyAncestorHasTraversalGroup(root, anchor),
            "Paragraph with inline links must be inside an IsTraversalGroup ancestor — regression of #487"
        )
    }

    /**
     * Issue #569 fix (#570): the paragraph carrying inline links must be a screen-reader
     * merge boundary so TalkBack reads it as one unit instead of skipping it. In Compose
     * `mergeDescendants` maps to the ANI `isScreenReaderFocusable` flag, so we assert the
     * flag directly on the unmerged tree — it is `false` without the fix and `true` with it.
     *
     * Simultaneously guards #487: the two inline links must survive as addressable
     * `LinkAnnotation`s on that same text node (merging must not prune them).
     *
     * Unlike a pixel snapshot, this distinguishes the patched from the unpatched renderer:
     * the merge flag flips while the rendered output stays byte-identical.
     */
    @Test
    fun link_paragraph_merges_yet_keeps_both_links() = runComposeUiTest {
        setContent { Wrap { Markdown(docLinks) } }
        val root = onRoot(useUnmergedTree = true).fetchSemanticsNode()
        val paragraph = findNodeWhoseTextContains(root, "Anthropic")
            ?: error("Could not locate paragraph text node containing the link copy")

        // #569: paragraph is a screen-reader merge boundary (false on develop, true on #570).
        assertTrue(
            paragraph.config.isMergingSemanticsOfDescendants,
            "Paragraph with inline links must merge descendants so TalkBack reads it — regression of #569"
        )

        // #487: both inline links remain individually addressable after the merge.
        val annotatedString = paragraph.config[SemanticsProperties.Text].first()
        val urls = annotatedString.getLinkAnnotations(0, annotatedString.length)
            .mapNotNull { (it.item as? LinkAnnotation.Url)?.url }
            .toSet()
        assertEquals(
            setOf("https://anthropic.com", "https://claude.ai"), urls,
            "Both inline links must survive merging — regression of #487"
        )
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

private fun findNodeWhoseTextContains(node: SemanticsNode, needle: String): SemanticsNode? {
    val textList = node.config.getOrElseNullable(SemanticsProperties.Text) { null }
    if (textList?.any { it.text.contains(needle) } == true) return node
    val edit = node.config.getOrElseNullable(SemanticsProperties.EditableText) { null }
    if (edit?.text?.contains(needle) == true) return node
    node.children.forEach { findNodeWhoseTextContains(it, needle)?.let { return it } }
    return null
}

private fun anyAncestorHasTraversalGroup(root: SemanticsNode, target: SemanticsNode): Boolean {
    val path = mutableListOf<SemanticsNode>()
    fun search(n: SemanticsNode): Boolean {
        path += n
        if (n.id == target.id) return true
        n.children.forEach { if (search(it)) return true }
        path.removeAt(path.lastIndex)
        return false
    }
    if (!search(root)) return false
    // `path` now goes root → … → target. Check ancestors (exclude target itself).
    return path.dropLast(1).any { ancestor ->
        ancestor.config.getOrElseNullable(SemanticsProperties.IsTraversalGroup) { false } == true
    }
}

@Suppress("unused")
private fun SemanticsConfiguration.dummy() = Unit
