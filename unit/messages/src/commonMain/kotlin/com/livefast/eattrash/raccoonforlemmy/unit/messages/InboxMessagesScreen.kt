package com.livefast.eattrash.raccoonforlemmy.unit.messages

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.otherUser
import com.livefast.eattrash.raccoonforlemmy.unit.messages.components.ChatCard
import com.livefast.eattrash.raccoonforlemmy.unit.messages.components.ChatCardPlaceholder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxMessagesScreen(modifier: Modifier = Modifier) {
    val model: InboxMessagesMviModel = getViewModel<InboxMessagesViewModel>()
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val mainRouter = remember { getMainRouter() }
    val lazyListState = rememberLazyListState()

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
                    is InboxMessagesMviModel.Effect.UpdateUnreadItems -> {
                        navigationCoordinator.setInboxUnread(effect.value)
                    }

                    InboxMessagesMviModel.Effect.BackToTop -> {
                        runCatching {
                            lazyListState.scrollToItem(0)
                        }
                    }
                }
            }.launchIn(this)
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = uiState.refreshing,
        onRefresh = {
            model.reduce(InboxMessagesMviModel.Intent.Refresh)
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            if (uiState.chats.isEmpty() && uiState.initial) {
                items(3) {
                    ChatCardPlaceholder(modifier = Modifier.padding(top = Spacing.interItem))
                }
            }
            if (uiState.chats.isEmpty() && !uiState.initial) {
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
                items = uiState.chats,
                key = {
                    it.id.toString() + (
                        it.updateDate ?: it.publishDate
                        ) + it.read + uiState.unreadOnly
                },
            ) { chat ->
                ChatCard(
                    user = chat.otherUser(uiState.currentUserId),
                    autoLoadImages = uiState.autoLoadImages,
                    lastMessage = chat.content.orEmpty(),
                    lastMessageDate = chat.publishDate,
                    onOpenUser = { user ->
                        mainRouter.openUserDetail(user, "")
                    },
                    onOpen = {
                        val userId = chat.otherUser(uiState.currentUserId)?.id
                        if (userId != null) {
                            mainRouter.openChat(userId)
                        }
                    },
                )
            }
            item {
                if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                    model.reduce(InboxMessagesMviModel.Intent.LoadNextPage)
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
