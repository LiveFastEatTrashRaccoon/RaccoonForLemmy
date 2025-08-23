package com.livefast.eattrash.raccoonforlemmy.feature.inbox.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.toInboxUnreadOnly
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InboxViewModel(
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
    private val coordinator: InboxCoordinator,
    private val settingsRepository: SettingsRepository,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<InboxMviModel.Intent, InboxMviModel.UiState, InboxMviModel.Effect>
    by DefaultMviModelDelegate(initialState = InboxMviModel.UiState()),
    InboxMviModel {
    init {
        viewModelScope.launch {
            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged) }
                }.launchIn(this)

            coordinator.unreadMentions
                .onEach { value ->
                    updateState { it.copy(unreadMentions = value) }
                }.launchIn(this)
            coordinator.unreadReplies
                .onEach { value ->
                    updateState { it.copy(unreadReplies = value) }
                }.launchIn(this)
            coordinator.unreadMessages
                .onEach { value ->
                    updateState { it.copy(unreadMessages = value) }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.ResetInbox::class)
                .onEach {
                    onFirstLoad()
                }.launchIn(this)
        }
        onFirstLoad()
    }

    private fun onFirstLoad() {
        val settingsUnreadOnly =
            settingsRepository.currentSettings.value.defaultInboxType
                .toInboxUnreadOnly()
        if (uiState.value.unreadOnly != settingsUnreadOnly) {
            changeUnreadOnly(settingsUnreadOnly)
        }
    }

    override fun reduce(intent: InboxMviModel.Intent) {
        when (intent) {
            is InboxMviModel.Intent.ChangeSection ->
                viewModelScope.launch {
                    updateState {
                        it.copy(section = intent.value)
                    }
                }

            InboxMviModel.Intent.ReadAll -> markAllRead()
            is InboxMviModel.Intent.ChangeInboxType -> changeUnreadOnly(unreadOnly = intent.unreadOnly)
        }
    }

    private fun changeUnreadOnly(unreadOnly: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(unreadOnly = unreadOnly)
            }
            coordinator.setUnreadOnly(unreadOnly)
        }
    }

    private fun markAllRead() {
        if (coordinator.totalUnread.value == 0) {
            return
        }

        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            userRepository.readAll(auth)
            emitEffect(InboxMviModel.Effect.Refresh)
            coordinator.sendEvent(InboxCoordinator.Event.Refresh)
            emitEffect(InboxMviModel.Effect.ReadAllInboxSuccess)
        }
    }
}
