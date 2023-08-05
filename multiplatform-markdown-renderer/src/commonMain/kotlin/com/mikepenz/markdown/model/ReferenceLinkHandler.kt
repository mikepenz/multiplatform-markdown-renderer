package com.mikepenz.markdown.model

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
 * Implementation for [ReferenceLinkHandler] to resolve referenced link within the Markdown
 */
class ReferenceLinkHandlerImpl : ReferenceLinkHandler {
    private val stored = mutableMapOf<String, String?>()
    override fun store(label: String, destination: String?) {
        stored[label] = destination
    }

    override fun find(label: String): String {
        return stored[label] ?: label
    }
}
