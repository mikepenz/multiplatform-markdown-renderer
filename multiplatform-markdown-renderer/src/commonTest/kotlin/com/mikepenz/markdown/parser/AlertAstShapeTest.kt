package com.mikepenz.markdown.parser

import com.mikepenz.markdown.model.MarkdownAlertType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Pins the AST shape `org.jetbrains:markdown` produces for GitHub alerts, which
 * `MarkdownAlert` relies on to separate the title from the alert's block content.
 */
class AlertAstShapeTest {

    private fun parse(input: String): ASTNode =
        MarkdownParser(GFMFlavourDescriptor()).buildMarkdownTreeFromString(input)

    private fun alerts(tree: ASTNode): List<ASTNode> = tree.children.filter { it.type == GFMElementTypes.ALERT }

    private fun titleOf(node: ASTNode, content: String): String? =
        node.findChildOfType(GFMTokenTypes.ALERT_TITLE)?.getTextInNode(content)?.toString()

    @Test
    fun everyAlertTypeIsRecognised() {
        MarkdownAlertType.entries.forEach { type ->
            val content = "> ${type.marker}\n> body"
            val alert = alerts(parse(content)).singleOrNull()
            assertNotNull(alert, "expected an ALERT node for ${type.marker}")
            assertEquals(type, MarkdownAlertType.from(titleOf(alert, content)!!))
        }
    }

    @Test
    fun markersAreMatchedCaseInsensitively() {
        val content = "> [!warning]\n> body"
        val alert = alerts(parse(content)).singleOrNull()
        assertNotNull(alert)
        assertEquals(MarkdownAlertType.WARNING, MarkdownAlertType.from(titleOf(alert, content)!!))
    }

    @Test
    fun unknownMarkerStaysABlockQuote() {
        val content = "> [!NONSENSE]\n> body"
        val tree = parse(content)
        assertTrue(alerts(tree).isEmpty())
        assertNotNull(tree.findChildOfType(MarkdownElementTypes.BLOCK_QUOTE))
        assertNull(MarkdownAlertType.from("[!NONSENSE]"))
    }

    @Test
    fun plainBlockQuoteIsNotAnAlert() {
        assertTrue(alerts(parse("> just a quote")).isEmpty())
    }

    /**
     * The title line is `BLOCK_QUOTE ALERT_TITLE EOL WHITE_SPACE`, and every following block is
     * preceded by more `EOL`/`WHITE_SPACE` marker tokens. `MarkdownAlert` skips exactly those.
     */
    @Test
    fun alertChildrenAreMarkerTokensThenBlockContent() {
        val content = """
            > [!IMPORTANT]
            > first para
            >
            > second para
        """.trimIndent()
        val alert = alerts(parse(content)).single()
        val children = alert.children

        assertEquals(MarkdownTokenTypes.BLOCK_QUOTE, children[0].type)
        assertEquals(GFMTokenTypes.ALERT_TITLE, children[1].type)
        assertEquals(MarkdownTokenTypes.EOL, children[2].type)

        val blocks = children.filterNot { it.type in MARKER_TYPES }
        assertEquals(listOf(MarkdownElementTypes.PARAGRAPH, MarkdownElementTypes.PARAGRAPH), blocks.map { it.type })
    }

    /**
     * The `> ` marker is a `MarkdownTokenTypes.BLOCK_QUOTE`, while a quote nested inside the alert is
     * a `MarkdownElementTypes.BLOCK_QUOTE`. They share a name but not a type — `MarkdownAlert` skips
     * only the former, so the nested quote still renders.
     */
    @Test
    fun nestedBlockQuoteIsAnElementNotAMarkerToken() {
        val content = """
            > [!NOTE]
            > outer
            >
            > > nested
        """.trimIndent()
        val alert = alerts(parse(content)).single()
        assertTrue(MarkdownTokenTypes.BLOCK_QUOTE !== MarkdownElementTypes.BLOCK_QUOTE)

        val blocks = alert.children.filterNot { it.type in MARKER_TYPES }
        assertEquals(listOf(MarkdownElementTypes.PARAGRAPH, MarkdownElementTypes.BLOCK_QUOTE), blocks.map { it.type })
    }

    private companion object {
        /** The marker tokens `MarkdownAlert` drops before rendering the alert's block content. */
        val MARKER_TYPES = setOf(
            MarkdownTokenTypes.BLOCK_QUOTE,
            GFMTokenTypes.ALERT_TITLE,
            MarkdownTokenTypes.EOL,
            MarkdownTokenTypes.WHITE_SPACE,
        )
    }

    @Test
    fun alertsCarryArbitraryBlockContent() {
        val content = """
            > [!TIP]
            > - one
            > - two
            >
            > ```kotlin
            > val x = 1
            > ```
        """.trimIndent()
        val alert = alerts(parse(content)).single()
        val types = alert.children.map { it.type }
        assertTrue(MarkdownElementTypes.UNORDERED_LIST in types)
        assertTrue(MarkdownElementTypes.CODE_FENCE in types)
    }
}
