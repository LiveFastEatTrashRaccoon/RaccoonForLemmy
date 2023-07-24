package com.github.diegoberaldin.raccoonforlemmy.core_md.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.github.diegoberaldin.raccoonforlemmy.core_md.utils.findChildOfTypeRecursive
import com.seiko.imageloader.rememberImagePainter
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownImage(content: String, node: ASTNode) {
    val link =
        node.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)
            ?.toString() ?: return

    val painter = rememberImagePainter(link)
    Image(
        painter = painter,
        contentDescription = "Markdown Image", // TODO
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth()
    )
}
