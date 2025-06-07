package com.livefast.eattrash.raccoonforlemmy.unit.mentions

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
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
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

class InboxMentionsScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(1u, "")
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: InboxMentionsMviModel = rememberScreenModel()
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
                        is InboxMentionsMviModel.Effect.UpdateUnreadItems -> {
                            navigationCoordinator.setInboxUnread(effect.value)
                        }

                        InboxMentionsMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                            }
                        }

                        is InboxMentionsMviModel.Effect.OpenDetail ->
                            detailOpener.openPostDetail(
                                post = effect.post,
                                highlightCommentId = effect.commentId,
                            )
                    }
                }.launchIn(this)
        }

        PullToRefreshBox(
            isRefreshing = uiState.refreshing,
            onRefresh = {
                model.reduce(InboxMentionsMviModel.Intent.Refresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                if (uiState.mentions.isEmpty() && uiState.initial) {
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
                if (uiState.mentions.isEmpty() && !uiState.initial) {
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
                    items = uiState.mentions,
                    key = { it.id.toString() + uiState.unreadOnly },
                ) { mention ->
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
                                            InboxMentionsMviModel.Intent.UpVoteComment(
                                                mention.id,
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
                                            InboxMentionsMviModel.Intent.DownVoteComment(
                                                mention.id,
                                            ),
                                        )
                                    },
                                ).takeIf { uiState.downVoteEnabled }

                            ActionOnSwipe.ToggleRead ->
                                SwipeAction(
                                    swipeContent = {
                                        val icon =
                                            when {
                                                mention.read -> Icons.Default.MarkChatUnread
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
                                            InboxMentionsMviModel.Intent.MarkAsRead(
                                                read = !mention.read,
                                                id = mention.id,
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
                            model.reduce(InboxMentionsMviModel.Intent.HapticIndication)
                        },
                        swipeToStartActions = uiState.actionsOnSwipeToStartInbox.toSwipeActions(),
                        swipeToEndActions = uiState.actionsOnSwipeToEndInbox.toSwipeActions(),
                        content = {
                            InboxCard(
                                mention = mention,
                                postLayout = uiState.postLayout,
                                type = InboxCardType.Mention,
                                autoLoadImages = uiState.autoLoadImages,
                                preferNicknames = uiState.preferNicknames,
                                showScores = uiState.showScores,
                                voteFormat = uiState.voteFormat,
                                downVoteEnabled = uiState.downVoteEnabled,
                                previewMaxLines = uiState.previewMaxLines,
                                onClick = { post ->
                                    model.reduce(
                                        InboxMentionsMviModel.Intent.WillOpenDetail(
                                            id = mention.id,
                                            post = post,
                                            commentId = mention.comment.id,
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
                                            mention.post.community
                                                ?.readableHandle
                                                .orEmpty(),
                                        ),
                                    )
                                },
                                onUpVote = {
                                    model.reduce(InboxMentionsMviModel.Intent.UpVoteComment(mention.id))
                                },
                                onDownVote = {
                                    model.reduce(
                                        InboxMentionsMviModel.Intent.DownVoteComment(mention.id),
                                    )
                                },
                                onReply = {
                                    detailOpener.openReply(
                                        originalPost = mention.post,
                                        originalComment = mention.comment,
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
                        model.reduce(InboxMentionsMviModel.Intent.LoadNextPage)
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
}
