package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.mikepenz.markdown.compose.LocalBulletListHandler
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.LocalOrderedListHandler
import com.mikepenz.markdown.compose.MarkdownElement
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.utils.getUnescapedTextInNode
import kotlinx.collections.immutable.persistentMapOf
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes.Companion.LIST_BULLET
import org.intellij.markdown.MarkdownTokenTypes.Companion.LIST_NUMBER
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.CHECK_BOX

/** key used to store the current depth in the [MarkdownComponentModel.extra] */
private const val MARKDOWN_LIST_DEPTH_KEY = "markdown_list_depth"

@Composable
fun MarkdownListItems(
    content: String,
    node: ASTNode,
    depth: Int = 0,
    markerModifier: RowScope.() -> Modifier = { Modifier },
    listModifier: RowScope.() -> Modifier = { Modifier },
    bullet: @Composable (index: Int, listNumber: Int, child: ASTNode?) -> Unit,
) {
    val padding = LocalMarkdownPadding.current
    val markdownComponents = LocalMarkdownComponents.current
    val markdownTypography = LocalMarkdownTypography.current

    Column(
        modifier = Modifier.padding(
            start = padding.listIndent * depth,
            top = padding.list,
            bottom = padding.list
        )
    ) {
        // Retrieve initial list number to determine the starting number for ordered lists
        // https://spec.commonmark.org/0.31.2/#start-number
        val initialListNumber = node.findChildOfType(MarkdownElementTypes.LIST_ITEM)
            ?.getUnescapedTextInNode(content)
            ?.takeWhile(Char::isDigit)
            ?.toIntOrNull()
            ?: 1

        var index = 0
        node.children.forEach { child ->
            if (child.type == MarkdownElementTypes.LIST_ITEM) {
                MarkdownListItem(
                    content = content,
                    child = child,
                    node = node,
                    index = index,
                    listNumber = initialListNumber,
                    depth = depth,
                    markdownComponents = markdownComponents,
                    markdownTypography = markdownTypography,
                    padding = padding,
                    markerModifier = markerModifier,
                    listModifier = listModifier,
                    bullet = bullet
                )
                index++
            }
        }
    }
}

/**
 * Renders a single list item
 */
@Composable
private fun MarkdownListItem(
    content: String,
    child: ASTNode,
    node: ASTNode,
    index: Int,
    listNumber: Int,
    depth: Int,
    markdownComponents: MarkdownComponents,
    markdownTypography: MarkdownTypography,
    padding: MarkdownPadding,
    markerModifier: RowScope.() -> Modifier,
    listModifier: RowScope.() -> Modifier,
    bullet: @Composable (index: Int, listNumber: Int, child: ASTNode?) -> Unit,
) {
    val checkboxNode = child.children.getOrNull(1)?.takeIf { it.type == CHECK_BOX }
    val listIndicator = when (node.type) {
        ORDERED_LIST -> child.findChildOfType(LIST_NUMBER)
        UNORDERED_LIST -> child.findChildOfType(LIST_BULLET)
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding.listItemTop, bottom = padding.listItemBottom)
    ) {
        // Render marker symbol (checkbox or bullet)
        Box(modifier = markerModifier()) {
            if (checkboxNode != null) {
                val model = MarkdownComponentModel(
                    content = content,
                    node = checkboxNode,
                    typography = markdownTypography,
                    extra = persistentMapOf(MARKDOWN_LIST_DEPTH_KEY to depth + 1)
                )
                markdownComponents.checkbox.invoke(model)
            } else {
                bullet(index, listNumber, listIndicator)
            }
        }

        // Render list item content
        Column(modifier = listModifier()) {
            child.children.forEach { nestedChild ->
                MarkdownNestedListItem(
                    nestedChild = nestedChild,
                    content = content,
                    depth = depth,
                    markdownComponents = markdownComponents,
                    markdownTypography = markdownTypography
                )
            }
        }
    }
}

/**
 * Renders nested list item content
 */
@Composable
private fun MarkdownNestedListItem(
    nestedChild: ASTNode,
    content: String,
    depth: Int,
    markdownComponents: MarkdownComponents,
    markdownTypography: MarkdownTypography,
) {
    when (nestedChild.type) {
        ORDERED_LIST -> {
            val model = MarkdownComponentModel(
                content = content,
                node = nestedChild,
                typography = markdownTypography,
                extra = persistentMapOf(MARKDOWN_LIST_DEPTH_KEY to depth + 1)
            )
            markdownComponents.orderedList.invoke(model)
        }

        UNORDERED_LIST -> {
            val model = MarkdownComponentModel(
                content = content,
                node = nestedChild,
                typography = markdownTypography,
                extra = persistentMapOf(MARKDOWN_LIST_DEPTH_KEY to depth + 1)
            )
            markdownComponents.unorderedList.invoke(model)
        }

        else -> {
            MarkdownElement(
                node = nestedChild,
                components = markdownComponents,
                content = content,
                includeSpacer = false
            )
        }
    }
}

@Composable
fun MarkdownOrderedList(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.ordered,
    depth: Int = 0,
    markerModifier: RowScope.() -> Modifier = { Modifier },
    listModifier: RowScope.() -> Modifier = { Modifier },
) {
    val orderedListHandler = LocalOrderedListHandler.current
    MarkdownListItems(content, node, depth, markerModifier, listModifier) { index, listNumber, child ->
        MarkdownBasicText(
            text = orderedListHandler.transform(
                type = LIST_NUMBER,
                bullet = child?.getUnescapedTextInNode(content),
                index = index,
                listNumber = listNumber,
                depth = depth
            ),
            style = style,
        )
    }
}

@Composable
fun MarkdownBulletList(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.bullet,
    depth: Int = 0,
    markerModifier: RowScope.() -> Modifier = { Modifier },
    listModifier: RowScope.() -> Modifier = { Modifier },
) {
    val bulletHandler = LocalBulletListHandler.current
    val listItemBottom = LocalMarkdownPadding.current.listItemBottom
    MarkdownListItems(content, node, depth, markerModifier, listModifier) { index, listNumber, child ->
        MarkdownBasicText(
            text = bulletHandler.transform(
                type = LIST_BULLET,
                bullet = child?.getUnescapedTextInNode(content),
                index = index,
                listNumber = listNumber,
                depth = depth
            ),
            style = style,
            modifier = Modifier.padding(bottom = listItemBottom)
        )
    }
}

/**
 * Retrieve the current list depth from the [MarkdownComponentModel]
 */
val MarkdownComponentModel.listDepth: Int
    get() = (extra[MARKDOWN_LIST_DEPTH_KEY] as? Int) ?: 0