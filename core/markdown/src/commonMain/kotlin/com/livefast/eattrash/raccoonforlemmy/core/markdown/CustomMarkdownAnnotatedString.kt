package com.livefast.eattrash.raccoonforlemmy.core.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.utils.MARKDOWN_TAG_URL
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

//internal var previousChild: ASTNode? = null
internal var nextChild:ASTNode? = null

internal fun List<ASTNode>.innerList(): List<ASTNode> = this.subList(1, this.size - 1)

@Composable
internal fun AnnotatedString.Builder.appendMarkdownLink(content: String, node: ASTNode) {
    val linkText = node.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.children?.innerList()
    if (linkText == null) {
        append(node.getTextInNode(content).toString())
        return
    }
    val destination = node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
        ?.getTextInNode(content)
        ?.toString()
    val linkLabel = node.findChildOfType(MarkdownElementTypes.LINK_LABEL)
        ?.getTextInNode(content)?.toString()
    val annotation = destination ?: linkLabel
    if (annotation != null) pushStringAnnotation(MARKDOWN_TAG_URL, annotation)
    pushStyle(
        SpanStyle(
            color = LocalMarkdownColors.current.linkText,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
    )
    buildMarkdownAnnotatedString(content, linkText)
    pop()
    if (annotation != null) pop()
}

@Composable
internal fun AnnotatedString.Builder.appendAutoLink(content: String, node: ASTNode) {
    val targetNode = node.children.firstOrNull {
        it.type.name == MarkdownElementTypes.AUTOLINK.name
    } ?: node
    val destination = targetNode.getTextInNode(content).toString()
    pushStringAnnotation(MARKDOWN_TAG_URL, (destination))
    pushStyle(
        SpanStyle(
            color = LocalMarkdownColors.current.linkText,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
    )
    append(destination)
    pop()
}

@Composable
fun AnnotatedString.Builder.buildCustomMarkdownAnnotatedString(content: String, children: List<ASTNode>) {
    var insideTags = false
    children.forEachIndexed { index, child ->
        val previousChild = if (index > 0) children[index - 1] else null
        val nextChild = if (index < children.size - 1) children[index + 1] else null
        when (child.type) {
            MarkdownElementTypes.PARAGRAPH -> buildCustomMarkdownAnnotatedString(content, child.children)
            MarkdownElementTypes.IMAGE -> child.findChildOfTypeRecursive(
                MarkdownElementTypes.LINK_DESTINATION
            )?.let {
                //appendInlineContent(MARKDOWN_TAG_IMAGE_URL, it.getTextInNode(content).toString())
                /*CustomMarkdownImage(
                    url = it.getTextInNode(content).toString(),
                    null,
                    true,
                )*/
            }

            MarkdownElementTypes.UNORDERED_LIST -> buildCustomMarkdownAnnotatedString(content, child.children)
            MarkdownElementTypes.LIST_ITEM -> {
                append("\u2022")
                append("\t\t")
                buildCustomMarkdownAnnotatedString(content, child.children)
            }

            MarkdownElementTypes.EMPH -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                buildCustomMarkdownAnnotatedString(content, child.children)
                pop()
            }

            MarkdownElementTypes.STRONG -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                buildCustomMarkdownAnnotatedString(content, child.children)
                pop()
            }

            GFMElementTypes.STRIKETHROUGH -> {
                pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                buildCustomMarkdownAnnotatedString(content, child.children)
                pop()
            }

            MarkdownElementTypes.CODE_SPAN -> {
                pushStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        color = LocalMarkdownColors.current.inlineCodeText,
                        background = LocalMarkdownColors.current.inlineCodeBackground
                    )
                )
                append(' ')
                buildCustomMarkdownAnnotatedString(content, child.children.innerList())
                append(' ')
                pop()
            }

            MarkdownElementTypes.AUTOLINK -> appendAutoLink(content, child)
            MarkdownElementTypes.INLINE_LINK -> appendMarkdownLink(content, child)
            MarkdownElementTypes.SHORT_REFERENCE_LINK -> appendMarkdownLink(content, child)
            MarkdownElementTypes.FULL_REFERENCE_LINK -> appendMarkdownLink(content, child)
            GFMTokenTypes.GFM_AUTOLINK -> if (child.parent == MarkdownElementTypes.LINK_TEXT) {
                append(child.getTextInNode(content).toString())
            } else appendAutoLink(content, child)

            MarkdownTokenTypes.TEXT -> {
                if (insideTags) {
                    if (previousChild?.type?.name == "~") {
                        pushStyle(SpanStyle(
                            baselineShift = BaselineShift(-.2F),
                            fontSize = LocalMarkdownTypography.current.paragraph.fontSize * .8,
                        ))
                    }
                }

                buildCustomMarkdownText(content, child)
                if (insideTags) {
                    pop()
                }
            }

            MarkdownTokenTypes.SINGLE_QUOTE -> append('\'')
            MarkdownTokenTypes.DOUBLE_QUOTE -> append('\"')
            MarkdownTokenTypes.LPAREN -> append('(')
            MarkdownTokenTypes.RPAREN -> append(')')
            MarkdownTokenTypes.LBRACKET -> append('[')
            MarkdownTokenTypes.RBRACKET -> append(']')
            MarkdownTokenTypes.LT -> append('<')
            MarkdownTokenTypes.GT -> append('>')
            MarkdownTokenTypes.COLON -> append(':')
            MarkdownTokenTypes.EXCLAMATION_MARK -> append('!')
            MarkdownTokenTypes.BACKTICK -> append('`')
            MarkdownTokenTypes.HARD_LINE_BREAK -> append("\n\n")
            MarkdownTokenTypes.EOL -> append('\n')
            MarkdownTokenTypes.WHITE_SPACE -> if (length > 0) {
                append(' ')
            }

            else -> {
                if (child.type.name == "~") {
                    if (insideTags) {
                        insideTags = false
                    } else if (nextChild?.type == MarkdownTokenTypes.TEXT && children.size > index + 2 && children[index + 2].type.name == "~") {
                        insideTags = true
                    } else {
                        append("~")
                    }
                }
            }
        }
    }
}

@Composable
internal fun AnnotatedString.Builder.buildCustomMarkdownText(content: String, child: ASTNode) {
    when {
        // Superscript handler
        content.substring(child.startOffset, child.endOffset).contains(Regex("\\^\\S+\\^")) -> {
            val text = content.substring(child.startOffset, child.endOffset)
            var startIndex = 0
            Regex("\\^\\S+\\^").findAll(text).forEach {
                if (it.range.first > 0) {
                    append(text.substring(startIndex, it.range.first))
                }
                pushStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift(.4F),
                        fontSize = LocalMarkdownTypography.current.paragraph.fontSize * .8,
                    ))
                append(text.substring(it.range.first + 1, it.range.last))
                pop()
                startIndex = it.range.last
            }
        }

        else -> {
            append(content.substring(child.startOffset, child.endOffset))
        }
    }
    if (nextChild != null && nextChild?.type == MarkdownElementTypes.IMAGE) {
        append("\n\n")
    }
}