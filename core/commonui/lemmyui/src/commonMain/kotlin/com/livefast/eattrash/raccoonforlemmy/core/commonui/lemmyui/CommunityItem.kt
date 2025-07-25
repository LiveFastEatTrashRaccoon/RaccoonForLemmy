package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.buildAnnotatedStringWithHighlights
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
fun CommunityItem(
    community: CommunityModel,
    modifier: Modifier = Modifier,
    small: Boolean = false,
    noPadding: Boolean = false,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    showSubscribers: Boolean = false,
    showFavorite: Boolean = false,
    showSubscribeButton: Boolean = false,
    options: List<Option> = emptyList(),
    highlightText: String? = null,
    onSelectOption: ((OptionId) -> Unit)? = null,
    onSubscribe: (() -> Unit)? = null,
) {
    val title = community.readableName(true)
    val communityHandle = community.readableHandle
    val communityIcon = community.icon.orEmpty()
    val iconSize = if (small) IconSize.m else IconSize.l
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    val highlightColor = Color(255, 194, 10, 150)
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    var optionsMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier =
        modifier.then(
            if (noPadding) {
                Modifier
            } else {
                Modifier.padding(Spacing.s)
            },
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        if (communityIcon.isNotEmpty() && autoLoadImages) {
            CustomImage(
                modifier =
                Modifier
                    .padding(Spacing.xxxs)
                    .size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 2)),
                url = communityIcon,
                autoload = autoLoadImages,
                contentScale = ContentScale.FillBounds,
            )
        } else {
            PlaceholderImage(
                size = iconSize,
                title = community.readableName(preferNicknames),
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = Spacing.xs),
        ) {
            Text(
                text =
                buildAnnotatedStringWithHighlights(
                    text = title,
                    highlightText = highlightText,
                    highlightColor = highlightColor,
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = fullColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text =
                buildAnnotatedStringWithHighlights(
                    text =
                    buildString {
                        append("!")
                        append(communityHandle)
                    },
                    highlightText = highlightText,
                    highlightColor = highlightColor,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = ancillaryColor,
            )
        }
        when {
            showSubscribers ->
                Row(
                    modifier = Modifier.padding(start = Spacing.xxs),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = community.subscribers.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ancillaryColor,
                    )
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "",
                        tint = ancillaryColor,
                    )
                }

            showFavorite -> {
                if (community.favorite) {
                    Icon(
                        modifier = Modifier.size(IconSize.s),
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        tint = ancillaryColor,
                    )
                }
            }

            showSubscribeButton -> {
                IconButton(
                    modifier = Modifier.size(IconSize.m),
                    onClick = {
                        onSubscribe?.invoke()
                    },
                ) {
                    Icon(
                        imageVector =
                        when (community.subscribed) {
                            true -> {
                                Icons.Outlined.RemoveCircleOutline
                            }

                            false -> {
                                Icons.Outlined.AddCircleOutline
                            }

                            else -> {
                                Icons.Outlined.Pending
                            }
                        },
                        contentDescription = "",
                        tint = ancillaryColor,
                    )
                }
            }
        }

        if (options.isNotEmpty()) {
            Box {
                IconButton(
                    modifier =
                    Modifier
                        .size(IconSize.m)
                        .padding(Spacing.xs)
                        .onGloballyPositioned {
                            optionsOffset = it.positionInParent()
                        },
                    onClick = {
                        optionsMenuOpen = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = LocalStrings.current.actionOpenOptionMenu,
                        tint = ancillaryColor,
                    )
                }

                CustomDropDown(
                    expanded = optionsMenuOpen,
                    onDismiss = {
                        optionsMenuOpen = false
                    },
                    offset =
                    DpOffset(
                        x = optionsOffset.x.toLocalDp(),
                        y = optionsOffset.y.toLocalDp(),
                    ),
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.text)
                            },
                            onClick = {
                                optionsMenuOpen = false
                                onSelectOption?.invoke(option.id)
                            },
                        )
                    }
                }
            }
        }
    }
}
