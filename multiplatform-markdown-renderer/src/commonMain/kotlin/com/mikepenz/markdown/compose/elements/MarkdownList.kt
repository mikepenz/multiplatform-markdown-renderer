package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.compose.handleElement
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
    bullet: @Composable (index: Int, child: ASTNode?) -> Unit,
) {
    val listDp = LocalMarkdownPadding.current.list
    val indentListDp = LocalMarkdownPadding.current.listIndent
    val listItemPaddingDp = LocalMarkdownPadding.current.listItemTop
    val listItemBottom = LocalMarkdownPadding.current.listItemBottom
    val markdownComponents = LocalMarkdownComponents.current
    val markdownTypography = LocalMarkdownTypography.current
    Column(
        modifier = Modifier.padding(
            start = (indentListDp) * depth,
            top = listDp,
            bottom = listDp
        )
    ) {
        var index = 0
        node.children.forEach { child ->
            when (child.type) {
                MarkdownElementTypes.LIST_ITEM -> {
                    // LIST_NUMBER/LIST_BULLET, CHECK_BOX, PARAGRAPH
                    val checkboxNode = child.children.getOrNull(1)?.takeIf { it.type == CHECK_BOX }
                    val listIndicator = when (node.type) {
                        ORDERED_LIST -> child.findChildOfType(LIST_NUMBER)
                        UNORDERED_LIST -> child.findChildOfType(LIST_BULLET)
                        else -> null
                    }

                    Row(Modifier.fillMaxWidth().padding(top = listItemPaddingDp, bottom = listItemBottom)) {
                        if (checkboxNode != null) {
                            Column {
                                val model = MarkdownComponentModel(
                                    content = content,
                                    node = checkboxNode,
                                    typography = markdownTypography,
                                    extra = persistentMapOf(MARKDOWN_LIST_DEPTH_KEY to depth + 1)
                                )
                                markdownComponents.checkbox.invoke(model)
                            }
                        } else {
                            bullet(index, listIndicator)
                        }

                        Column {
                            child.children.onEach { nestedChild ->
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
                                        handleElement(
                                            node = nestedChild,
                                            components = markdownComponents,
                                            content = content,
                                            includeSpacer = false
                                        )
                                    }
                                }
                            }
                        }
                    }

                    index++
                }
            }
        }
    }
}

@Composable
fun MarkdownOrderedList(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.ordered,
    depth: Int = 0,
) {
    val orderedListHandler = LocalOrderedListHandler.current
    MarkdownListItems(content, node, depth) { index, child ->
        MarkdownBasicText(
            text = orderedListHandler.transform(
                type = LIST_NUMBER,
                bullet = child?.getUnescapedTextInNode(content),
                index = index,
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
) {
    val bulletHandler = LocalBulletListHandler.current
    val listItemBottom = LocalMarkdownPadding.current.listItemBottom
    MarkdownListItems(content, node, depth) { index, child ->
        MarkdownBasicText(
            text = bulletHandler.transform(
                type = LIST_BULLET,
                bullet = child?.getUnescapedTextInNode(content),
                index = index,
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