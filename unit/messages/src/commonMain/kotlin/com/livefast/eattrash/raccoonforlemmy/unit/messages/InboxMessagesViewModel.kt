package com.livefast.eattrash.raccoonforlemmy.unit.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.otherUser
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PrivateMessageRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InboxMessagesViewModel(
    private val identityRepository: IdentityRepository,
    private val siteRepository: SiteRepository,
    private val messageRepository: PrivateMessageRepository,
    private val settingsRepository: SettingsRepository,
    private val coordinator: InboxCoordinator,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<InboxMessagesMviModel.Intent, InboxMessagesMviModel.UiState, InboxMessagesMviModel.Effect>
    by DefaultMviModelDelegate(initialState = InboxMessagesMviModel.UiState()),
    InboxMessagesMviModel {
    private var currentPage: Int = 1

    init {
        viewModelScope.launch {
            coordinator.events
                .onEach {
                    when (it) {
                        InboxCoordinator.Event.Refresh -> {
                            refresh()
                            emitEffect(InboxMessagesMviModel.Effect.BackToTop)
                        }
                    }
                }.launchIn(this)
            coordinator.unreadOnly
                .onEach {
                    if (it != uiState.value.unreadOnly) {
                        changeUnreadOnly(it)
                    }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    handleLogout()
                }.launchIn(this)

            val auth = identityRepository.authToken.value.orEmpty()
            val currentUserId = siteRepository.getCurrentUser(auth)?.id ?: 0
            updateState { it.copy(currentUserId = currentUserId) }

            if (uiState.value.initial) {
                val value = coordinator.unreadOnly.value
                changeUnreadOnly(value)
                refresh(initial = true)
            }
            updateUnreadItems()
        }
    }

    override fun reduce(intent: InboxMessagesMviModel.Intent) {
        when (intent) {
            InboxMessagesMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            InboxMessagesMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                    emitEffect(InboxMessagesMviModel.Effect.BackToTop)
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        currentPage = 1
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                refreshing = !initial,
                loading = false,
            )
        }
        loadNextPage()
        updateUnreadItems()
    }

    private fun changeUnreadOnly(value: Boolean) {
        if (uiState.value.currentUserId == 0L) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(unreadOnly = value) }
            refresh(initial = true)
            emitEffect(InboxMessagesMviModel.Effect.BackToTop)
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }

        updateState { it.copy(loading = true) }
        val auth = identityRepository.authToken.value
        val refreshing = currentState.refreshing
        val unreadOnly = currentState.unreadOnly
        val itemList =
            messageRepository
                .getAll(
                    auth = auth,
                    page = currentPage,
                    unreadOnly = unreadOnly,
                )?.groupBy {
                    it.otherUser(currentState.currentUserId)?.id ?: 0
                }?.mapNotNull { entry ->
                    val messages = entry.value.sortedBy { m -> m.publishDate }
                    messages.lastOrNull()
                }
        if (!itemList.isNullOrEmpty()) {
            currentPage++
        }
        updateState {
            val newItems =
                if (refreshing) {
                    itemList.orEmpty()
                } else {
                    buildList {
                        addAll(it.chats)
                        addAll(
                            itemList.orEmpty().filter { outerChat ->
                                val outerOtherUser = outerChat.otherUser(currentState.currentUserId)
                                currentState.chats.none { chat ->
                                    val otherUser = chat.otherUser(currentState.currentUserId)
                                    outerOtherUser == otherUser
                                }
                            },
                        )
                    }.distinctBy { msg -> msg.id }
                }
            it.copy(
                chats = newItems,
                loading = false,
                canFetchMore = itemList?.isEmpty() != true,
                refreshing = false,
                initial = false,
            )
        }
    }

    private fun updateUnreadItems() {
        viewModelScope.launch {
            val unreadCount = coordinator.updateUnreadCount()
            emitEffect(InboxMessagesMviModel.Effect.UpdateUnreadItems(unreadCount))
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            updateState { it.copy(chats = emptyList()) }
            refresh(initial = true)
        }
    }
}
