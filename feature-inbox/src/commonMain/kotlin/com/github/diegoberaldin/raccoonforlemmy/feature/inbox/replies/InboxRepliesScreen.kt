package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.InboxCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.InboxCardType
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeableCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di.getInboxRepliesViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class InboxRepliesScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getInboxRepliesViewModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val navigator = remember { navigationCoordinator.getRootNavigator() }

        LaunchedEffect(model) {
            model.effects.onEach { effect ->
                when (effect) {
                    is InboxRepliesMviModel.Effect.UpdateUnreadItems -> {
                        navigationCoordinator.setInboxUnread(effect.value)
                    }
                }
            }.launchIn(this)
        }

        val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
            model.reduce(InboxRepliesMviModel.Intent.Refresh)
        })
        Box(
            modifier = Modifier.pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                itemsIndexed(uiState.replies) { idx, mention ->
                    val themeRepository = remember { getThemeRepository() }
                    val fontScale by themeRepository.contentFontScale.collectAsState()
                    CompositionLocalProvider(
                        LocalDensity provides Density(
                            density = LocalDensity.current.density,
                            fontScale = fontScale,
                        ),
                    ) {
                        SwipeableCard(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.swipeActionsEnabled,
                            backgroundColor = {
                                when (it) {
                                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.surfaceTint
                                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.tertiary
                                    else -> Color.Transparent
                                }
                            },
                            onGestureBegin = {
                                model.reduce(InboxRepliesMviModel.Intent.HapticIndication)
                            },
                            onDismissToStart = {
                                model.reduce(
                                    InboxRepliesMviModel.Intent.MarkAsRead(
                                        read = true,
                                        mentionId = mention.id,
                                    ),
                                )
                            },
                            onDismissToEnd = {
                                model.reduce(
                                    InboxRepliesMviModel.Intent.MarkAsRead(
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
                                val (iconModifier, iconTint) = when (direction) {
                                    DismissDirection.StartToEnd -> {
                                        Modifier.background(
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            shape = CircleShape,
                                        ) to MaterialTheme.colorScheme.tertiary
                                    }

                                    else -> {
                                        Modifier.background(
                                            color = MaterialTheme.colorScheme.onSecondary,
                                            shape = CircleShape,
                                        ) to MaterialTheme.colorScheme.secondary
                                    }
                                }

                                Icon(
                                    modifier = iconModifier.padding(Spacing.xs),
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = iconTint,
                                )
                            },
                            content = {
                                InboxCard(
                                    mention = mention,
                                    postLayout = uiState.postLayout,
                                    type = InboxCardType.Reply,
                                    onOpenPost = { post ->
                                        navigator?.push(
                                            PostDetailScreen(
                                                post = post,
                                                highlightCommentId = mention.comment.id,
                                            ),
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
                                        model.reduce(InboxRepliesMviModel.Intent.UpVoteComment(idx))
                                    },
                                    onDownVote = {
                                        model.reduce(InboxRepliesMviModel.Intent.DownVoteComment(idx))
                                    },
                                )
                            },
                        )
                        if (uiState.postLayout != PostLayout.Card) {
                            Divider(modifier = Modifier.padding(vertical = Spacing.s))
                        } else {
                            Spacer(modifier = Modifier.height(Spacing.s))
                        }
                    }
                }
                item {
                    if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
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

            PullRefreshIndicator(
                refreshing = uiState.refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
