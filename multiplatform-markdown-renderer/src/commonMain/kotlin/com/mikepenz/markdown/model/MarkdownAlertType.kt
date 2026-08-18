package com.mikepenz.markdown.model

/**
 * The five GitHub alert types, as recognised by the GFM parser's `ALERT_TITLE` token.
 *
 * See https://github.com/orgs/community/discussions/16925
 */
enum class MarkdownAlertType(
    /** The marker as written in markdown, e.g. `[!NOTE]`. Matched case-insensitively. */
    val marker: String,
    /** The default, English title rendered next to the icon. */
    val title: String,
) {
    NOTE("[!NOTE]", "Note"),
    TIP("[!TIP]", "Tip"),
    IMPORTANT("[!IMPORTANT]", "Important"),
    WARNING("[!WARNING]", "Warning"),
    CAUTION("[!CAUTION]", "Caution"),
    ;

    companion object {
        /**
         * Resolves the [MarkdownAlertType] for the text of an `ALERT_TITLE` token, e.g. `[!note]`.
         * Returns `null` for anything the parser would not have accepted as an alert.
         */
        fun from(markerText: String): MarkdownAlertType? {
            val trimmed = markerText.trim()
            return entries.firstOrNull { it.marker.equals(trimmed, ignoreCase = true) }
        }
    }
}
