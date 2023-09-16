package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun PostCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    blurred: Boolean = false,
    onImageClick: ((String) -> Unit)? = null,
) {
    if (imageUrl.isNotEmpty()) {
        val painterResource = asyncPainterResource(data = imageUrl)
        KamelImage(
            modifier = modifier.fillMaxWidth()
                .heightIn(min = 200.dp)
                .blur(radius = if (blurred) 60.dp else 0.dp)
                .onClick {
                    onImageClick?.invoke(imageUrl)
                },
            resource = painterResource,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            onFailure = {
                Text(text = "⚠️ Image loading error")
            },
        )
    }
}
