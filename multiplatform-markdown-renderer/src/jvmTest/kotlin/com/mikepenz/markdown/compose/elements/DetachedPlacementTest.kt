package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.platform.testTag
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import kotlin.test.Test

/**
 * Regression test for #639: `onPlaced` read `parentLayoutCoordinates` on detached coordinates
 * while lazy items / pager pages were recycled, throwing `IllegalStateException`.
 */
@OptIn(ExperimentalTestApi::class, ExperimentalFoundationApi::class)
class DetachedPlacementTest {
    private val colors = DefaultMarkdownColors(Color.Black, Color.LightGray, Color.LightGray, Color.Gray, Color.White)
    private val style = TextStyle.Default
    private val typography = DefaultMarkdownTypography(
        style, style, style, style, style, style, style, style, style, style, style, style, style, style,
        TextLinkStyles(), style,
    )
    private val content = "Some **bold** text and a [link](https://example.com) to recycle."

    @Test
    fun pagerWithLazyMarkdownSurvivesRecycling() = runComposeUiTest {
        setContent {
            HorizontalPager(rememberPagerState { 2 }, Modifier.fillMaxSize().testTag("pager")) { page ->
                LazyColumn(Modifier.fillMaxSize().testTag("list$page")) {
                    items(200) { Markdown(content, colors, typography, Modifier.height(48.dp), immediate = true) }
                }
            }
        }
        repeat(3) {
            // recycle lazy items on the visible page, then swipe to the other page and back
            repeat(5) { onNodeWithTag("pager").performTouchInput { swipeUp() } }
            onNodeWithTag("pager").performTouchInput { swipeLeft() }
            waitForIdle()
            repeat(5) { onNodeWithTag("pager").performTouchInput { swipeDown() } }
            onNodeWithTag("pager").performTouchInput { swipeRight() }
            waitForIdle()
        }
    }
}
