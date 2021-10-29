package com.mikepenz.markdown.utils

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Interface to describe the [ReferenceLinkHandler]
 */
interface ReferenceLinkHandler {
    /** Keep the provided link */
    fun store(label: String, destination: String?)

    /** Returns the link for the provided label if it exists */
    fun find(label: String): String
}

/**
 * Local [ReferenceLinkHandler] provider
 */
val LocalReferenceLinkHandler = staticCompositionLocalOf<ReferenceLinkHandler> {
    error("CompositionLocal ReferenceLinkHandler not present")
}

/**
 * Implementation for [ReferenceLinkHandler] to resolve referenced link within the Markdown
 */
class ReferenceLinkHandlerImpl() : ReferenceLinkHandler {
    private val stored = mutableMapOf<String, String?>()
    override fun store(label: String, destination: String?) {
        stored[label] = destination
    }

    override fun find(label: String): String {
        return stored[label] ?: label
    }
}