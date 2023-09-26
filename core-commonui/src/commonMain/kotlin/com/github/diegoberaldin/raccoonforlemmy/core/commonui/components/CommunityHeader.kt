package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalDp
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun CommunityHeader(
    community: CommunityModel,
    onOpenCommunityInfo: (() -> Unit)? = null,
    onOpenInstanceInfo: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier.fillMaxWidth().aspectRatio(4.5f).padding(Spacing.xs),
    ) {
        // banner
        val banner = community.banner.orEmpty()
        if (banner.isNotEmpty()) {
            CustomImage(
                modifier = Modifier.fillMaxSize(),
                url = banner,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        var optionsExpanded by remember { mutableStateOf(false) }
        var optionsOffset by remember { mutableStateOf(Offset.Zero) }
        Icon(
            modifier = Modifier.align(Alignment.TopEnd)
                .onGloballyPositioned {
                    optionsOffset = it.positionInParent()
                }.onClick {
                    optionsExpanded = true
                },
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
        )
        CustomDropDown(
            expanded = optionsExpanded,
            onDismiss = {
                optionsExpanded = false
            },
            offset = DpOffset(
                x = optionsOffset.x.toLocalDp(),
                y = (-50).dp,
            ),
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = Spacing.m,
                    vertical = Spacing.xs,
                ).onClick {
                    optionsExpanded = false
                    onOpenCommunityInfo?.invoke()
                },
                text = stringResource(MR.strings.community_detail_info),
            )
            Text(
                modifier = Modifier.padding(
                    horizontal = Spacing.m,
                    vertical = Spacing.xs,
                ).onClick {
                    optionsExpanded = false
                    onOpenInstanceInfo?.invoke()
                },
                text = stringResource(MR.strings.community_detail_instance_info),
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            // avatar
            val communityIcon = community.icon.orEmpty()
            val avatarSize = 60.dp
            if (communityIcon.isNotEmpty()) {
                CustomImage(
                    modifier = Modifier.padding(Spacing.xxxs).size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2)),
                    url = communityIcon,
                    quality = FilterQuality.Low,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                Box(
                    modifier = Modifier.padding(Spacing.xxxs).size(avatarSize).background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(avatarSize / 2),
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = community.name.firstOrNull()?.toString().orEmpty().uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            // textual data
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    modifier = Modifier.padding(horizontal = Spacing.s),
                    text = buildString {
                        append(community.name)
                        if (community.host.isNotEmpty()) {
                            append("@${community.host}")
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
