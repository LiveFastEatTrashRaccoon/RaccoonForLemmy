package com.github.diegoberaldin.raccoonforlemmy.core_commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core_md.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel

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
