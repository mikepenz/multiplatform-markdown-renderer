package com.mikepenz.markdown.model

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StreamingMarkdownStateTest {
    @Test
    fun initialStateExposesAllEmptyFieldsAndProvidedReferenceHandler() {
        val referenceLinkHandler = ReferenceLinkHandlerImpl()
        val state = streamingState(referenceLinkHandler = referenceLinkHandler)

        assertEquals("", state.content.toString())
        assertSame(referenceLinkHandler, state.referenceLinkHandler)
        assertTrue(state.snapshot.value.stableAst.isEmpty())
        assertTrue(state.snapshot.value.unstableAstTail.isEmpty())
        assertTrue(state.links.value.isEmpty())
    }

    @Test
    fun appendUpdatesContentSnapshotAndReturnedSnapshot() = runTest {
        val state = streamingState()

        val returned = state.append("draft")

        assertEquals("draft", state.content.toString())
        assertSame(returned, state.snapshot.value)
        assertTrue(returned.stableAst.isEmpty())
        assertEquals(listOf(MarkdownElementTypes.PARAGRAPH), returned.unstableAstTail.types())
    }

    @Test
    fun appendKeepsContentAndStableNodeReferencesAcrossUnstableTailUpdates() = runTest {
        val state = streamingState()

        val completed = state.append("first paragraph\n\n")
        val stableParagraph = completed.stableAst.single { it.type == MarkdownElementTypes.PARAGRAPH }
        val contentReference = state.content

        val withTail = state.append("second paragraph")

        assertEquals("first paragraph\n\nsecond paragraph", state.content.toString())
        assertSame(contentReference, state.content)
        assertSame(stableParagraph, withTail.stableAst.single { it.type == MarkdownElementTypes.PARAGRAPH })
        assertEquals(listOf(MarkdownElementTypes.PARAGRAPH), withTail.unstableAstTail.types())
    }

    @Test
    fun emptyAppendKeepsFieldsConsistent() = runTest {
        val state = streamingState()
        state.append("body")
        val contentReference = state.content

        val returned = state.append("")

        assertEquals("body", state.content.toString())
        assertSame(contentReference, state.content)
        assertSame(returned, state.snapshot.value)
    }

    @Test
    fun snapshotFlowEmitsInitialAndChangedSnapshotsForCollectors() = runTest {
        val state = streamingState()
        val snapshots = mutableListOf<StreamingMarkdownState.Snapshot>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            state.snapshot.collect { snapshots += it }
        }

        val stableSnapshot = state.append("stable paragraph\n\n")
        val tailSnapshot = state.append("tail paragraph")
        advanceUntilIdle()

        assertEquals(3, snapshots.size)
        assertTrue(snapshots[0].stableAst.isEmpty())
        assertTrue(snapshots[0].unstableAstTail.isEmpty())
        assertSame(stableSnapshot, snapshots[1])
        assertSame(tailSnapshot, snapshots[2])
        assertSame(tailSnapshot, state.snapshot.value)
        assertTrue(snapshots[1].stableAst.any { it.type == MarkdownElementTypes.PARAGRAPH })
        assertTrue(snapshots[1].unstableAstTail.isEmpty())
        assertTrue(snapshots[2].stableAst.any { it.type == MarkdownElementTypes.PARAGRAPH })
        assertEquals(listOf(MarkdownElementTypes.PARAGRAPH), snapshots[2].unstableAstTail.types())
    }

    @Test
    fun snapshotFlowDoesNotEmitAgainWhenAppendProducesEqualSnapshot() = runTest {
        val state = streamingState()
        val snapshots = mutableListOf<StreamingMarkdownState.Snapshot>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            state.snapshot.collect { snapshots += it }
        }

        state.append("body")
        val countAfterContent = snapshots.size
        val returned = state.append("")
        advanceUntilIdle()

        assertEquals(2, countAfterContent)
        assertEquals(countAfterContent, snapshots.size)
        assertSame(returned, state.snapshot.value)
    }

    @Test
    fun lookupLinksStoresStableAndUnstableDefinitionsWhenEnabled() = runTest {
        val referenceLinkHandler = ReferenceLinkHandlerImpl()
        val state = streamingState(lookupLinks = true, referenceLinkHandler = referenceLinkHandler)

        state.append("[stable]: https://stable.example\n\n")
        state.append("[tail]: https://tail.example")

        assertEquals("https://stable.example", state.links.value["[stable]"])
        assertEquals("https://tail.example", state.links.value["[tail]"])
        assertEquals("https://stable.example", referenceLinkHandler.find("[stable]"))
        assertEquals("https://tail.example", referenceLinkHandler.find("[tail]"))

        state.append("\n\nbody")

        assertEquals("https://stable.example", state.links.value["[stable]"])
        assertEquals("https://tail.example", state.links.value["[tail]"])
    }

    @Test
    fun linksFlowEmitsInitialAndChangedLinksForCollectorsWhenLookupEnabled() = runTest {
        val state = streamingState(lookupLinks = true)
        val links = mutableListOf<Map<String, String?>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            state.links.collect { links += it }
        }

        state.append("[stable]: https://stable.example\n\n")
        state.append("[tail]: https://tail.example")
        state.append("\n\nbody")
        advanceUntilIdle()

        assertEquals(3, links.size)
        assertTrue(links[0].isEmpty())
        assertEquals(mapOf("[stable]" to "https://stable.example"), links[1])
        assertEquals(
            mapOf(
                "[stable]" to "https://stable.example",
                "[tail]" to "https://tail.example",
            ),
            links[2]
        )
        assertEquals(links.last(), state.links.value)
    }

    @Test
    fun lookupLinksLeavesLinkFieldsEmptyWhenDisabled() = runTest {
        val referenceLinkHandler = ReferenceLinkHandlerImpl()
        val state = streamingState(lookupLinks = false, referenceLinkHandler = referenceLinkHandler)

        state.append("[ignored]: https://ignored.example\n\n")

        assertTrue(state.links.value.isEmpty())
        assertEquals("", referenceLinkHandler.find("[ignored]"))
    }

    @Test
    fun linksFlowDoesNotEmitWhenLookupDisabled() = runTest {
        val state = streamingState(lookupLinks = false)
        val links = mutableListOf<Map<String, String?>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            state.links.collect { links += it }
        }

        state.append("[ignored]: https://ignored.example\n\n")
        state.append("body")
        advanceUntilIdle()

        assertEquals(listOf(emptyMap()), links)
        assertTrue(state.links.value.isEmpty())
    }

    private fun streamingState(
        lookupLinks: Boolean = true,
        referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    ): StreamingMarkdownStateImpl {
        val flavour = GFMFlavourDescriptor()
        return StreamingMarkdownStateImpl(
            Input(
                content = "",
                lookupLinks = lookupLinks,
                flavour = flavour,
                parser = MarkdownParser(flavour),
                referenceLinkHandler = referenceLinkHandler,
                retainState = true,
            )
        )
    }

    private fun List<ASTNode>.types() = map { it.type }
}
