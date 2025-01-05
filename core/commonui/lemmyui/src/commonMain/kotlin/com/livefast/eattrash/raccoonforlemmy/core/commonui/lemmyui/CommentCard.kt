package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar.getCustomTextToolbar
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

private val BAR_BASE_WIDTH_UNIT = 1.25.dp
private const val COMMENT_TEXT_SCALE_FACTOR = 0.97f

@Composable
fun CommentCard(
    comment: CommentModel,
    modifier: Modifier = Modifier,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    hideAuthor: Boolean = false,
    hideCommunity: Boolean = true,
    indentAmount: Int = 2,
    barThickness: Int = 1,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    showScores: Boolean = true,
    showExpandedIndicator: Boolean = true,
    actionButtonsActive: Boolean = true,
    isOp: Boolean = false,
    isMod: Boolean = false,
    isAdmin: Boolean = false,
    isCurrentUser: Boolean = false,
    showBot: Boolean = false,
    adminTagColor: Int? = null,
    botTagColor: Int? = null,
    meTagColor: Int? = null,
    opTagColor: Int? = null,
    modTagColor: Int? = null,
    downVoteEnabled: Boolean = true,
    highlightText: String? = null,
    options: List<Option> = emptyList(),
    onClick: (() -> Unit)? = null,
    onImageClick: ((String) -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onReply: (() -> Unit)? = null,
    onOpenCommunity: ((CommunityModel, String) -> Unit)? = null,
    onOpenCreator: ((UserModel, String) -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
    onToggleExpanded: (() -> Unit)? = null,
) {
    val themeRepository = remember { getThemeRepository() }
    var commentHeight by remember { mutableStateOf(0f) }
    val commentBarTheme by themeRepository.commentBarTheme.collectAsState()
    var textSelection by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
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
    val shareHelper = remember { getShareHelper() }
    val clipboardManager = LocalClipboardManager.current
    val onShareLambda = {
        val query = clipboardManager.getText()?.text.orEmpty()
        shareHelper.share(query)
    }
    val shareActionLabel = LocalStrings.current.postActionShare
    val cancelActionLabel = LocalStrings.current.buttonCancel
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val optionsActionLabel = LocalStrings.current.actionOpenOptionMenu
    val openCommunityActionLabel =
        buildString {
            append(LocalStrings.current.exploreResultTypeCommunities)
            append(": ")
            append(comment.community?.name.orEmpty())
        }
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

    CompositionLocalProvider(
        LocalTextToolbar provides
            getCustomTextToolbar(
                shareActionLabel = shareActionLabel,
                cancelActionLabel = cancelActionLabel,
                onShare = onShareLambda,
                onCancel = {
                    focusManager.clearFocus(true)
                },
            ),
    ) {
        Row(
            modifier =
                modifier.semantics(mergeDescendants = true) {
                    val helperActions =
                        buildList {
                            val community = comment.community
                            if (community != null && onOpenCommunity != null) {
                                this +=
                                    CustomAccessibilityAction(openCommunityActionLabel) {
                                        onOpenCommunity(community, "")
                                        true
                                    }
                            }
                            val user = comment.creator
                            if (user != null && onOpenCreator != null) {
                                this +=
                                    CustomAccessibilityAction(openUserActionLabel) {
                                        onOpenCreator(user, "")
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
            if (indentAmount > 0 && comment.depth > 0) {
                Box(
                    modifier =
                        Modifier
                            .padding(top = Spacing.xxs)
                            .width(barWidth)
                            .height(commentHeight.toLocalDp())
                            .background(color = barColor, shape = RoundedCornerShape(indentAmount / 2)),
                )
            }
            Box(
                modifier =
                    Modifier
                        .onClick(
                            indication = null,
                            onClick = {
                                if (textSelection) {
                                    focusManager.clearFocus()
                                    textSelection = false
                                } else {
                                    onClick?.invoke()
                                }
                            },
                            onDoubleClick = onDoubleClick ?: {},
                        ),
            ) {
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
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    CommunityAndCreatorInfo(
                        modifier = Modifier.padding(top = Spacing.xxs),
                        iconSize = IconSize.s,
                        autoLoadImages = autoLoadImages,
                        preferNicknames = preferNicknames,
                        creator = comment.creator.takeIf { !hideAuthor },
                        community = comment.community.takeIf { !hideCommunity },
                        indicatorExpanded = comment.expanded.takeIf { showExpandedIndicator },
                        distinguished = comment.distinguished,
                        isOp = isOp,
                        isMod = isMod,
                        isAdmin = isAdmin,
                        isBot = comment.creator?.bot.takeIf { showBot } ?: false,
                        isCurrentUser = isCurrentUser,
                        adminTagColor = adminTagColor,
                        botTagColor = botTagColor,
                        meTagColor = meTagColor,
                        opTagColor = opTagColor,
                        modTagColor = modTagColor,
                        onOpenCreator = { user ->
                            onOpenCreator?.invoke(user, "")
                        },
                        onOpenCommunity = { community ->
                            onOpenCommunity?.invoke(community, "")
                        },
                        onToggleExpanded = onToggleExpanded,
                    )
                    if (comment.removed) {
                        Text(
                            text = LocalStrings.current.messageContentRemoved,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                        )
                    } else if (comment.deleted) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = LocalStrings.current.messageContentDeleted,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                        )
                    } else {
                        CustomizedContent(ContentFontClass.Comment) {
                            CompositionLocalProvider(
                                LocalDensity provides
                                    Density(
                                        density = LocalDensity.current.density,
                                        // additional downscale for font in comments
                                        fontScale = LocalDensity.current.fontScale * COMMENT_TEXT_SCALE_FACTOR,
                                    ),
                            ) {
                                PostCardBody(
                                    text = comment.text.orEmpty(),
                                    autoLoadImages = autoLoadImages,
                                    onClick = {
                                        if (textSelection) {
                                            focusManager.clearFocus(true)
                                            textSelection = false
                                        } else {
                                            onClick?.invoke()
                                        }
                                    },
                                    highlightText = highlightText,
                                    onOpenImage = onImageClick,
                                    onDoubleClick = onDoubleClick,
                                    onLongClick = {
                                        textSelection = true
                                    },
                                )
                            }
                        }
                    }
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
                        actionButtonsActive = actionButtonsActive,
                        downVoteEnabled = downVoteEnabled,
                        onClick = onClick,
                        onUpVote = onUpVote,
                        onDownVote = onDownVote,
                        onSave = onSave,
                        onReply = onReply,
                        publishDate = comment.publishDate,
                        updateDate = comment.updateDate,
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
    }
}
