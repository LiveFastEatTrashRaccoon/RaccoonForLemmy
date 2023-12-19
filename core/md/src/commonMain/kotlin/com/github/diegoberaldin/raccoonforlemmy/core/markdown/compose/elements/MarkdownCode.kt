package com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.elements

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.LocalMarkdownColors
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.LocalMarkdownTypography
import org.intellij.markdown.ast.ASTNode

@Composable
private fun MarkdownCode(
    code: String,
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val backgroundCodeColor = LocalMarkdownColors.current.backgroundCode
    Surface(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
    ) {
        Text(
            code,
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(8.dp),
            style = style,
            color = LocalMarkdownColors.current.text,
        )
    }
}

@Composable
internal fun MarkdownCodeFence(
    content: String,
    node: ASTNode,
) {
    // CODE_FENCE_START, FENCE_LANG, {content}, CODE_FENCE_END
    val start = node.children[2].startOffset
    val end = node.children[node.children.size - 2].endOffset
    MarkdownCode(content.subSequence(start, end).toString().replaceIndent())
}

@Composable
internal fun MarkdownCodeBlock(
    content: String,
    node: ASTNode,
) {
    val start = node.children[0].startOffset
    val end = node.children[node.children.size - 1].endOffset
    MarkdownCode(content.subSequence(start, end).toString().replaceIndent())
}
