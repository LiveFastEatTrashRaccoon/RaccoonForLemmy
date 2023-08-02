package com.github.diegoberaldin.raccoonforlemmy.core_commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel

@Composable
internal fun PostCardTitle(post: PostModel) {
    Text(
        text = post.title,
        style = MaterialTheme.typography.titleMedium,
    )
}