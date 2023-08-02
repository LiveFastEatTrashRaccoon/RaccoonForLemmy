package com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.utils.findChildOfTypeRecursive
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownImage(content: String, node: ASTNode) {
    val link =
        node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)
            ?.toString() ?: return

    val painterResource = asyncPainterResource(data = link)
    KamelImage(
        resource = painterResource,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth(),
    )
}
