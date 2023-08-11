package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun UserHeader(
    user: UserModel,
) {
    val avatar = user.avatar.orEmpty()
    val profileImageSize = 100.dp
    if (avatar.isNotEmpty()) {
        val painterResource = asyncPainterResource(data = avatar)
        KamelImage(
            modifier = Modifier.size(profileImageSize)
                .clip(RoundedCornerShape(profileImageSize / 2)),
            resource = painterResource,
            contentDescription = null,
        )
    } else {
        Box(
            modifier = Modifier.padding(Spacing.xxxs).size(profileImageSize)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(profileImageSize / 2),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = user.name.firstOrNull()?.toString().orEmpty().uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
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
