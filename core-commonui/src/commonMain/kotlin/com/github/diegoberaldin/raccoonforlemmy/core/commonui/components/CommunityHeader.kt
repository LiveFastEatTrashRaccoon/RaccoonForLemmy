package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun CommunityHeader(
    community: CommunityModel,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    options: List<String> = emptyList(),
    onOptionSelected: ((Int) -> Unit)? = null,
    onOpenImage: ((String) -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(Spacing.s),
    ) {
        // banner
        val banner = community.banner.orEmpty()
        if (banner.isNotEmpty() && autoLoadImages) {
            CustomImage(
                modifier = Modifier.fillMaxWidth().aspectRatio(4.5f),
                url = banner,
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )
        }

        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (options.isNotEmpty()) {
                var optionsExpanded by remember { mutableStateOf(false) }
                var optionsOffset by remember { mutableStateOf(Offset.Zero) }
                Icon(
                    modifier = Modifier.onGloballyPositioned {
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
                        y = optionsOffset.y.toLocalDp(),
                        // y = (-50).dp,
                    ),
                ) {
                    options.forEachIndexed { idx, option ->
                        Text(
                            modifier = Modifier.padding(
                                horizontal = Spacing.m,
                                vertical = Spacing.xs,
                            ).onClick {
                                optionsExpanded = false
                                onOptionSelected?.invoke(idx)
                            },
                            text = option,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            // avatar
            val communityIcon = community.icon.orEmpty()
            val avatarSize = 60.dp
            if (communityIcon.isNotEmpty() && autoLoadImages) {
                CustomImage(
                    modifier = Modifier
                        .padding(Spacing.xxxs)
                        .size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2))
                        .onClick {
                            onOpenImage?.invoke(communityIcon)
                        },
                    url = communityIcon,
                    quality = FilterQuality.Low,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                PlaceholderImage(
                    size = avatarSize,
                    title = community.name,
                )
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
