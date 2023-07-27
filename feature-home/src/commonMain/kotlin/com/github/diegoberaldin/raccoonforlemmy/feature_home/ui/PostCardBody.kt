package com.github.diegoberaldin.raccoonforlemmy.feature_home.ui

import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.core_md.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

@Composable
internal fun PostCardBody(post: PostModel) {
    val body = post.text
    if (body.isNotEmpty()) {
        Markdown(content = body)
    }
}