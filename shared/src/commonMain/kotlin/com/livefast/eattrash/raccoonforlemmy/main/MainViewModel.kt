package com.livefast.eattrash.raccoonforlemmy.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toTabNavigationSections
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.CreateSpecialTagsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification.InboxNotificationChecker
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val inboxCoordinator: InboxCoordinator,
    private val identityRepository: IdentityRepository,
    private val settingRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val notificationChecker: InboxNotificationChecker,
    private val lemmyValueCache: LemmyValueCache,
    private val createSpecialTagsUseCase: CreateSpecialTagsUseCase,
) : ViewModel(),
    MviModelDelegate<MainMviModel.Intent, MainMviModel.UiState, MainMviModel.Effect>
        by DefaultMviModelDelegate(MainMviModel.UiState(
        bottomBarSections = settingRepository.currentBottomBarSections.value.toTabNavigationSections())),
    MainMviModel {
    init {
        viewModelScope.launch {
            identityRepository.startup()
            val auth = identityRepository.authToken.value
            lemmyValueCache.refresh(auth)
            createSpecialTagsUseCase()

            inboxCoordinator.totalUnread
                .onEach { unreadCount ->
                    emitEffect(MainMviModel.Effect.UnreadItemsDetected(unreadCount))
                }.launchIn(this)

            settingRepository.currentSettings
                .map {
                    it.inboxBackgroundCheckPeriod
                }.distinctUntilChanged()
                .onEach {
                    val minutes = it?.inWholeMinutes
                    if (minutes != null) {
                        notificationChecker.setPeriod(minutes)
                        notificationChecker.start()
                    } else {
                        notificationChecker.stop()
                    }
                }.launchIn(this)
            settingRepository.currentSettings
                .onEach {
                    updateCustomProfileIcon()
                }.launchIn(this)
            settingRepository.currentBottomBarSections
                .onEach { sectionIds ->
                    val sections = sectionIds.toTabNavigationSections()
                    updateState { it.copy(bottomBarSections = sections) }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { isLogged ->
                    updateState { it.copy(isLogged = isLogged ?: false) }
                    updateCustomProfileIcon()
                }.launchIn(this)
        }
    }

    override fun reduce(intent: MainMviModel.Intent) {
        when (intent) {
            is MainMviModel.Intent.SetBottomBarOffsetHeightPx -> {
                viewModelScope.launch {
                    updateState { it.copy(bottomBarOffsetHeightPx = intent.value) }
                }
            }

            MainMviModel.Intent.ReadAllInbox -> markAllRead()
        }
    }

    private suspend fun updateCustomProfileIcon() {
        val settings = settingRepository.currentSettings.value
        updateState {
            it.copy(
                customProfileUrl =
                    if (settings.useAvatarAsProfileNavigationIcon) {
                        identityRepository.cachedUser?.avatar
                    } else {
                        null
                    },
            )
        }
    }

    private fun markAllRead() {
        if (inboxCoordinator.totalUnread.value == 0) {
            return
        }

        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            userRepository.readAll(auth)
            inboxCoordinator.sendEvent(InboxCoordinator.Event.Refresh)
            emitEffect(MainMviModel.Effect.ReadAllInboxSuccess)
        }
    }
}
