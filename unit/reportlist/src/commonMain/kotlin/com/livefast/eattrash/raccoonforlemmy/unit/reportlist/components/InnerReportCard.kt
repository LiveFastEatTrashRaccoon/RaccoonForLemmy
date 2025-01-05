package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
internal fun InnerReportCard(
    modifier: Modifier = Modifier,
    reason: String,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    date: String? = null,
    creator: UserModel? = null,
    postLayout: PostLayout = PostLayout.Card,
    options: List<Option> = emptyList(),
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    originalContent: (@Composable () -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val optionsActionLabel = LocalStrings.current.actionOpenOptionMenu
    val openUserActionLabel =
        buildString {
            append(LocalStrings.current.postReplySourceAccount)
            append(" ")
            append(creator?.name.orEmpty())
        }
    val openResolveActionLabel =
        buildString {
            append(LocalStrings.current.actionOpen)
        }

    Box(
        modifier =
            modifier.then(
                if (postLayout == PostLayout.Card) {
                    Modifier
                        .padding(horizontal = Spacing.xs)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(CornerSize.l),
                        ).clip(RoundedCornerShape(CornerSize.l))
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                            shape = RoundedCornerShape(CornerSize.l),
                        ).padding(vertical = Spacing.xs)
                } else {
                    Modifier.background(MaterialTheme.colorScheme.background)
                },
            ).semantics {
                val helperActions =
                    buildList {
                        if (creator != null && onOpenCreator != null) {
                            this +=
                                CustomAccessibilityAction(openUserActionLabel) {
                                    onOpenCreator(creator)
                                    true
                                }
                        }
                        if (onOpen != null) {
                            this +=
                                CustomAccessibilityAction(openResolveActionLabel) {
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
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            ReportHeader(
                modifier = Modifier.padding(horizontal = Spacing.s),
                creator = creator,
                autoLoadImages = autoLoadImages,
                onOpenCreator = onOpenCreator,
                preferNicknames = preferNicknames,
            )
            CustomizedContent(ContentFontClass.Body) {
                PostCardBody(
                    modifier = Modifier.padding(horizontal = Spacing.s),
                    text = reason,
                )
                if (originalContent != null) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(CornerSize.l),
                                ).padding(all = Spacing.s),
                    ) {
                        originalContent()
                    }
                }
            }
            ReportFooter(
                modifier =
                    Modifier.padding(
                        vertical = Spacing.xs,
                        horizontal = Spacing.s,
                    ),
                date = date,
                onOpenResolve = onOpen,
                options = options,
                optionsMenuOpen = optionsMenuOpen,
                onOptionsMenuToggled = {
                    optionsMenuOpen = it
                },
                onOptionSelected = onOptionSelected,
            )
        }
    }
}

@Composable
private fun ReportHeader(
    modifier: Modifier = Modifier,
    creator: UserModel? = null,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    iconSize: Dp = IconSize.s,
    onOpenCreator: ((UserModel) -> Unit)? = null,
) {
    val creatorName = creator?.readableName(preferNicknames).orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    if (creatorName.isNotEmpty()) {
        Row(
            modifier =
                modifier
                    .onClick(
                        onClick = {
                            if (creator != null) {
                                onOpenCreator?.invoke(creator)
                            }
                        },
                    ).clearAndSetSemantics { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            if (creatorAvatar.isNotEmpty() && autoLoadImages) {
                CustomImage(
                    modifier =
                        Modifier
                            .padding(Spacing.xxxs)
                            .size(iconSize)
                            .clip(RoundedCornerShape(iconSize / 2)),
                    url = creatorAvatar,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                    autoload = autoLoadImages,
                )
            }
            Text(
                modifier = Modifier.padding(vertical = Spacing.xs),
                text = creatorName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun ReportFooter(
    modifier: Modifier = Modifier,
    date: String? = null,
    optionsMenuOpen: Boolean = false,
    options: List<Option> = emptyList(),
    onOpenResolve: (() -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
    onOptionsMenuToggled: ((Boolean) -> Unit)? = null,
) {
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    Box(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(IconSize.m).padding(1.5.dp),
                    imageVector = Icons.Default.Schedule,
                    contentDescription = LocalStrings.current.creationDate,
                    tint = ancillaryColor,
                )
                Text(
                    modifier = Modifier.padding(start = Spacing.xxs),
                    text = date?.prettifyDate() ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = ancillaryColor,
                )
            }
            if (options.isNotEmpty()) {
                IconButton(
                    modifier =
                        Modifier
                            .size(IconSize.m)
                            .padding(Spacing.xs)
                            .padding(top = Spacing.xxs)
                            .onGloballyPositioned {
                                optionsOffset = it.positionInParent()
                            }.clearAndSetSemantics { },
                    onClick = {
                        onOptionsMenuToggled?.invoke(true)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = LocalStrings.current.actionOpenOptionMenu,
                        tint = ancillaryColor,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (onOpenResolve != null) {
                IconButton(
                    modifier = Modifier.clearAndSetSemantics { },
                    onClick = {
                        onOpenResolve.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.OpenInNew,
                        contentDescription = LocalStrings.current.actionOpen,
                    )
                }
            }
        }
        CustomDropDown(
            expanded = optionsMenuOpen,
            onDismiss = {
                onOptionsMenuToggled?.invoke(false)
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
                        onOptionsMenuToggled?.invoke(false)
                        onOptionSelected?.invoke(option.id)
                    },
                )
            }
        }
    }
}
