package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PostCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    minHeight: Dp = 200.dp,
    maxHeight: Dp = Dp.Unspecified,
    blurred: Boolean = false,
    onImageClick: ((String) -> Unit)? = null,
) {
    if (imageUrl.isNotEmpty()) {
        CustomImage(
            modifier = modifier.fillMaxWidth()
                .heightIn(min = minHeight, max = maxHeight)
                .blur(radius = if (blurred) 60.dp else 0.dp)
                .onClick {
                    onImageClick?.invoke(imageUrl)
                },
            url = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
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
}
