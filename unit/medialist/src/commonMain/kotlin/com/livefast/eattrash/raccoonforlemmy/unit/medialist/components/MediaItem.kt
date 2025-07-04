package com.livefast.eattrash.raccoonforlemmy.unit.medialist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Download
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardVideo
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAVideo
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getUrl

@Composable
internal fun MediaItem(
    media: MediaModel,
    instance: String,
    modifier: Modifier = Modifier,
    postLayout: PostLayout = PostLayout.Card,
    autoloadImages: Boolean = true,
    fullHeightImage: Boolean = true,
    fullWidthImage: Boolean = false,
    options: List<Option> = emptyList(),
    onSelectOption: ((OptionId) -> Unit)? = null,
    onOpenFullScreen: ((String) -> Unit)? = null,
) {
    val url = media.getUrl(instance)
    val backgroundColor =
        when (postLayout) {
            PostLayout.Card -> MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
            else -> MaterialTheme.colorScheme.background
        }
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val optionsActionLabel = LocalStrings.current.actionOpenOptionMenu

    Box(
        modifier =
        modifier
            .then(
                if (postLayout == PostLayout.Card) {
                    Modifier
                        .padding(horizontal = Spacing.xs)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(CornerSize.l),
                        ).clip(RoundedCornerShape(CornerSize.l))
                        .background(backgroundColor)
                        .padding(vertical = Spacing.s)
                } else {
                    Modifier
                },
            ).semantics(mergeDescendants = true) {
                val helperActions =
                    buildList {
                        if (options.isNotEmpty()) {
                            this +=
                                CustomAccessibilityAction(optionsActionLabel) {
                                    optionsMenuOpen = true
                                    true
                                }
                        }
                    }
                customActions = helperActions
            },
    ) {
        Column(
            modifier =
            Modifier
                .background(backgroundColor)
                .padding(top = Spacing.s),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            if (media.alias.looksLikeAVideo) {
                PostCardVideo(
                    modifier =
                    Modifier
                        .padding(
                            vertical = Spacing.xxs,
                            horizontal = if (fullWidthImage) 0.dp else Spacing.s,
                        ),
                    url = url,
                    onOpenFullScreen = {
                        onOpenFullScreen?.invoke(url)
                    },
                )
            } else {
                PostCardImage(
                    modifier =
                    Modifier
                        .padding(
                            vertical = Spacing.xs,
                            horizontal = if (fullWidthImage) 0.dp else Spacing.s,
                        ).then(
                            if (postLayout == PostLayout.Card && !fullWidthImage) {
                                Modifier.clip(RoundedCornerShape(CornerSize.xl))
                            } else {
                                Modifier
                            },
                        ),
                    imageUrl = url,
                    autoLoadImages = autoloadImages,
                    contentScale = if (fullHeightImage) ContentScale.FillWidth else ContentScale.Crop,
                    maxHeight = if (fullHeightImage) Dp.Unspecified else EXTENDED_POST_MAX_HEIGHT,
                    loadButtonContent = @Composable {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = LocalStrings.current.actionDownload,
                        )
                    },
                )
            }

            MediaFooter(
                modifier =
                Modifier.padding(
                    vertical = Spacing.xs,
                    horizontal = Spacing.s,
                ),
                date = media.date,
                options = options,
                optionsMenuOpen = optionsMenuOpen,
                onToggleOptionsMenu = {
                    optionsMenuOpen = it
                },
                onSelectOption = onSelectOption,
            )
        }
    }
}

@Composable
private fun MediaFooter(
    modifier: Modifier = Modifier,
    date: String? = null,
    optionsMenuOpen: Boolean = false,
    options: List<Option> = emptyList(),
    onOpen: (() -> Unit)? = null,
    onSelectOption: ((OptionId) -> Unit)? = null,
    onToggleOptionsMenu: ((Boolean) -> Unit)? = null,
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
                    style = MaterialTheme.typography.labelMedium,
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
                        onToggleOptionsMenu?.invoke(true)
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
            if (onOpen != null) {
                IconButton(
                    modifier = Modifier.clearAndSetSemantics { },
                    onClick = {
                        onOpen.invoke()
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
                onToggleOptionsMenu?.invoke(false)
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
                        onToggleOptionsMenu?.invoke(false)
                        onSelectOption?.invoke(option.id)
                    },
                )
            }
        }
    }
}

private val EXTENDED_POST_MAX_HEIGHT = 200.dp
