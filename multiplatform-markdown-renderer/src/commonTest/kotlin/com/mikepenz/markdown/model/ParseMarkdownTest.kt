package com.mikepenz.markdown.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests for the synchronous top-level [parseMarkdown] function.
 */
class ParseMarkdownTest {

    @Test
    fun parseMarkdown_happyPath_returnsSuccessSynchronously() {
        // When parsing a simple heading synchronously (no coroutine/Flow collection)
        val state = parseMarkdown("# Title")

        // Then the result is a Success carrying the original content and a parsed node
        val success = assertIs<State.Success>(state)
        assertEquals("# Title", success.content)
        assertTrue(success.node.children.isNotEmpty())
    }

    @Test
    fun parseMarkdown_emptyContent_returnsSuccess() {
        // When parsing empty content
        val state = parseMarkdown("")

        // Then it still resolves to Success (an empty tree), never Loading
        val success = assertIs<State.Success>(state)
        assertEquals("", success.content)
    }

    @Test
    fun parseMarkdown_withLookupLinks_resolvesReferenceLink() {
        // Given a document containing a reference link definition
        val referenceLinkHandler = ReferenceLinkHandlerImpl()
        val content = """
            [example]

            [example]: https://example.com
        """.trimIndent()

        // When parsing with link lookup enabled
        val state = parseMarkdown(
            content = content,
            lookupLinks = true,
            referenceLinkHandler = referenceLinkHandler,
        )

        // Then the link is resolved and stored in the provided handler.
        // The parser keeps the reference label including its surrounding brackets.
        val success = assertIs<State.Success>(state)
        assertTrue(success.linksLookedUp)
        assertEquals("https://example.com", referenceLinkHandler.find("[example]"))
    }

    @Test
    fun parseMarkdown_onParseFailure_returnsErrorState() {
        // Given a reference link handler that throws while storing a resolved link
        val failure = IllegalStateException("boom")
        val throwingHandler = object : ReferenceLinkHandler {
            override fun store(label: String, destination: String?) {
                throw failure
            }

            override fun find(label: String): String = ""
        }
        val content = """
            [example]

            [example]: https://example.com
        """.trimIndent()

        // When parsing with link lookup enabled so the handler is exercised
        val state = parseMarkdown(
            content = content,
            lookupLinks = true,
            referenceLinkHandler = throwingHandler,
        )

        // Then the function returns an Error state carrying the throwable, rather than throwing
        val error = assertIs<State.Error>(state)
        assertEquals(failure, error.result)
    }
}
