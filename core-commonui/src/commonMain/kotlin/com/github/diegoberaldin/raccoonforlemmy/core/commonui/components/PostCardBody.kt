package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

@Composable
fun PostCardBody(
    modifier: Modifier = Modifier,
    post: PostModel,
) {
    val body = post.text
    if (body.isNotEmpty()) {
        Markdown(
            modifier = modifier,
            content = body,
        )
    }
}
