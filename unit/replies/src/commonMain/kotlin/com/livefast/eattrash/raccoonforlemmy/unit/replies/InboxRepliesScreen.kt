package com.livefast.eattrash.raccoonforlemmy.unit.replies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.InboxCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.InboxCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.InboxCardType
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxRepliesScreen(modifier: Modifier = Modifier) {
    val model: InboxRepliesMviModel = getViewModel<InboxRepliesViewModel>()
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val lazyListState = rememberLazyListState()
    val detailOpener = remember { getDetailOpener() }
    val themeRepository = remember { getThemeRepository() }
    val upVoteColor by themeRepository.upVoteColor.collectAsState()
    val downVoteColor by themeRepository.downVoteColor.collectAsState()
    val toggleReadColor by themeRepository.saveColor.collectAsState()
    val defaultUpvoteColor = MaterialTheme.colorScheme.primary
    val defaultToggleReadColor = MaterialTheme.colorScheme.secondary
    val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary

    LaunchedEffect(navigationCoordinator) {
        navigationCoordinator.onDoubleTabSelection
            .onEach { section ->
                runCatching {
                    if (section == TabNavigationSection.Inbox) {
                        lazyListState.scrollToItem(0)
                    }
                }
            }.launchIn(this)
    }
    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    is InboxRepliesMviModel.Effect.UpdateUnreadItems -> {
                        navigationCoordinator.setInboxUnread(effect.value)
                    }

                    InboxRepliesMviModel.Effect.BackToTop -> {
                        runCatching {
                            lazyListState.scrollToItem(0)
                        }
                    }

                    is InboxRepliesMviModel.Effect.OpenDetail ->
                        detailOpener.openPostDetail(
                            post = effect.post,
                            highlightCommentId = effect.commentId,
                        )
                }
            }.launchIn(this)
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = uiState.refreshing,
        onRefresh = {
            model.reduce(InboxRepliesMviModel.Intent.Refresh)
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            if (uiState.replies.isEmpty() && uiState.initial) {
                items(3) {
                    InboxCardPlaceholder(
                        postLayout = uiState.postLayout,
                    )
                    if (uiState.postLayout != PostLayout.Card) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                    } else {
                        Spacer(modifier = Modifier.height(Spacing.interItem))
                    }
                }
            }
            if (uiState.replies.isEmpty() && !uiState.initial) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs),
                        textAlign = TextAlign.Center,
                        text = LocalStrings.current.messageEmptyList,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            items(
                items = uiState.replies,
                key = { it.id.toString() + uiState.unreadOnly },
            ) { reply ->

                @Composable
                fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> = mapNotNull {
                    when (it) {
                        ActionOnSwipe.UpVote ->
                            SwipeAction(
                                swipeContent = {
                                    Icon(
                                        modifier = VoteAction.UpVote.toModifier(),
                                        imageVector = VoteAction.UpVote.toIcon(),
                                        contentDescription = LocalStrings.current.actionUpvote,
                                        tint = Color.White,
                                    )
                                },
                                backgroundColor = upVoteColor ?: defaultUpvoteColor,
                                onTriggered = {
                                    model.reduce(
                                        InboxRepliesMviModel.Intent.UpVoteComment(
                                            reply.id,
                                        ),
                                    )
                                },
                            )

                        ActionOnSwipe.DownVote ->
                            SwipeAction(
                                swipeContent = {
                                    Icon(
                                        modifier = VoteAction.DownVote.toModifier(),
                                        imageVector = VoteAction.DownVote.toIcon(),
                                        contentDescription = LocalStrings.current.actionDownvote,
                                        tint = Color.White,
                                    )
                                },
                                backgroundColor = downVoteColor ?: defaultDownVoteColor,
                                onTriggered = {
                                    model.reduce(
                                        InboxRepliesMviModel.Intent.DownVoteComment(
                                            reply.id,
                                        ),
                                    )
                                },
                            ).takeIf { uiState.downVoteEnabled }

                        ActionOnSwipe.ToggleRead ->
                            SwipeAction(
                                swipeContent = {
                                    val icon =
                                        when {
                                            reply.read -> Icons.Default.MarkChatUnread
                                            else -> Icons.Default.MarkChatRead
                                        }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = LocalStrings.current.actionMarkAsRead,
                                        tint = Color.White,
                                    )
                                },
                                backgroundColor = toggleReadColor ?: defaultToggleReadColor,
                                onTriggered = {
                                    model.reduce(
                                        InboxRepliesMviModel.Intent.MarkAsRead(
                                            read = !reply.read,
                                            id = reply.id,
                                        ),
                                    )
                                },
                            )

                        else -> null
                    }
                }

                SwipeActionCard(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.swipeActionsEnabled,
                    onGestureBegin = {
                        model.reduce(InboxRepliesMviModel.Intent.HapticIndication)
                    },
                    swipeToStartActions = uiState.actionsOnSwipeToStartInbox.toSwipeActions(),
                    swipeToEndActions = uiState.actionsOnSwipeToEndInbox.toSwipeActions(),
                    content = {
                        InboxCard(
                            mention = reply,
                            postLayout = uiState.postLayout,
                            type = InboxCardType.Reply,
                            autoLoadImages = uiState.autoLoadImages,
                            preferNicknames = uiState.preferNicknames,
                            showScores = uiState.showScores,
                            voteFormat = uiState.voteFormat,
                            downVoteEnabled = uiState.downVoteEnabled,
                            previewMaxLines = uiState.previewMaxLines,
                            onClick = { post ->
                                model.reduce(
                                    InboxRepliesMviModel.Intent.WillOpenDetail(
                                        id = reply.id,
                                        post = post,
                                        commentId = reply.comment.id,
                                    ),
                                )
                            },
                            onOpenCreator = { user, instance ->
                                detailOpener.openUserDetail(
                                    user = user,
                                    otherInstance = instance,
                                )
                            },
                            onOpenCommunity = { community ->
                                detailOpener.openCommunityDetail(community = community)
                            },
                            onImageClick = { url ->
                                navigationCoordinator.pushScreen(
                                    ZoomableImageScreen(
                                        url = url,
                                        source =
                                        reply.post.community
                                            ?.readableHandle
                                            .orEmpty(),
                                    ),
                                )
                            },
                            onUpVote = {
                                model.reduce(InboxRepliesMviModel.Intent.UpVoteComment(reply.id))
                            },
                            onDownVote = {
                                model.reduce(InboxRepliesMviModel.Intent.DownVoteComment(reply.id))
                            },
                            onReply = {
                                detailOpener.openReply(
                                    originalPost = reply.post,
                                    originalComment = reply.comment,
                                )
                            },
                        )
                    },
                )
                if (uiState.postLayout != PostLayout.Card) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                } else {
                    Spacer(modifier = Modifier.height(Spacing.interItem))
                }
            }
            item {
                if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                    model.reduce(InboxRepliesMviModel.Intent.LoadNextPage)
                }
                if (uiState.loading && !uiState.refreshing) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(Spacing.xxxl))
            }
        }
    }
}
