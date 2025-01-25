package com.mikepenz.markdown.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.mikepenz.markdown.compose.*
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.utils.getUnescapedTextInNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes.Companion.LIST_BULLET
import org.intellij.markdown.MarkdownTokenTypes.Companion.LIST_NUMBER
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

@Composable
fun MarkdownListItems(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.list,
    level: Int = 0,
    bullet: @Composable (index: Int, child: ASTNode?) -> Unit,
) {
    val listDp = LocalMarkdownPadding.current.list
    val indentListDp = LocalMarkdownPadding.current.listIndent
    val listItemPaddingDp = LocalMarkdownPadding.current.listItemTop
    val listItemBottom = LocalMarkdownPadding.current.listItemBottom
    Column(
        modifier = Modifier.padding(
            start = (indentListDp) * level,
            top = listDp,
            bottom = listDp
        )
    ) {
        var index = 0
        node.children.forEach { child ->
            when (child.type) {
                MarkdownElementTypes.LIST_ITEM -> {
                    val listIndicator = when (node.type) {
                        ORDERED_LIST -> child.findChildOfType(LIST_NUMBER)
                        UNORDERED_LIST -> child.findChildOfType(LIST_BULLET)
                        else -> null
                    }

                    Row(Modifier.fillMaxWidth().padding(top = listItemPaddingDp, bottom = listItemBottom)) {
                        bullet(index, listIndicator)

                        Column {
                            child.children.onEach { nestedChild ->
                                when (nestedChild.type) {
                                    ORDERED_LIST -> MarkdownOrderedList(content, nestedChild, style, level + 1)
                                    UNORDERED_LIST -> MarkdownBulletList(content, nestedChild, style, level + 1)
                                    else -> {
                                        handleElement(
                                            node = nestedChild,
                                            components = LocalMarkdownComponents.current,
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
    level: Int = 0,
) {
    val orderedListHandler = LocalOrderedListHandler.current
    MarkdownListItems(content, node, style, level) { index, child ->
        MarkdownBasicText(
            text = orderedListHandler.transform(
                LIST_NUMBER,
                child?.getUnescapedTextInNode(content),
                index
            ),
            style = style,
            color = LocalMarkdownColors.current.text,
        )
    }
}

@Composable
fun MarkdownBulletList(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalMarkdownTypography.current.bullet,
    level: Int = 0,
) {
    val bulletHandler = LocalBulletListHandler.current
    val listItemBottom = LocalMarkdownPadding.current.listItemBottom
    MarkdownListItems(content, node, style, level) { index, child ->
        MarkdownBasicText(
            bulletHandler.transform(
                LIST_BULLET,
                child?.getUnescapedTextInNode(content),
                index
            ),
            style = style,
            color = LocalMarkdownColors.current.text,
            modifier = Modifier.padding(bottom = listItemBottom)
        )
    }
}
