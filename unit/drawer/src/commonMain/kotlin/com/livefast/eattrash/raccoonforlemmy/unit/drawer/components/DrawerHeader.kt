package com.livefast.eattrash.raccoonforlemmy.unit.drawer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Composable
internal fun DrawerHeader(
    modifier: Modifier = Modifier,
    user: UserModel? = null,
    instance: String? = null,
    autoLoadImages: Boolean = true,
    onOpenChangeInstance: (() -> Unit)? = null,
    onOpenSwitchAccount: (() -> Unit)? = null,
) {
    val avatarSize = 52.dp
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    Row(
        modifier =
        modifier.padding(
            top = Spacing.m,
            start = Spacing.s,
            end = Spacing.s,
            bottom = Spacing.s,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        if (user != null) {
            // avatar
            val userAvatar = user.avatar.orEmpty()
            if (userAvatar.isNotEmpty()) {
                CustomImage(
                    modifier =
                    Modifier
                        .padding(Spacing.xxxs)
                        .size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2)),
                    url = userAvatar,
                    autoload = autoLoadImages,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                PlaceholderImage(
                    size = avatarSize,
                    title = user.name,
                )
            }

            Row {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    Text(
                        text =
                        buildString {
                            if (user.displayName.isNotEmpty()) {
                                append(user.displayName)
                            } else {
                                append(user.name)
                            }
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = fullColor,
                    )
                    Text(
                        text =
                        buildString {
                            append(user.name)
                            append("@")
                            append(user.host)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        color = ancillaryColor,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onOpenSwitchAccount?.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = LocalStrings.current.actionSwitchAccount,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        } else {
            val anonymousTitle = LocalStrings.current.navigationDrawerAnonymous
            PlaceholderImage(
                size = avatarSize,
                title = anonymousTitle,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                Text(
                    text = anonymousTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = fullColor,
                )
                Row {
                    Text(
                        text = instance.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        color = ancillaryColor,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            onOpenChangeInstance?.invoke()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = LocalStrings.current.actionSwitchInstance,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }
    }
}
