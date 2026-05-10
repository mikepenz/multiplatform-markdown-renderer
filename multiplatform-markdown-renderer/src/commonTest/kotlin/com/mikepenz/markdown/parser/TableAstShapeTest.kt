package com.mikepenz.markdown.parser

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Investigative tests that document the EXACT AST shape produced by
 * `org.jetbrains:markdown` 0.7.3 (GFMFlavourDescriptor) for the contents
 * of GFM table CELL nodes.
 *
 * These tests intentionally `println` the structure so the output can be
 * inspected via `gradlew :multiplatform-markdown-renderer:jvmTest --info`
 * or in the HTML test report. Assertions are deliberately loose; the
 * primary purpose is documentation / regression-detection.
 */
class TableAstShapeTest {

    private fun parse(input: String): ASTNode =
        MarkdownParser(GFMFlavourDescriptor()).buildMarkdownTreeFromString(input)

    /** Pretty-print an AST subtree with text snippets. */
    private fun dump(node: ASTNode, content: String, indent: String = ""): String {
        val sb = StringBuilder()
        val text = node.getTextInNode(content).toString()
            .replace("\n", "\\n")
            .let { if (it.length > 60) it.substring(0, 60) + "…" else it }
        sb.append(indent).append(node.type.name).append("  [").append(text).append("]\n")
        for (child in node.children) {
            sb.append(dump(child, content, "$indent  "))
        }
        return sb.toString()
    }

    private fun findAll(node: ASTNode, typeName: String, into: MutableList<ASTNode> = mutableListOf()): List<ASTNode> {
        if (node.type.name == typeName) into.add(node)
        node.children.forEach { findAll(it, typeName, into) }
        return into
    }

    /** Returns the list of CELL nodes in document order. */
    private fun cells(tree: ASTNode): List<ASTNode> = findAll(tree, GFMTokenTypes.CELL.name)

    private fun runScenario(label: String, input: String) {
        val tree = parse(input)
        println("======== $label ========")
        println("INPUT:")
        println(input)
        println("FULL TREE:")
        println(dump(tree, input))
        val tables = findAll(tree, GFMElementTypes.TABLE.name)
        println("TABLES FOUND: ${tables.size}")
        val cellList = cells(tree)
        println("CELLS FOUND: ${cellList.size}")
        cellList.forEachIndexed { i, c ->
            println("-- CELL[$i] text='${c.getTextInNode(input)}' children=${c.children.size}")
            c.children.forEachIndexed { j, ch ->
                println("    child[$j] type=${ch.type.name} text='${ch.getTextInNode(input)}'")
            }
        }
        assertNotNull(tables.firstOrNull(), "Expected a TABLE in: $input")
        assertTrue(cellList.isNotEmpty(), "Expected CELL nodes in: $input")
    }

    private val header = "| h1 | h2 |\n|----|----|\n"

    @Test fun plainText() = runScenario("plain TEXT", header + "| hello world | x |\n")

    @Test fun strong() = runScenario("**bold**", header + "| **bold** | x |\n")

    @Test fun emphAsterisk() = runScenario("*italic*", header + "| *italic* | x |\n")

    @Test fun emphUnderscore() = runScenario("_italic_", header + "| _italic_ | x |\n")

    @Test fun strikethrough() = runScenario("~~strike~~", header + "| ~~strike~~ | x |\n")

    @Test fun codeSpan() = runScenario("`code`", header + "| `code` | x |\n")

    @Test fun inlineLink() = runScenario("[link](url)", header + "| [link](https://x) | x |\n")

    @Test fun autoLinkAngle() = runScenario("<https://x>", header + "| <https://x> | x |\n")

    @Test fun gfmAutoLinkBare() = runScenario("bare https://x", header + "| https://x | x |\n")

    @Test fun fullReferenceLink() = runScenario(
        "[ref][label]",
        header + "| [ref][label] | x |\n\n[label]: https://x\n"
    )

    @Test fun shortReferenceLink() = runScenario(
        "[ref]",
        header + "| [ref] | x |\n\n[ref]: https://x\n"
    )

    @Test fun inlineImage() = runScenario("![alt](url)", header + "| ![alt](https://x.png) | x |\n")

    @Test fun escapedPipe() = runScenario("escaped \\|", header + "| a \\| b | x |\n")

    @Test fun hardLineBreakTwoSpaces() = runScenario(
        "two-trailing-space hard break",
        header + "| line1  \nline2 | x |\n"
    )

    @Test fun htmlSpan() = runScenario("<span>x</span>", header + "| <span>x</span> | y |\n")

    @Test fun mixedInline() = runScenario(
        "mixed: bold + code + link",
        header + "| **b** and `c` and [l](https://x) | y |\n"
    )
}
