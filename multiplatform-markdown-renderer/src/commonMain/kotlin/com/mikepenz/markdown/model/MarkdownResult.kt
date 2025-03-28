package com.mikepenz.markdown.model

import org.intellij.markdown.ast.ASTNode

/**
 * Helper class to hold the result of parsing the markdown content
 */
sealed class MarkdownResult {
    object Loading : MarkdownResult()
    object Error : MarkdownResult()
    class Success(val result: ASTNode) : MarkdownResult()
}