package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar.getCustomTextToolbar
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

sealed interface InboxCardType {
    data object Mention : InboxCardType

    data object Reply : InboxCardType
}

@Composable
fun InboxCard(
    mention: PersonMentionModel,
    type: InboxCardType,
    onImageClick: (String) -> Unit,
    onClick: (PostModel) -> Unit,
    onOpenCreator: (UserModel, String) -> Unit,
    onOpenCommunity: (CommunityModel) -> Unit,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    showScores: Boolean = true,
    downVoteEnabled: Boolean = true,
    previewMaxLines: Int? = 1,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    postLayout: PostLayout = PostLayout.Card,
    options: List<Option> = emptyList(),
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onSelectOption: ((OptionId) -> Unit)? = null,
    onReply: (() -> Unit)? = null,
) {
    var textSelection by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val onClickPost = {
        onClick.invoke(mention.post)
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
            append(mention.community.name)
        }
    val openUserActionLabel =
        buildString {
            append(LocalStrings.current.postReplySourceAccount)
            append(" ")
            append(mention.creator.name)
        }
    val upVoteActionLabel =
        buildString {
            append(LocalStrings.current.actionUpvote)
            append(": ")
            append(mention.upvotes)
        }
    val downVoteActionLabel =
        buildString {
            append(LocalStrings.current.actionDownvote)
            append(": ")
            append(mention.downvotes)
        }
    val replyActionLabel =
        buildString {
            append(LocalStrings.current.actionReply)
        }

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
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                        ).padding(vertical = Spacing.s)
                } else {
                    Modifier.background(MaterialTheme.colorScheme.background)
                },
            ).onClick(
                onClick = {
                    if (textSelection) {
                        focusManager.clearFocus()
                        textSelection = false
                    } else {
                        onClickPost()
                    }
                },
            ).semantics(mergeDescendants = true) {
                val helperActions =
                    buildList {
                        this +=
                            CustomAccessibilityAction(openCommunityActionLabel) {
                                onOpenCommunity(mention.community)
                                true
                            }
                        this +=
                            CustomAccessibilityAction(openUserActionLabel) {
                                onOpenCreator(mention.creator, "")
                                true
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
                        if (onReply != null) {
                            this +=
                                CustomAccessibilityAction(replyActionLabel) {
                                    onReply()
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
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                InboxCardHeader(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .onClick(
                            onClick = onClickPost,
                        ).padding(horizontal = Spacing.s),
                    mention = mention,
                    type = type,
                )
                if (mention.comment.removed) {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = LocalStrings.current.messageContentRemoved,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                    )
                } else {
                    val previewText =
                        if (previewMaxLines == null || previewMaxLines < 0) {
                            mention.comment.text.orEmpty()
                        } else {
                            mention.comment.text
                                .orEmpty()
                                .split("\n")
                                .take(previewMaxLines)
                                .joinToString("\n")
                        }
                    CustomizedContent(ContentFontClass.Body) {
                        PostCardBody(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = Spacing.s,
                                ),
                            text = previewText,
                            autoLoadImages = autoLoadImages,
                            onOpenImage = onImageClick,
                            onClick = {
                                if (textSelection) {
                                    focusManager.clearFocus(true)
                                    textSelection = false
                                } else {
                                    onClickPost.invoke()
                                }
                            },
                            onLongClick = {
                                textSelection = true
                            },
                        )
                    }
                }
                InboxReplySubtitle(
                    modifier =
                    Modifier
                        .onClick(onClick = onClickPost)
                        .padding(
                            start = Spacing.s,
                            end = Spacing.s,
                            top = Spacing.s,
                        ),
                    creator = mention.creator,
                    community = mention.community,
                    autoLoadImages = autoLoadImages,
                    preferNicknames = preferNicknames,
                    date = mention.publishDate,
                    score = mention.score,
                    showScores = showScores,
                    upVotes = mention.upvotes,
                    downVotes = mention.downvotes,
                    voteFormat = voteFormat,
                    upVoted = mention.myVote > 0,
                    downVoted = mention.myVote < 0,
                    downVoteEnabled = downVoteEnabled,
                    optionsMenuOpen = optionsMenuOpen,
                    options = options,
                    onOpenCommunity = onOpenCommunity,
                    onOpenCreator = { user ->
                        onOpenCreator(user, "")
                    },
                    onUpVote = onUpVote,
                    onDownVote = onDownVote,
                    onToggleOptionsMenu = {
                        optionsMenuOpen = it
                    },
                    onSelectOption = onSelectOption,
                    onReply = onReply,
                )
            }
        }
    }
}
