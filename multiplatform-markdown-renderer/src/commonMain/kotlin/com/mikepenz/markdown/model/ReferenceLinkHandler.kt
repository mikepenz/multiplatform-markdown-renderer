package com.mikepenz.markdown.model

interface ReferenceLinkHandler {
    fun store(label: String, destination: String?)
    fun find(label: String): String
}
