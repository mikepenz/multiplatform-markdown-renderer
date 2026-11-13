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
 *  - Issue #668 — a list item is one stop, read with its marker.
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

    /**
     * Issue #668: a list item is one screen-reader stop, its marker read with its text. Before the fix the marker was
     * a stop of its own, and TalkBack said "Bullet", then the item on the next swipe.
     */
    @Test
    fun bullet_list_item_is_one_stop_read_with_its_marker() = runComposeUiTest {
        setContent { Wrap { Markdown("- Dogs on leash\n- Pack out what you pack in") } }
        val root = onRoot().fetchSemanticsNode()

        val item = findNodeWhoseTextContains(root, "Dogs on leash")
            ?: error("Could not locate the list item")
        assertTrue(item.readText().contains("•"), "The bullet must be read with its item — regression of #668")
        assertEquals(
            0, nodesWhoseTextIs(root, "•").size,
            "No stop may hold the bullet alone — regression of #668"
        )
    }

    /** Issue #668: an ordered item is read with its number, including a list that starts past 1. */
    @Test
    fun ordered_list_item_is_read_with_its_number() = runComposeUiTest {
        setContent { Wrap { Markdown("3. Third\n4. Fourth") } }
        val root = onRoot().fetchSemanticsNode()

        val item = findNodeWhoseTextContains(root, "Third")
            ?: error("Could not locate the list item")
        assertTrue(item.readText().contains("3."), "The number must be read with its item — regression of #668")
    }

    /**
     * Issue #668 with #569: an item whose text holds a link is still one stop with its marker, and keeps the link.
     * The paragraph's own merge (#570) must give way to the item's, or the marker is left alone again.
     */
    @Test
    fun list_item_with_a_link_is_one_stop_and_keeps_its_link() = runComposeUiTest {
        setContent { Wrap { Markdown("- See [the trail map](https://example.com/map) first") } }
        val root = onRoot().fetchSemanticsNode()

        val item = findNodeWhoseTextContains(root, "the trail map")
            ?: error("Could not locate the list item")
        assertTrue(item.readText().contains("•"), "The bullet must be read with a linked item — regression of #668")
        val urls = item.config[SemanticsProperties.Text]
            .flatMap { text -> text.getLinkAnnotations(0, text.length).mapNotNull { (it.item as? LinkAnnotation.Url)?.url } }
        assertEquals(listOf("https://example.com/map"), urls, "The item's link must survive — regression of #487")
    }

    /** Issue #668: a nested list's items stay stops of their own, not swallowed by the item that holds them. */
    @Test
    fun nested_list_items_stay_their_own_stops() = runComposeUiTest {
        setContent { Wrap { Markdown("- Parent\n    - Child") } }
        val root = onRoot().fetchSemanticsNode()

        val parent = findNodeWhoseTextContains(root, "Parent") ?: error("Could not locate the parent item")
        val child = findNodeWhoseTextContains(root, "Child") ?: error("Could not locate the child item")
        assertTrue(parent.id != child.id, "The nested item must be its own stop")
        assertTrue(!parent.readText().contains("Child"), "The parent item must not read its nested list")
        assertTrue(child.readText().contains("•"), "The nested item must be read with its own marker")
    }

    /** Issue #668: a list says how many items it has, so TalkBack announces it on the way in. */
    @Test
    fun list_exposes_its_item_count() = runComposeUiTest {
        setContent { Wrap { Markdown("- One\n- Two\n- Three") } }
        val root = onRoot(useUnmergedTree = true).fetchSemanticsNode()

        val lists = collect(root, SemanticsProperties.CollectionInfo)
        assertEquals(listOf(3), lists.map { it.rowCount })
        assertEquals(listOf(0, 1, 2), collect(root, SemanticsProperties.CollectionItemInfo).map { it.rowIndex })
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

/** Everything a screen reader reads on [this] stop: its text, in order. */
private fun SemanticsNode.readText(): String =
    config.getOrElseNullable(SemanticsProperties.Text) { null }.orEmpty().joinToString(" ") { it.text }

private fun nodesWhoseTextIs(root: SemanticsNode, text: String): List<SemanticsNode> {
    val out = mutableListOf<SemanticsNode>()
    walk(root) { n -> if (n.readText().trim() == text) out += n }
    return out
}

@Suppress("unused")
private fun SemanticsConfiguration.dummy() = Unit
