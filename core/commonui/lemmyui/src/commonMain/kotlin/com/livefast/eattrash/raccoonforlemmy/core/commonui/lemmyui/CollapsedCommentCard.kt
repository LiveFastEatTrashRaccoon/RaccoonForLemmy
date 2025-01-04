package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

private val BAR_BASE_WIDTH_UNIT = 1.25.dp

@Composable
fun CollapsedCommentCard(
    comment: CommentModel,
    modifier: Modifier = Modifier,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    indentAmount: Int = 2,
    barThickness: Int = 1,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    showScores: Boolean = true,
    actionButtonsActive: Boolean = true,
    isOp: Boolean = false,
    isMod: Boolean = false,
    showBot: Boolean = false,
    options: List<Option> = emptyList(),
    onClick: (() -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onReply: (() -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
    onToggleExpanded: (() -> Unit)? = null,
) {
    val themeRepository = remember { getThemeRepository() }
    val commentBarTheme by themeRepository.commentBarTheme.collectAsState()
    var commentHeight by remember { mutableStateOf(0f) }
    val barColor =
        themeRepository.getCommentBarColor(
            depth = comment.depth,
            commentBarTheme = commentBarTheme,
        )
    val barWidth =
        if (comment.depth > 0) {
            BAR_BASE_WIDTH_UNIT * barThickness
        } else {
            0.dp
        }
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val optionsActionLabel = LocalStrings.current.actionOpenOptionMenu
    val openUserActionLabel =
        buildString {
            append(LocalStrings.current.postReplySourceAccount)
            append(" ")
            append(comment.creator?.name.orEmpty())
        }
    val upVoteActionLabel =
        buildString {
            append(LocalStrings.current.actionUpvote)
            append(": ")
            append(comment.upvotes)
        }
    val downVoteActionLabel =
        buildString {
            append(LocalStrings.current.actionDownvote)
            append(": ")
            append(comment.downvotes)
        }
    val saveActionLabel =
        buildString {
            if (comment.saved) {
                append(LocalStrings.current.actionRemoveFromBookmarks)
            } else {
                append(LocalStrings.current.actionAddToBookmarks)
            }
        }
    val toggleExpandedActionLabel =
        buildString {
            if (comment.expanded) {
                append(LocalStrings.current.actionCollapse)
            } else {
                append(LocalStrings.current.actionExpand)
            }
        }
    val replyActionLabel =
        buildString {
            append(LocalStrings.current.actionReply)
        }

    Row(
        modifier =
            modifier
                .onClick(
                    onClick = onClick ?: {},
                ).semantics(mergeDescendants = true) {
                    val helperActions =
                        buildList {
                            val user = comment.creator
                            if (user != null && onOpenCreator != null) {
                                this +=
                                    CustomAccessibilityAction(openUserActionLabel) {
                                        onOpenCreator(user)
                                        true
                                    }
                            }
                            if (onUpVote != null) {
                                this +=
                                    CustomAccessibilityAction(upVoteActionLabel) {
                                        onUpVote()
                                        true
                                    }
                            }
                            if (onDownVote != null) {
                                this +=
                                    CustomAccessibilityAction(downVoteActionLabel) {
                                        onDownVote()
                                        true
                                    }
                            }
                            if (onSave != null) {
                                this +=
                                    CustomAccessibilityAction(saveActionLabel) {
                                        onSave()
                                        true
                                    }
                            }
                            if (onReply != null) {
                                this +=
                                    CustomAccessibilityAction(replyActionLabel) {
                                        onReply()
                                        true
                                    }
                            }
                            if (onToggleExpanded != null) {
                                this +=
                                    CustomAccessibilityAction(toggleExpandedActionLabel) {
                                        onToggleExpanded()
                                        true
                                    }
                            }
                            if (options.isNotEmpty()) {
                                this +=
                                    CustomAccessibilityAction(
                                        label = optionsActionLabel,
                                        action = {
                                            optionsMenuOpen = true
                                            true
                                        },
                                    )
                            }
                        }
                    if (helperActions.isNotEmpty()) {
                        customActions = helperActions
                    }
                },
    ) {
        Box(
            modifier = Modifier.width((indentAmount * comment.depth).dp),
        )
        if (comment.depth > 0) {
            Box(
                modifier =
                    Modifier
                        .padding(top = Spacing.xxs)
                        .width(barWidth)
                        .height(commentHeight.toLocalDp())
                        .background(color = barColor),
            )
        }
        Column(
            modifier =
                Modifier
                    .padding(start = barWidth)
                    .fillMaxWidth()
                    .padding(
                        vertical = Spacing.xxs,
                        horizontal = Spacing.s,
                    ).onGloballyPositioned {
                        commentHeight = it.size.toSize().height
                    },
        ) {
            CommunityAndCreatorInfo(
                modifier = Modifier.padding(top = Spacing.xxs),
                iconSize = IconSize.s,
                creator = comment.creator,
                indicatorExpanded = comment.expanded,
                distinguished = comment.distinguished,
                isOp = isOp,
                isMod = isMod,
                isBot = comment.creator?.bot?.takeIf { showBot } ?: false,
                autoLoadImages = autoLoadImages,
                preferNicknames = preferNicknames,
                onToggleExpanded = {
                    onToggleExpanded?.invoke()
                },
                onOpenCreator = onOpenCreator,
            )
            PostCardFooter(
                modifier = Modifier.padding(vertical = Spacing.xs),
                score = comment.score,
                showScores = showScores,
                voteFormat = voteFormat,
                upVotes = comment.upvotes,
                downVotes = comment.downvotes,
                saved = comment.saved,
                upVoted = comment.myVote > 0,
                downVoted = comment.myVote < 0,
                comments = comment.comments,
                onClick = onClick,
                onUpVote = onUpVote,
                onDownVote = onDownVote,
                onSave = onSave,
                onReply = onReply,
                publishDate = comment.publishDate,
                updateDate = comment.updateDate,
                actionButtonsActive = actionButtonsActive,
                options = options,
                onOptionSelected = onOptionSelected,
                optionsMenuOpen = optionsMenuOpen,
                onOptionsMenuToggled = {
                    optionsMenuOpen = it
                },
            )
        }
    }
}
