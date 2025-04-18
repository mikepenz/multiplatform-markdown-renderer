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
 * Implementation for [ReferenceLinkHandler] to resolve referenced link within the Markdown.
 *
 * The label is stored lowercase to allow case insensitive lookups. (https://github.com/adam-p/markdown-here/wiki/markdown-cheatsheet#links)
 */
class ReferenceLinkHandlerImpl : ReferenceLinkHandler {
    private val stored = mutableMapOf<String, String?>()

    override fun store(label: String, destination: String?) {
        stored[label.lowercase()] = destination
    }

    override fun find(label: String): String {
        return stored[label.lowercase()] ?: ""
    }
}
