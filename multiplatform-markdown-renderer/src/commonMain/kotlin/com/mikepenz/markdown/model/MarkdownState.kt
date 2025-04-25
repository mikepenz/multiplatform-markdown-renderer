package com.mikepenz.markdown.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.mikepenz.markdown.utils.lookupLinkDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

/**
 * A [MarkdownState] that executes the parsing of the markdown content with the [MarkdownParser] asynchronously.
 *
 * @param content The markdown content to parse.
 * @param lookupLinks Whether to lookup links in the parsed tree or not.
 * @param flavour The [MarkdownFlavourDescriptor] to use for parsing.
 * @param parser The [MarkdownParser] to use for parsing.
 * @param referenceLinkHandler The [ReferenceLinkHandler] to use for storing links.
 * @param immediate Whether to parse the content immediately or not. (WARNING: This is not advices, as it will block the composition!)
 */
@Composable
fun rememberMarkdownState(
    content: String,
    lookupLinks: Boolean = true,
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    immediate: Boolean = LocalInspectionMode.current,
): MarkdownState {
    val input = Input(
        content = content,
        lookupLinks = lookupLinks,
        flavour = flavour,
        parser = parser,
        referenceLinkHandler = referenceLinkHandler,
    )
    val state = remember(input) { MarkdownState(input) }
    if (immediate) {
        state.parseBlocking()
    } else {
        LaunchedEffect(state) {
            state.parse()
        }
    }
    return state
}

/**
 * A [MarkdownState] that that executes the parsing of the markdown content with the [MarkdownParser] asynchronously.
 */
@Stable
class MarkdownState(
    val input: Input,
) {
    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(State.Loading(input.referenceLinkHandler))
    val state: StateFlow<State> = stateFlow.asStateFlow()

    private val linkStateFlow: MutableStateFlow<Map<String, String?>> = MutableStateFlow(emptyMap())
    val links: StateFlow<Map<String, String?>> = linkStateFlow.asStateFlow()

    /**
     * Parses the markdown content asynchronously using the Default dispatcher.
     * When a result is available it will be emitted to the [state] flow.
     */
    suspend fun parse(): State = withContext(Dispatchers.Default) {
        parseBlocking()
    }

    /**
     * Parses the markdown content synchronously.
     */
    internal fun parseBlocking(): State {
        return try {
            val parsedResult = input.parser.buildMarkdownTreeFromString(input.content)
            if (input.lookupLinks) {
                val links = mutableMapOf<String, String?>()
                lookupLinkDefinition(links, parsedResult, input.content, recursive = true)
                links.onEach { (key, value) -> input.referenceLinkHandler.store(key, value) }
                linkStateFlow.value = links
            }
            State.Success(parsedResult, input.content, input.lookupLinks, input.referenceLinkHandler)
        } catch (error: Throwable) {
            State.Error(error, input.referenceLinkHandler)
        }.also { result ->
            stateFlow.value = result
        }
    }
}

/**
 * Creates a [kotlinx.coroutines.flow.Flow] of [State] for use in non-composable contexts like view models.
 * As soon as the flow is collected, it will start parsing the content, and emit the state once ready.
 *
 * @param content The markdown content to parse.
 * @param lookupLinks Whether to lookup links in the parsed tree or not.
 * @param flavour The [MarkdownFlavourDescriptor] to use for parsing.
 * @param parser The [MarkdownParser] to use for parsing.
 * @param referenceLinkHandler The [ReferenceLinkHandler] to use for storing links.
 *
 * @return A [kotlinx.coroutines.flow.Flow] of [State] that will emit the parsing state.
 */
fun parseMarkdownFlow(
    content: String,
    lookupLinks: Boolean = true,
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
) = flow {
    emit(State.Loading(referenceLinkHandler))
    val markdownState = MarkdownState(
        Input(
            content = content,
            lookupLinks = lookupLinks,
            flavour = flavour,
            parser = parser,
            referenceLinkHandler = referenceLinkHandler,
        )
    )
    markdownState.parse()
    emitAll(markdownState.state)
}

/**
 * The input for the [MarkdownState].
 *
 * @param content The markdown content to parse.
 * @param lookupLinks Whether to lookup links in the parsed tree or not.
 * @param flavour The [MarkdownFlavourDescriptor] to use for parsing.
 * @param parser The [MarkdownParser] to use for parsing.
 * @param referenceLinkHandler The [ReferenceLinkHandler] to use for storing links.
 */
data class Input(
    val content: String,
    val lookupLinks: Boolean,
    val flavour: MarkdownFlavourDescriptor,
    val parser: MarkdownParser,
    val referenceLinkHandler: ReferenceLinkHandler,
)

/**
 * The current state of the [MarkdownState].
 */
@Stable
sealed interface State {

    /** The [ReferenceLinkHandler] to store links in. */
    val referenceLinkHandler: ReferenceLinkHandler

    /** The parsing is in-progress. */
    data class Loading(
        override val referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    ) : State

    /** The parsing was successful. */
    data class Success(
        val node: ASTNode,
        val content: String,
        val linksLookedUp: Boolean,
        override val referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    ) : State

    /** The parsing failed due to [Throwable]. */
    data class Error(
        val result: Throwable,
        override val referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    ) : State
}
