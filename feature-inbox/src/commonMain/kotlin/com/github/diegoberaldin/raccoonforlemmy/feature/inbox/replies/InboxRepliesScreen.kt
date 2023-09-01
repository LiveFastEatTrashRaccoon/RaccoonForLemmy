package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.InboxReplySubtitle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di.getInboxRepliesViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main.InboxMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main.InboxViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class InboxRepliesScreen(
    private val parentModel: InboxViewModel,
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getInboxRepliesViewModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val parentUiState by parentModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(parentModel) {
            parentModel.uiState.map { it.unreadOnly }.distinctUntilChanged().onEach {
                model.reduce(InboxRepliesMviModel.Intent.ChangeUnreadOnly(unread = it))
            }.launchIn(this)

            if (uiState.unreadOnly != parentUiState.unreadOnly) {
                model.reduce(InboxRepliesMviModel.Intent.ChangeUnreadOnly(parentUiState.unreadOnly))
            }

            parentModel.effects.onEach {
                when (it) {
                    InboxMviModel.Effect.Refresh -> {
                        model.reduce(InboxRepliesMviModel.Intent.Refresh)
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
            var width by remember { mutableStateOf(0f) }
            LazyColumn(
                modifier = Modifier.fillMaxSize().onGloballyPositioned {
                    width = it.size.toSize().width
                },
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                items(uiState.mentions, key = { it.id }) { mention ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            when (it) {
                                DismissValue.DismissedToEnd -> {
                                    model.reduce(
                                        InboxRepliesMviModel.Intent.MarkMentionAsRead(
                                            read = false,
                                            mentionId = mention.id,
                                        ),
                                    )
                                }

                                DismissValue.DismissedToStart -> {
                                    model.reduce(
                                        InboxRepliesMviModel.Intent.MarkMentionAsRead(
                                            read = true,
                                            mentionId = mention.id,
                                        ),
                                    )
                                }

                                else -> Unit
                            }
                            false
                        },
                    )
                    var willDismissDirection: DismissDirection? by remember {
                        mutableStateOf(null)
                    }
                    val threshold = 0.15f
                    LaunchedEffect(Unit) {
                        snapshotFlow { dismissState.offset.value }.collect {
                            willDismissDirection = when {
                                it > width * threshold -> DismissDirection.StartToEnd
                                it < -width * threshold -> DismissDirection.EndToStart
                                else -> null
                            }
                        }
                    }
                    LaunchedEffect(willDismissDirection) {
                        if (willDismissDirection != null) {
                            model.reduce(InboxRepliesMviModel.Intent.HapticIndication)
                        }
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(
                            DismissDirection.StartToEnd,
                            DismissDirection.EndToStart,
                        ),
                        dismissThresholds = {
                            FractionalThreshold(threshold)
                        },
                        background = {
                            val direction =
                                dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.Transparent
                                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.secondary
                                    DismissValue.DismissedToStart,
                                    -> MaterialTheme.colorScheme.secondary
                                },
                            )
                            val alignment = when (direction) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> Icons.Default.MarkChatUnread
                                DismissDirection.EndToStart -> Icons.Default.MarkChatRead
                            }
                            val iconModifier = Modifier.background(
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = CircleShape,
                            )
                            val iconTint = MaterialTheme.colorScheme.secondary

                            Box(
                                Modifier.fillMaxSize().background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment,
                            ) {
                                Icon(
                                    modifier = iconModifier,
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = iconTint,
                                )
                            }
                        },
                    ) {
                        Card(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(CornerSize.m),
                            ).padding(
                                vertical = Spacing.s,
                                horizontal = Spacing.s,
                            ).onClick {
                                navigator.push(
                                    PostDetailScreen(
                                        post = mention.post,
                                        onBack = {
                                            navigator.pop()
                                        },
                                    ),
                                )
                            },
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
                            ) {
                                InboxReplyHeader(
                                    mention = mention,
                                )
                                PostCardBody(
                                    text = mention.comment.text,
                                )
                                InboxReplySubtitle(
                                    creator = mention.creator,
                                    community = mention.community,
                                    date = mention.publishDate,
                                    score = mention.score,
                                    upVoted = mention.myVote > 0,
                                    downVoted = mention.myVote < 0,
                                    onOpenCreator = { user ->
                                        navigator.push(
                                            UserDetailScreen(
                                                user = user,
                                                onBack = {
                                                    navigator.pop()
                                                },
                                            ),
                                        )
                                    },
                                    onOpenCommunity = { community ->
                                        navigator.push(
                                            CommunityDetailScreen(
                                                community = community,
                                                onBack = {
                                                    navigator.pop()
                                                },
                                            ),
                                        )
                                    },
                                )
                            }
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
                    Spacer(modifier = Modifier.height(Spacing.xxl))
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
