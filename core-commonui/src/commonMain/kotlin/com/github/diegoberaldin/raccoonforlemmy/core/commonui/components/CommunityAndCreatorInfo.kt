package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Arrangement
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
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

@Composable
fun CommunityAndCreatorInfo(
    modifier: Modifier = Modifier,
    community: CommunityModel? = null,
    creator: UserModel? = null,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
) {
    val communityName = community?.name.orEmpty()
    val communityIcon = community?.icon.orEmpty()
    val communityHost = community?.host.orEmpty()
    val creatorName = creator?.name.orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    val creatorHost = creator?.host.orEmpty()
    val iconSize = 32.dp

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        if (communityIcon.isNotEmpty()) {
            CustomImage(
                modifier = Modifier
                    .padding(Spacing.xxxs)
                    .size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 2)),
                url = communityIcon,
                quality = FilterQuality.Low,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        } else if (creatorAvatar.isNotEmpty()) {
            CustomImage(
                modifier = Modifier
                    .padding(Spacing.xxxs)
                    .size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 2))
                    .onClick {
                        if (community != null) {
                            onOpenCommunity?.invoke(community)
                        }
                    },
                url = creatorAvatar,
                quality = FilterQuality.Low,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        Column(
            modifier = Modifier.padding(vertical = Spacing.xxxs),
        ) {
            if (community != null) {
                Text(
                    modifier = Modifier
                        .onClick {
                            onOpenCommunity?.invoke(community)
                        },
                    text = buildString {
                        append(communityName)
                        if (communityHost.isNotEmpty()) {
                            append("@$communityHost")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (creator != null) {
                Text(
                    modifier = Modifier
                        .onClick {
                            onOpenCreator?.invoke(creator)
                        },
                    text = buildString {
                        append(creatorName)
                        if (creatorHost.isNotEmpty()) {
                            append("@$creatorHost")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
