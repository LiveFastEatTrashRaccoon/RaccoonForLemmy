package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun UserHeader(
    user: UserModel,
) {
    val avatar = user.avatar.orEmpty()
    if (avatar.isNotEmpty()) {
        val painterResource = asyncPainterResource(data = avatar)
        val profileImageSize = 100.dp
        KamelImage(
            modifier = Modifier.size(profileImageSize)
                .clip(RoundedCornerShape(profileImageSize / 2)),
            resource = painterResource,
            contentDescription = null,
            onLoading = {
                CircularProgressIndicator()
            },
        )
    }
    Text(
        text = user.name,
        style = MaterialTheme.typography.headlineSmall,
    )
    Text(
        text = user.host,
        style = MaterialTheme.typography.titleMedium,
    )
}
