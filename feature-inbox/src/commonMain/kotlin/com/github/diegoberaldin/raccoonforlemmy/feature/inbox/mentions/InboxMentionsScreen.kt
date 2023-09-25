package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.InboxMentionCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeableCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di.getInboxMentionsViewModel

class InboxMentionsScreen : Tab {

    override val options: TabOptions
        @Composable get() {
            return TabOptions(1u, "")
        }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getInboxMentionsViewModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = remember { getNavigationCoordinator().getRootNavigator() }

        val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
            model.reduce(InboxMentionsMviModel.Intent.Refresh)
        })
        Box(
            modifier = Modifier.pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                itemsIndexed(uiState.mentions) { idx, mention ->
                    SwipeableCard(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.swipeActionsEnabled,
                        backgroundColor = {
                            when (it) {
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.secondary
                                DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.tertiary
                                else -> Color.Transparent
                            }
                        },
                        onGestureBegin = {
                            model.reduce(InboxMentionsMviModel.Intent.HapticIndication)
                        },
                        onDismissToStart = {
                            model.reduce(
                                InboxMentionsMviModel.Intent.MarkAsRead(
                                    read = true,
                                    mentionId = mention.id,
                                ),
                            )
                        },
                        onDismissToEnd = {
                            model.reduce(
                                InboxMentionsMviModel.Intent.MarkAsRead(
                                    read = false,
                                    mentionId = mention.id,
                                ),
                            )
                        },
                        swipeContent = { direction ->
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> Icons.Default.MarkChatUnread
                                DismissDirection.EndToStart -> Icons.Default.MarkChatRead
                            }
                            Icon(
                                modifier = Modifier.padding(Spacing.xs),
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        },
                        content = {
                            InboxMentionCard(
                                mention = mention,
                                postLayout = uiState.postLayout,
                                onOpenPost = { post ->
                                    navigator?.push(
                                        PostDetailScreen(post),
                                    )
                                },
                                onOpenCreator = { user ->
                                    navigator?.push(
                                        UserDetailScreen(user),
                                    )
                                },
                                onOpenCommunity = { community ->
                                    navigator?.push(
                                        CommunityDetailScreen(community),
                                    )
                                },
                                onUpVote = {
                                    model.reduce(InboxMentionsMviModel.Intent.UpVoteComment(idx))
                                },
                                onDownVote = {
                                    model.reduce(InboxMentionsMviModel.Intent.DownVoteComment(idx))
                                },
                            )
                        },
                    )
                }
                item {
                    if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
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

            PullRefreshIndicator(
                refreshing = uiState.refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
