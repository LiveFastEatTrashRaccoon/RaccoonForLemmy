package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun PostCardSubtitle(
    community: CommunityModel? = null,
    creator: UserModel? = null,
    modifier: Modifier = Modifier,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
) {
    val communityName = community?.name.orEmpty()
    val communityIcon = community?.icon.orEmpty()
    val communityHost = community?.host.orEmpty()
    val creatorName = creator?.name.orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    val creatorHost = creator?.host.orEmpty()
    val iconSize = 16.dp
    if (communityName.isNotEmpty() || creatorName.isNotEmpty()) {
        Row(
            modifier = modifier.padding(vertical = Spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            if (communityName.isNotEmpty()) {
                Row(
                    modifier = Modifier.onClick {
                        if (community != null) {
                            onOpenCommunity?.invoke(community)
                        }
                    },
                ) {
                    if (communityIcon.isNotEmpty()) {
                        val painterResource = asyncPainterResource(data = communityIcon)
                        KamelImage(
                            modifier = Modifier
                                .padding(Spacing.xxxs)
                                .size(iconSize)
                                .clip(RoundedCornerShape(iconSize / 2)),
                            resource = painterResource,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                    Text(
                        text = buildString {
                            append(communityName)
                            if (communityHost.isNotEmpty()) {
                                append("@$communityHost")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            if (creatorName.isNotEmpty()) {
                if (communityName.isNotEmpty()) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                if (creatorAvatar.isNotEmpty()) {
                    val painterResource = asyncPainterResource(data = creatorAvatar)
                    KamelImage(
                        modifier = Modifier
                            .padding(Spacing.xxxs)
                            .size(iconSize)
                            .clip(RoundedCornerShape(iconSize / 2)),
                        resource = painterResource,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }
                Text(
                    text = buildString {
                        append(creatorName)
                        if (creatorHost.isNotEmpty() && communityHost != creatorHost) {
                            append("@$creatorHost")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
