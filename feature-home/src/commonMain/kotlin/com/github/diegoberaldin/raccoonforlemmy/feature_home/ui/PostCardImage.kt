package com.github.diegoberaldin.raccoonforlemmy.feature_home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun PostCardImage(post: PostModel) {
    val imageUrl = post.thumbnailUrl.orEmpty()
    if (imageUrl.isNotEmpty()) {
        val painterResource = asyncPainterResource(data = imageUrl)
        KamelImage(
            modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
            resource = painterResource,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}