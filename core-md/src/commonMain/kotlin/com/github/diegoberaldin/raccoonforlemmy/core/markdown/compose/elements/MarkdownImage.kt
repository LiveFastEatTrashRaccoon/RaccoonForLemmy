package com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.utils.findChildOfTypeRecursive
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
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

    val painterResource = asyncPainterResource(
        data = link,
        filterQuality = FilterQuality.Medium,
    )
    KamelImage(
        resource = painterResource,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth(),
        onFailure = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(MR.strings.message_image_loading_error)
            )
        },
        onLoading = { progress ->
            CircularProgressIndicator(
                progress = progress,
                color = MaterialTheme.colorScheme.primary,
            )
        },
    )
}
