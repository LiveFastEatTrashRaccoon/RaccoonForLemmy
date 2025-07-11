package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Update
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.formatToReadableValue
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.readContentAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FeedbackButton
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier

@Composable
fun PostCardFooter(
    modifier: Modifier = Modifier,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    comments: Int? = null,
    publishDate: String? = null,
    updateDate: String? = null,
    score: Int = 0,
    showScores: Boolean = true,
    unreadComments: Int? = null,
    upVotes: Int = 0,
    downVotes: Int = 0,
    saved: Boolean = false,
    upVoted: Boolean = false,
    downVoted: Boolean = false,
    actionButtonsActive: Boolean = true,
    markRead: Boolean = false,
    downVoteEnabled: Boolean = true,
    optionsMenuOpen: Boolean = false,
    options: List<Option> = emptyList(),
    onClick: (() -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onReply: (() -> Unit)? = null,
    onToggleOptionsMenu: ((Boolean) -> Unit)? = null,
    onSelectOption: ((OptionId) -> Unit)? = null,
) {
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val themeRepository = remember { getThemeRepository() }
    val upVoteColor by themeRepository.upVoteColor.collectAsState()
    val downVoteColor by themeRepository.downVoteColor.collectAsState()
    val defaultUpvoteColor = MaterialTheme.colorScheme.primary
    val additionalAlphaFactor = if (markRead) readContentAlpha else 1f
    val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
    val ancillaryColor =
        MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha * additionalAlphaFactor)

    CustomizedContent(ContentFontClass.AncillaryText) {
        Box(
            modifier =
            modifier.onClick(
                indication = null,
                onClick = {
                    onClick?.invoke()
                },
                onLongClick = {
                    if (!optionsMenuOpen) {
                        onToggleOptionsMenu?.invoke(true)
                    }
                },
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                val buttonModifier = Modifier.size(IconSize.l).clearAndSetSemantics { }
                if (comments != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            modifier =
                            buttonModifier
                                .padding(
                                    top = 3.5.dp,
                                    end = 3.5.dp,
                                    bottom = 3.5.dp,
                                ),
                            enabled = actionButtonsActive,
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

                        Text(
                            text = "$comments",
                            style = MaterialTheme.typography.labelMedium,
                            color = ancillaryColor,
                        )
                    }
                }
                if (unreadComments != null) {
                    Text(
                        modifier =
                        Modifier
                            .padding(start = Spacing.xxs)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(CornerSize.s),
                            ).padding(horizontal = Spacing.xxs),
                        text = "+$unreadComments",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
                listOf(
                    updateDate.orEmpty(),
                    publishDate.orEmpty(),
                ).firstOrNull {
                    it.isNotBlank()
                }?.also { publishDate ->
                    Row(
                        modifier = Modifier.padding(start = Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val isShowingUpdateDate = !updateDate.isNullOrBlank()
                        Icon(
                            modifier =
                            Modifier.size(IconSize.m).then(
                                if (!isShowingUpdateDate) {
                                    Modifier.padding(1.5.dp)
                                } else {
                                    Modifier.padding(0.25.dp)
                                },
                            ),
                            imageVector =
                            if (isShowingUpdateDate) {
                                Icons.Default.Update
                            } else {
                                Icons.Default.Schedule
                            },
                            contentDescription = LocalStrings.current.creationDate,
                            tint = ancillaryColor,
                        )
                        Text(
                            modifier = Modifier.padding(start = Spacing.xxs),
                            text = publishDate.prettifyDate(),
                            style = MaterialTheme.typography.labelMedium,
                            color = ancillaryColor,
                        )
                    }
                }
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
                if (actionButtonsActive) {
                    FeedbackButton(
                        modifier =
                        buttonModifier.padding(
                            top = 2.5.dp,
                            bottom = 2.5.dp,
                            end = 2.5.dp,
                        ),
                        imageVector =
                        if (!saved) {
                            Icons.Default.BookmarkBorder
                        } else {
                            Icons.Default.Bookmark
                        },
                        tintColor =
                        if (saved) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            ancillaryColor
                        },
                        enabled = actionButtonsActive,
                        contentDescription = LocalStrings.current.actionAddToBookmarks,
                        onClick = {
                            onSave?.invoke()
                        },
                    )
                }
                FeedbackButton(
                    modifier =
                    buttonModifier
                        .padding(all = 2.5.dp)
                        .then(VoteAction.UpVote.toModifier()),
                    imageVector = VoteAction.UpVote.toIcon(),
                    tintColor =
                    if (upVoted) {
                        upVoteColor ?: defaultUpvoteColor
                    } else {
                        ancillaryColor
                    },
                    enabled = actionButtonsActive,
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
                                end = 2.5.dp,
                            ).then(VoteAction.DownVote.toModifier()),
                        imageVector = VoteAction.DownVote.toIcon(),
                        tintColor =
                        if (downVoted) {
                            downVoteColor ?: defaultDownVoteColor
                        } else {
                            ancillaryColor
                        },
                        enabled = actionButtonsActive,
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
}
