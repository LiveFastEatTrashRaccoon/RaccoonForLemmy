package com.github.diegoberaldin.raccoonforlemmy.feature_home.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

@Composable
internal fun PostCardTitle(post: PostModel) {
    Text(
        text = post.title,
        style = MaterialTheme.typography.titleMedium,
    )
}