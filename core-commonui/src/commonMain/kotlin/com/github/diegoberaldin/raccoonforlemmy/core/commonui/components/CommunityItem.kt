package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun CommunityItem(
    community: CommunityModel,
    modifier: Modifier = Modifier,
) {
    val title = community.title
    val communityName = community.name
    val communityIcon = community.icon.orEmpty()
    val communityHost = community.host
    val iconSize = 30.dp
    Row(
        modifier = modifier.padding(
            vertical = Spacing.xs,
            horizontal = Spacing.s,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        if (communityIcon.isNotEmpty()) {
            val painterResource = asyncPainterResource(
                data = communityIcon,
                filterQuality = FilterQuality.Medium,
            )
            KamelImage(
                modifier = Modifier.padding(Spacing.xxxs).size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 2)),
                resource = painterResource,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        } else {
            Box(
                modifier = Modifier.padding(Spacing.xxxs).size(iconSize)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(iconSize / 2),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = community.name.firstOrNull()?.toString().orEmpty().uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Column {
            Text(
                text = buildString {
                    append(title)
                },
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = buildString {
                    append("!")
                    append(communityName)
                    if (communityHost.isNotEmpty()) {
                        append("@$communityHost")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
