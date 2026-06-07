package com.mikepenz.markdown.sample

import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.asMarkdownState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FlowMarkdownViewModel {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _markdownContent = MutableStateFlow(generateMarkdownContent(0))

    private val _updateCount = MutableStateFlow(0)
    val updateCount: StateFlow<Int> = _updateCount.asStateFlow()

    private val _retainState = MutableStateFlow(false)
    val retainState: StateFlow<Boolean> = _retainState.asStateFlow()

    private val _autoUpdate = MutableStateFlow(false)
    val autoUpdate: StateFlow<Boolean> = _autoUpdate.asStateFlow()

    private var autoUpdateJob: Job? = null

    // Markdown state flow that transforms the markdown content
    // Uses flatMapLatest to react to retainState changes
    val markdownState: StateFlow<State> = _retainState
        .flatMapLatest { retain ->
            _markdownContent.asMarkdownState(retainState = retain)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = State.Loading()
        )

    fun updateContent() {
        val newCount = _updateCount.value + 1
        _updateCount.value = newCount
        _markdownContent.value = generateMarkdownContent(newCount)
    }

    fun reset() {
        _updateCount.value = 0
        _markdownContent.value = generateMarkdownContent(0)
    }

    fun setRetainState(retain: Boolean) {
        _retainState.value = retain
    }

    fun setAutoUpdate(enabled: Boolean) {
        _autoUpdate.value = enabled

        autoUpdateJob?.cancel() // cancel any existing job to avoid leaking a second loop
        if (enabled) {
            autoUpdateJob = viewModelScope.launch {
                while (true) {
                    delay(2000)
                    updateContent()
                }
            }
        } else {
            autoUpdateJob = null
        }
    }

    fun dispose() {
        autoUpdateJob?.cancel()
        viewModelScope.cancel()
    }

    private fun generateMarkdownContent(count: Int): String {
        return buildString {
            if (count == 0) {
                appendLine("# Initial Content")
                appendLine()
                appendLine("This is the initial markdown content.")
                appendLine()
                appendLine("Click **Update Content** to generate new markdown from the flow.")
            } else {
                appendLine("# Flow Markdown Demo")
                appendLine()
                appendLine("## Update #$count")
                appendLine()
                appendLine("This content was generated dynamically from a `Flow<String>` in the ViewModel.")
                appendLine()
                appendLine("### Features")
                appendLine()
                appendLine("- **Flow-based**: Content comes from a `StateFlow<String>` in the ViewModel")
                appendLine("- **Reactive**: Updates automatically when the flow emits")
                appendLine("- **Efficient**: Reuses the same `MarkdownStateImpl` instance")
                appendLine("- **Retain State**: Toggle to see the difference in behavior")
                appendLine()
                appendLine("### Current State")
                appendLine()
                appendLine("- Total updates: **$count**")
                appendLine()
                appendLine("### Code Example")
                appendLine()
                appendLine("```kotlin")
                appendLine("class FlowMarkdownViewModel {")
                appendLine("    private val _markdownContent = MutableStateFlow(\"...\")")
                appendLine("    ")
                appendLine("    val markdownState: StateFlow<State> = _markdownContent")
                appendLine("        .asMarkdownState()")
                appendLine("        .stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading())")
                appendLine("}")
                appendLine("```")
            }
        }
    }
}
