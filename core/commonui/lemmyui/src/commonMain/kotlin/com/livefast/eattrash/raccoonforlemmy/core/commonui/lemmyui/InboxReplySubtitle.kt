package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.formatToReadableValue
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FeedbackButton
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
fun InboxReplySubtitle(
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    creator: UserModel? = null,
    community: CommunityModel? = null,
    iconSize: Dp = IconSize.s,
    date: String? = null,
    score: Int = 0,
    showScores: Boolean = true,
    upVotes: Int = 0,
    downVotes: Int = 0,
    downVoteEnabled: Boolean = true,
    optionsMenuOpen: Boolean = false,
    options: List<Option> = emptyList(),
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    upVoted: Boolean = false,
    downVoted: Boolean = false,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
    onOptionsMenuToggled: ((Boolean) -> Unit)? = null,
    onReply: (() -> Unit)? = null,
) {
    val buttonModifier = Modifier.size(IconSize.l).clearAndSetSemantics { }
    val themeRepository = remember { getThemeRepository() }
    val upVoteColor by themeRepository.upVoteColor.collectAsState()
    val downVoteColor by themeRepository.downVoteColor.collectAsState()
    val defaultUpvoteColor = MaterialTheme.colorScheme.primary
    val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    CustomizedContent(ContentFontClass.AncillaryText) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            val communityName = community?.readableName(preferNicknames).orEmpty()
            val communityIcon = community?.icon.orEmpty()
            val creatorName = creator?.readableName(preferNicknames).orEmpty()
            val creatorAvatar = creator?.avatar.orEmpty()
            if (communityName.isNotEmpty() || creatorName.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(horizontal = Spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    if (creatorName.isNotEmpty()) {
                        Row(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .onClick(
                                        onClick = {
                                            if (creator != null) {
                                                onOpenCreator?.invoke(creator)
                                            }
                                        },
                                    ).clearAndSetSemantics { },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                        ) {
                            if (creatorAvatar.isNotEmpty() && autoLoadImages) {
                                CustomImage(
                                    modifier =
                                        Modifier
                                            .padding(Spacing.xxxs)
                                            .size(iconSize)
                                            .clip(RoundedCornerShape(iconSize / 2)),
                                    url = creatorAvatar,
                                    autoload = autoLoadImages,
                                    quality = FilterQuality.Low,
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                            Text(
                                modifier = Modifier.padding(vertical = Spacing.xs),
                                text = creatorName,
                                style = MaterialTheme.typography.bodySmall,
                                color = ancillaryColor,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                    }
                    if (communityName.isNotEmpty()) {
                        Row(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .onClick(
                                        onClick = {
                                            if (community != null) {
                                                onOpenCommunity?.invoke(community)
                                            }
                                        },
                                    ).clearAndSetSemantics { },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            if (communityIcon.isNotEmpty() && autoLoadImages) {
                                CustomImage(
                                    modifier =
                                        Modifier
                                            .padding(Spacing.xxxs)
                                            .size(iconSize)
                                            .clip(RoundedCornerShape(iconSize / 2)),
                                    url = communityIcon,
                                    autoload = autoLoadImages,
                                    quality = FilterQuality.Low,
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                            CustomizedContent(ContentFontClass.AncillaryText) {
                                Text(
                                    modifier = Modifier.padding(vertical = Spacing.xs),
                                    text = communityName,
                                    style = MaterialTheme.typography.bodySmall,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    color = ancillaryColor,
                                )
                            }
                        }
                    }
                }

                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    ) {
                        IconButton(
                            modifier =
                                buttonModifier
                                    .padding(
                                        top = 3.5.dp,
                                        bottom = 3.5.dp,
                                        end = 3.5.dp,
                                    ),
                            onClick = {
                                onReply?.invoke()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Chat,
                                contentDescription = LocalStrings.current.actionReply,
                                tint = ancillaryColor,
                            )
                        }
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
                                        .onGloballyPositioned {
                                            optionsOffset = it.positionInParent()
                                        },
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
                        FeedbackButton(
                            modifier =
                                buttonModifier
                                    .padding(
                                        top = 2.5.dp,
                                        bottom = 2.5.dp,
                                        end = 2.5.dp,
                                    ).then(VoteAction.UpVote.toModifier()),
                            imageVector = VoteAction.UpVote.toIcon(),
                            tintColor =
                                if (upVoted) {
                                    upVoteColor ?: defaultUpvoteColor
                                } else {
                                    ancillaryColor
                                },
                            contentDescription = LocalStrings.current.actionUpvote,
                            onClick = {
                                onUpVote?.invoke()
                            },
                        )
                        if (showScores) {
                            Text(
                                text =
                                    formatToReadableValue(
                                        voteFormat = voteFormat,
                                        score = score,
                                        upVotes = upVotes,
                                        downVotes = downVotes,
                                        upVoteColor = upVoteColor ?: defaultUpvoteColor,
                                        downVoteColor = downVoteColor ?: defaultDownVoteColor,
                                        upVoted = upVoted,
                                        downVoted = downVoted,
                                    ),
                                style = MaterialTheme.typography.labelMedium,
                                color = ancillaryColor,
                            )
                        }
                        if (downVoteEnabled) {
                            FeedbackButton(
                                modifier =
                                    buttonModifier
                                        .padding(
                                            top = 2.5.dp,
                                            bottom = 2.5.dp,
                                            start = 2.5.dp,
                                        ).then(VoteAction.DownVote.toModifier()),
                                imageVector = VoteAction.DownVote.toIcon(),
                                tintColor =
                                    if (downVoted) {
                                        downVoteColor ?: defaultDownVoteColor
                                    } else {
                                        ancillaryColor
                                    },
                                contentDescription = LocalStrings.current.actionDownvote,
                                onClick = {
                                    onDownVote?.invoke()
                                },
                            )
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
        }
    }
}
