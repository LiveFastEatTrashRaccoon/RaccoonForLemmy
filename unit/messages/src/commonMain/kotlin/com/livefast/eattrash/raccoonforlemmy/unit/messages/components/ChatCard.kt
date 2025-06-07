package com.livefast.eattrash.raccoonforlemmy.unit.messages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.ellipsize
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
internal fun ChatCard(
    user: UserModel?,
    lastMessage: String,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    lastMessageDate: String? = null,
    options: List<Option> = emptyList(),
    onOpenUser: ((UserModel) -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    onSelectOption: ((OptionId) -> Unit)? = null,
) {
    var optionsMenuOpen by remember { mutableStateOf(false) }
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val creatorName = user?.readableName(preferNicknames).orEmpty()
    val creatorAvatar = user?.avatar.orEmpty()
    val iconSize = IconSize.xl
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    val optionsActionLabel = LocalStrings.current.actionOpenOptionMenu
    val openUserActionLabel =
        buildString {
            append(LocalStrings.current.postReplySourceAccount)
            append(" ")
            append(user?.name.orEmpty())
        }
    val openActionLabel =
        buildString {
            append(LocalStrings.current.actionOpen)
        }

    Row(
        modifier =
        modifier
            .padding(horizontal = Spacing.xs)
            .onClick(
                onClick = {
                    onOpen?.invoke()
                },
            ).semantics {
                val helperActions =
                    buildList {
                        if (user != null && onOpenUser != null) {
                            this +=
                                CustomAccessibilityAction(openUserActionLabel) {
                                    onOpenUser(user)
                                    true
                                }
                        }
                        if (onOpen != null) {
                            this +=
                                CustomAccessibilityAction(openActionLabel) {
                                    onOpen()
                                    true
                                }
                        }
                        if (options.isNotEmpty()) {
                            this +=
                                CustomAccessibilityAction(optionsActionLabel) {
                                    optionsMenuOpen = true
                                    true
                                }
                        }
                    }
                if (helperActions.isNotEmpty()) {
                    customActions = helperActions
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
    ) {
        if (creatorAvatar.isNotEmpty()) {
            CustomImage(
                modifier =
                Modifier
                    .padding(Spacing.xxxs)
                    .size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 2))
                    .onClick(
                        onClick = {
                            if (user != null) {
                                onOpenUser?.invoke(user)
                            }
                        },
                    ).clearAndSetSemantics { },
                quality = FilterQuality.Low,
                url = creatorAvatar,
                autoload = autoLoadImages,
                contentScale = ContentScale.FillBounds,
            )
        } else {
            PlaceholderImage(
                modifier = Modifier.clearAndSetSemantics { },
                onClick = {
                    if (user != null) {
                        onOpenUser?.invoke(user)
                    }
                },
                size = iconSize,
                title = creatorName,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Row(
                modifier = Modifier.padding(end = Spacing.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // user name
                Text(
                    modifier = Modifier.weight(1f),
                    text = creatorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = ancillaryColor,
                )
                // last message date
                if (lastMessageDate != null) {
                    Text(
                        modifier = Modifier.padding(start = Spacing.xxs),
                        text = lastMessageDate.prettifyDate(),
                        style = MaterialTheme.typography.labelSmall,
                        color = ancillaryColor,
                    )
                }
            }
            CustomizedContent(ContentFontClass.Body) {
                // last message text
                PostCardBody(
                    text = lastMessage.ellipsize(90),
                    autoLoadImages = autoLoadImages,
                    onClick = {
                        onOpen?.invoke()
                    },
                )
            }
            Box {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.size(IconSize.m))
                        Spacer(modifier = Modifier.weight(1f))
                        if (options.isNotEmpty()) {
                            IconButton(
                                modifier =
                                Modifier
                                    .size(IconSize.m)
                                    .padding(Spacing.xs)
                                    .onGloballyPositioned {
                                        optionsOffset = it.positionInParent()
                                    }.clearAndSetSemantics { },
                                onClick = {
                                    optionsMenuOpen = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = LocalStrings.current.actionOpenOptionMenu,
                                    tint = ancillaryColor,
                                )
                            }
                        }
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
}
