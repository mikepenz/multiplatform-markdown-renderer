package com.mikepenz.markdown.ui.m3

import androidx.compose.runtime.Composable
import com.mikepenz.markdown.ui.annotation.DarkLightPreview
import com.mikepenz.markdown.ui.m3.util.TestMarkdown

/**
 * Covers the GitHub alert syntax as documented
 * https://github.com/orgs/community/discussions/16925
 */
@DarkLightPreview
@Composable
fun AlertNoteTest() = TestMarkdown(
    """
        > [!NOTE]
        > Useful information that users should know, even when skimming content.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertTipTest() = TestMarkdown(
    """
        > [!TIP]
        > Helpful advice for doing things better or more easily.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertImportantTest() = TestMarkdown(
    """
        > [!IMPORTANT]
        > Key information users need to know to achieve their goal.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertWarningTest() = TestMarkdown(
    """
        > [!WARNING]
        > Urgent info that needs immediate user attention to avoid problems.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertCautionTest() = TestMarkdown(
    """
        > [!CAUTION]
        > Advises about risks or negative outcomes of certain actions.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertWithLowercaseMarkerTest() = TestMarkdown(
    """
        > [!tip]
        > The marker is matched case-insensitively.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertWithMultipleParagraphsTest() = TestMarkdown(
    """
        > [!IMPORTANT]
        > Key information users need to know.
        >
        > *Everything* is going according to **plan**.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertWithOtherElementsTest() = TestMarkdown(
    """
        > [!TIP]
        > #### The quarterly results look great!
        >
        > - Revenue was off the chart.
        > - Profits were higher than ever.
        >
        > Read more on [GitHub](https://github.com/mikepenz).
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertWithNestedBlockquoteTest() = TestMarkdown(
    """
        > [!NOTE]
        > Useful information that users should know.
        >
        >> A nested quote inside the alert.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun UnknownAlertMarkerRendersAsBlockquoteTest() = TestMarkdown(
    """
        > [!NONSENSE]
        > An unrecognised marker is left as a plain blockquote.
        """.trimIndent()
)

@DarkLightPreview
@Composable
fun AlertsAndBlockquoteTogetherTest() = TestMarkdown(
    """
        > [!WARNING]
        > Urgent info that needs immediate user attention.

        > Dorothy followed her through many of the beautiful rooms in her castle.
        """.trimIndent()
)
