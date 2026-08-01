package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * The accent color (bar, icon, title) of each GitHub alert type.
 */
@Immutable
interface MarkdownAlertColors {
    /** Accent color of a `[!NOTE]` alert. */
    val note: Color

    /** Accent color of a `[!TIP]` alert. */
    val tip: Color

    /** Accent color of an `[!IMPORTANT]` alert. */
    val important: Color

    /** Accent color of a `[!WARNING]` alert. */
    val warning: Color

    /** Accent color of a `[!CAUTION]` alert. */
    val caution: Color

    fun colorFor(type: MarkdownAlertType): Color = when (type) {
        MarkdownAlertType.NOTE -> note
        MarkdownAlertType.TIP -> tip
        MarkdownAlertType.IMPORTANT -> important
        MarkdownAlertType.WARNING -> warning
        MarkdownAlertType.CAUTION -> caution
    }
}

@Immutable
private data class DefaultMarkdownAlertColors(
    override val note: Color,
    override val tip: Color,
    override val important: Color,
    override val warning: Color,
    override val caution: Color,
) : MarkdownAlertColors

/**
 * @param darkTheme Selects the light or dark variant of the default palette.
 */
fun markdownAlertColors(
    darkTheme: Boolean = false,
    note: Color = MarkdownAlertColorDefaults.note(darkTheme),
    tip: Color = MarkdownAlertColorDefaults.tip(darkTheme),
    important: Color = MarkdownAlertColorDefaults.important(darkTheme),
    warning: Color = MarkdownAlertColorDefaults.warning(darkTheme),
    caution: Color = MarkdownAlertColorDefaults.caution(darkTheme),
): MarkdownAlertColors = DefaultMarkdownAlertColors(
    note = note,
    tip = tip,
    important = important,
    warning = warning,
    caution = caution,
)

/**
 * GitHub's alert accent palette, in its light and dark variants.
 *
 * Material's color schemes carry no slot with the semantics these alerts rely on — a green "tip",
 * an amber "warning" — so the palette is fixed rather than derived from the active theme.
 */
object MarkdownAlertColorDefaults {
    fun note(darkTheme: Boolean): Color = if (darkTheme) Color(0xFF4493F8) else Color(0xFF0969DA)
    fun tip(darkTheme: Boolean): Color = if (darkTheme) Color(0xFF3FB950) else Color(0xFF1A7F37)
    fun important(darkTheme: Boolean): Color = if (darkTheme) Color(0xFFAB7DF8) else Color(0xFF8250DF)
    fun warning(darkTheme: Boolean): Color = if (darkTheme) Color(0xFFD29922) else Color(0xFF9A6700)
    fun caution(darkTheme: Boolean): Color = if (darkTheme) Color(0xFFF85149) else Color(0xFFCF222E)
}
