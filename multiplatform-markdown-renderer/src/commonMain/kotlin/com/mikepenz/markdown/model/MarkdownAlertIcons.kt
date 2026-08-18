package com.mikepenz.markdown.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * The icons rendered next to an alert title. Override via
 * [com.mikepenz.markdown.compose.LocalMarkdownAlertIcons], or pass an icon directly to
 * [com.mikepenz.markdown.compose.elements.MarkdownAlert].
 *
 * Supply `null` for a type to render its title without an icon.
 */
@Immutable
interface MarkdownAlertIcons {
    val note: ImageVector?
    val tip: ImageVector?
    val important: ImageVector?
    val warning: ImageVector?
    val caution: ImageVector?

    fun iconFor(type: MarkdownAlertType): ImageVector? = when (type) {
        MarkdownAlertType.NOTE -> note
        MarkdownAlertType.TIP -> tip
        MarkdownAlertType.IMPORTANT -> important
        MarkdownAlertType.WARNING -> warning
        MarkdownAlertType.CAUTION -> caution
    }
}

@Immutable
data class DefaultMarkdownAlertIcons(
    override val note: ImageVector? = MarkdownAlertIconDefaults.Note,
    override val tip: ImageVector? = MarkdownAlertIconDefaults.Tip,
    override val important: ImageVector? = MarkdownAlertIconDefaults.Important,
    override val warning: ImageVector? = MarkdownAlertIconDefaults.Warning,
    override val caution: ImageVector? = MarkdownAlertIconDefaults.Caution,
) : MarkdownAlertIcons

fun markdownAlertIcons(
    note: ImageVector? = MarkdownAlertIconDefaults.Note,
    tip: ImageVector? = MarkdownAlertIconDefaults.Tip,
    important: ImageVector? = MarkdownAlertIconDefaults.Important,
    warning: ImageVector? = MarkdownAlertIconDefaults.Warning,
    caution: ImageVector? = MarkdownAlertIconDefaults.Caution,
): MarkdownAlertIcons = DefaultMarkdownAlertIcons(
    note = note,
    tip = tip,
    important = important,
    warning = warning,
    caution = caution,
)

/**
 * The bundled alert icons. Defined here rather than pulled from `compose-material-icons` so the
 * core module keeps its `compileOnly` runtime/ui/foundation dependency set.
 *
 * Icons are tinted at draw time, so the baked-in fill colour is irrelevant.
 */
object MarkdownAlertIconDefaults {
    /** Outlined info circle. */
    val Note: ImageVector by lazy {
        icon(
            "MarkdownAlertNote",
            "M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 " +
                "18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z",
        )
    }

    /** Outlined lightbulb. */
    val Tip: ImageVector by lazy {
        icon(
            "MarkdownAlertTip",
            "M9 21c0 .55.45 1 1 1h4c.55 0 1-.45 1-1v-1H9v1zm3-19C8.14 2 5 5.14 5 9c0 2.38 1.19 4.47 3 " +
                "5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.86-3.14-7-7-7zm2.85 " +
                "11.1l-.85.6V16h-4v-2.3l-.85-.6C7.8 12.16 7 10.63 7 9c0-2.76 2.24-5 5-5s5 2.24 5 5c0 1.63-.8 " +
                "3.16-2.15 4.1z",
        )
    }

    /** Outlined speech bubble with an exclamation mark. */
    val Important: ImageVector by lazy {
        icon(
            "MarkdownAlertImportant",
            "M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H5.17l-.59.59-.58." +
                "58V4h16v12zm-9-4h2v2h-2zm0-6h2v4h-2z",
        )
    }

    /** Outlined triangle with an exclamation mark. */
    val Warning: ImageVector by lazy {
        icon(
            "MarkdownAlertWarning",
            "M12 5.99L19.53 19H4.47L12 5.99M12 2L1 21h22L12 2zm1 14h-2v2h2v-2zm0-6h-2v4h2v-4z",
        )
    }

    /** Outlined octagon with an exclamation mark. */
    val Caution: ImageVector by lazy {
        icon(
            "MarkdownAlertCaution",
            "M15.73 3H8.27L3 8.27v7.46L8.27 21h7.46L21 15.73V8.27L15.73 3zM19 14.9L14.9 19H9.1L5 14.9V9.1L9.1 " +
                "5h5.8L19 9.1v5.8zM11 7h2v6h-2zm0 8h2v2h-2z",
        )
    }

    private fun icon(name: String, pathData: String): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = addPathNodes(pathData),
        fill = SolidColor(Color.Black),
    ).build()
}
