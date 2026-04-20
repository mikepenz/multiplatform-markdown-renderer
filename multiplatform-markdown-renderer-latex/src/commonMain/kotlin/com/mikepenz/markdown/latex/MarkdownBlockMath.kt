package com.mikepenz.markdown.latex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.latex.model.DisplayList
import com.mikepenz.markdown.utils.extractCodeFenceContent
import com.mikepenz.markdown.utils.extractMathContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

val latexBlockMath: MarkdownComponent = { model ->
    MarkdownBlockMath(model.content, model.node)
}

@Composable
fun MarkdownBlockMath(content: String, node: ASTNode) {
    val mathEngine = rememberMathEngine()
    val color = LocalMarkdownColors.current.text
    val typography = LocalMarkdownTypography.current
    val fontSize = typography.paragraph.fontSize.value

    val latex = when (node.type) {
        MarkdownElementTypes.CODE_FENCE -> node.extractCodeFenceContent(content)?.second ?: ""
        else -> node.extractMathContent(content)
    }

    if (latex.isBlank()) return

    val displayList by produceState<DisplayList?>(initialValue = null, key1 = latex) {
        value = try {
            mathEngine.parse(latex)
        } catch (_: Exception) {
            null
        }
    }

    when (val dl = displayList) {
        null -> {
            BasicText(
                text = latex,
                style = typography.code.copy(color = color),
            )
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                MathCanvas(
                    displayList = dl,
                    fontSize = fontSize,
                    color = color,
                )
            }
        }
    }
}
