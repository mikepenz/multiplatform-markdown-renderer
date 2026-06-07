package com.mikepenz.markdown.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for the Flow<String>.asMarkdownState() extension function.
 */
class AsMarkdownStateTest {

    @Test
    fun asMarkdownState_emitsLoadingThenSuccess() = runTest {
        // Given a simple markdown content flow
        val contentFlow = MutableStateFlow("# Hello World")

        // When we transform it to a markdown state flow
        val states = contentFlow
            .asMarkdownState(retainState = false)
            .take(2) // Take first 2 emissions: Loading and Success
            .toList()

        // Then we should get Loading followed by Success
        assertEquals(2, states.size)
        assertIs<State.Loading>(states[0])
        assertIs<State.Success>(states[1])
    }

    @Test
    fun asMarkdownState_parsesContent() = runTest {
        // Given markdown with a heading
        val contentFlow = MutableStateFlow("# Test Heading")

        // When we transform and collect the result
        val states = contentFlow
            .asMarkdownState(retainState = false)
            .take(2)
            .toList()

        // Then the success state should contain the parsed content
        val successState = states[1] as State.Success
        assertEquals("# Test Heading", successState.content)
        // The AST node should exist
        assertTrue(successState.node.children.isNotEmpty())
    }



    @Test
    fun asMarkdownState_emptyContent() = runTest {
        // Given an empty markdown string
        val contentFlow = MutableStateFlow("")

        // When we transform it
        val states = contentFlow
            .asMarkdownState(retainState = false)
            .take(2)
            .toList()

        // Then it should still emit Loading then Success
        assertEquals(2, states.size)
        assertIs<State.Loading>(states[0])
        assertIs<State.Success>(states[1])
        assertEquals("", (states[1] as State.Success).content)
    }

    @Test
    fun asMarkdownState_complexMarkdown() = runTest {
        // Given complex markdown with multiple elements
        val markdown = """
            # Heading 1

            ## Heading 2

            - List item 1
            - List item 2

            **Bold text** and *italic text*

            ```kotlin
            fun test() {}
            ```
        """.trimIndent()

        val contentFlow = MutableStateFlow(markdown)

        // When we transform it
        val states = contentFlow
            .asMarkdownState(retainState = false)
            .take(2)
            .toList()

        // Then it should parse successfully
        assertIs<State.Loading>(states[0])
        assertIs<State.Success>(states[1])
        val success = states[1] as State.Success
        assertEquals(markdown, success.content)
        assertTrue(success.node.children.isNotEmpty())
    }

    @Test
    fun asMarkdownState_incrementalContentBuildup() = runBlocking {
        // Given a flow that gradually builds up markdown content
        val contentFlow = MutableStateFlow("# Title")
        val states = mutableListOf<State>()

        // Create a SINGLE asMarkdownState flow and collect from it continuously
        val stateFlow = contentFlow.asMarkdownState(retainState = false)

        // Start collecting in background
        val job = launch {
            stateFlow.collect { state ->
                states.add(state)
            }
        }

        // Wait for initial emissions (Loading + Success) - use real delay since parse uses Dispatchers.Default
        delay(500)
        assertTrue(states.size >= 2, "Should have at least Loading + Success for first emission, got ${states.size}: $states")
        assertIs<State.Loading>(states[0], "First state should be Loading")
        assertIs<State.Success>(states[1], "Second state should be Success")
        assertEquals("# Title", (states[1] as State.Success).content)

        val stateCountAfterFirst = states.size

        // Update with more content - this tests that the flow continues to work
        contentFlow.value = "# Title\n\nSome paragraph text."
        delay(500)

        // Verify we got NEW states (Loading + Success for the second emission)
        assertTrue(states.size > stateCountAfterFirst, "Should have new states after second emission, got ${states.size} total states")
        val newStates = states.subList(stateCountAfterFirst, states.size)
        assertTrue(newStates.any { it is State.Loading }, "Should have Loading state for second update, got: $newStates")
        assertTrue(newStates.any { it is State.Success && it.content.contains("paragraph text") },
            "Should have Success state with new content, got: $newStates")

        val stateCountAfterSecond = states.size

        // Third update
        contentFlow.value = "# Title\n\nSome paragraph text.\n\n- Item 1\n- Item 2"
        delay(500)

        // Verify we got ANOTHER set of new states
        assertTrue(states.size > stateCountAfterSecond, "Should have new states after third emission, got ${states.size} total states")
        val thirdUpdateStates = states.subList(stateCountAfterSecond, states.size)
        assertTrue(thirdUpdateStates.any { it is State.Loading }, "Should have Loading state for third update, got: $thirdUpdateStates")
        val finalSuccess = thirdUpdateStates.filterIsInstance<State.Success>().lastOrNull()
        assertTrue(finalSuccess != null && finalSuccess.content.contains("- Item 2"),
            "Should have Success state with list items, got: $thirdUpdateStates")

        job.cancel()

        // Verify the pattern: we should have multiple Loading states (one per update with retainState=false)
        val loadingStates = states.filterIsInstance<State.Loading>()
        assertTrue(loadingStates.size >= 3, "Should have at least 3 Loading states (one per emission), got ${loadingStates.size}: $states")
    }
}
