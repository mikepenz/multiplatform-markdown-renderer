package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import org.intellij.markdown.ast.ASTNode

/** Default definition for the [MarkdownHighlightedCodeFence]. Uses default theme, attempts to apply language from markdown. */
val highlightedCodeFence: MarkdownComponent = { MarkdownHighlightedCodeFence(content = it.content, node = it.node, style = it.typography.code) }

/** Default definition for the [MarkdownHighlightedCodeBlock]. Uses default theme, attempts to apply language from markdown. */
val highlightedCodeBlock: MarkdownComponent = { MarkdownHighlightedCodeBlock(content = it.content, node = it.node, style = it.typography.code) }

@Composable
fun MarkdownHighlightedCodeFence(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.code,
    highlights: Highlights.Builder = rememberHighlightsBuilder(),
) {
    MarkdownCodeFence(content, node, style) { code, language, style ->
        MarkdownHighlightedCode(code = code, language = language, highlights = highlights, style = style)
    }
}

@Composable
fun MarkdownHighlightedCodeBlock(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.code,
    highlights: Highlights.Builder = rememberHighlightsBuilder(),
) {
    MarkdownCodeBlock(content, node, style) { code, language, style ->
        MarkdownHighlightedCode(code = code, language = language, highlights = highlights, style = style)
    }
}

@Composable
fun MarkdownHighlightedCode(
    code: String,
    language: String?,
    highlights: Highlights.Builder = rememberHighlightsBuilder(),
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val backgroundCodeColor = LocalMarkdownColors.current.codeBackground
    val codeBackgroundCornerSize = LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock
    val syntaxLanguage = remember(language) { language?.let { SyntaxLanguage.getByName(it) } }

    val codeHighlights by remembering(code) {
        derivedStateOf {
            highlights
                .code(code)
                .let { if (syntaxLanguage != null) it.language(syntaxLanguage) else it }
                .build()
        }
    }

    MarkdownCodeBackground(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(codeBackgroundCornerSize),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        @Suppress("DEPRECATION")
        MarkdownBasicText(
            text = buildAnnotatedString {
                text(codeHighlights.getCode())
                codeHighlights.getHighlights()
                    .filterIsInstance<ColorHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
                codeHighlights.getHighlights()
                    .filterIsInstance<BoldHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(codeBlockPadding),
            style = style
        )
    }
}

@Composable
private fun rememberHighlightsBuilder(): Highlights.Builder {
    val isDarkTheme = isSystemInDarkTheme()
    return remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }
}

@Composable
internal inline fun <T, K> remembering(
    key1: K,
    crossinline calculation: @DisallowComposableCalls (K) -> T,
): T = remember(key1) { calculation(key1) }

internal fun AnnotatedString.Builder.text(text: String, style: SpanStyle = SpanStyle()) = withStyle(style = style) {
    append(text)
}