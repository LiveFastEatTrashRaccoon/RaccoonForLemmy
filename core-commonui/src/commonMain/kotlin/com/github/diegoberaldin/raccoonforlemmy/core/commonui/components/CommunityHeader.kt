package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.racconforlemmy.core.utils.toLocalDp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun CommunityHeader(
    community: CommunityModel,
    isOnOtherInstance: Boolean = false,
    onOpenCommunityInfo: (() -> Unit)? = null,
    onOpenInstanceInfo: (() -> Unit)? = null,
    onSubscribeButtonClicked: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier.fillMaxWidth().aspectRatio(4.5f).padding(Spacing.xs),
    ) {
        // banner
        val banner = community.banner.orEmpty()
        if (banner.isNotEmpty()) {
            val painterResource = asyncPainterResource(banner)
            KamelImage(
                modifier = Modifier.fillMaxSize(),
                resource = painterResource,
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
                val painterResource = asyncPainterResource(data = communityIcon)
                KamelImage(
                    modifier = Modifier.padding(Spacing.xxxs).size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2)),
                    resource = painterResource,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // subscribe button
                if (!isOnOtherInstance) {
                    Button(
                        contentPadding = PaddingValues(horizontal = Spacing.m),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onSubscribeButtonClicked?.invoke()
                        },
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                imageVector = when (community.subscribed) {
                                    true -> Icons.Default.Check
                                    false -> Icons.Default.AddCircle
                                    else -> Icons.Default.MoreHoriz
                                },
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary),
                            )
                            Text(
                                text = when (community.subscribed) {
                                    true -> stringResource(MR.strings.community_button_subscribed)
                                    false -> stringResource(MR.strings.community_button_subscribe)
                                    else -> stringResource(MR.strings.community_button_pending)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
