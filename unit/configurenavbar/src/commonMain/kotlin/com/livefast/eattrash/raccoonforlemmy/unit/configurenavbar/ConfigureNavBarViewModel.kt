package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInts
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toTabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class ConfigureNavBarViewModel(
    private val accountRepository: AccountRepository,
    private val identityRepository: IdentityRepository,
    private val bottomNavItemsRepository: BottomNavItemsRepository,
    private val settingsRepository: SettingsRepository,
    private val hapticFeedback: HapticFeedback,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<ConfigureNavBarMviModel.Intent, ConfigureNavBarMviModel.UiState, ConfigureNavBarMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ConfigureNavBarMviModel.UiState()),
    ConfigureNavBarMviModel {
    init {
        viewModelScope.launch {
            notificationCenter
                .subscribe(NotificationCenterEvent.TabNavigationSectionSelected::class)
                .onEach { evt ->
                    evt.sectionId.toTabNavigationSection()?.also { section ->
                        handleAdd(section)
                    }
                }.launchIn(this)

            refresh()
        }
    }

    override fun reduce(intent: ConfigureNavBarMviModel.Intent) {
        when (intent) {
            ConfigureNavBarMviModel.Intent.HapticFeedback -> hapticFeedback.vibrate()
            ConfigureNavBarMviModel.Intent.Reset -> handleReset()
            is ConfigureNavBarMviModel.Intent.Delete -> handleDelete(intent.section)
            is ConfigureNavBarMviModel.Intent.SwapItems -> handleSwap(intent.from, intent.to)
            ConfigureNavBarMviModel.Intent.Save -> save()
        }
    }

    private suspend fun refresh() {
        val accountId = accountRepository.getActive()?.id
        val currentSections = bottomNavItemsRepository.get(accountId)
        updateState {
            it.copy(
                sections = currentSections,
                availableSections = getAvailableSections(currentSections),
            )
        }
    }

    private fun getAvailableSections(
        excludeSections: List<TabNavigationSection> = emptyList(),
    ): List<TabNavigationSection> {
        val availableSections =
            buildList {
                this += TabNavigationSection.Home
                this += TabNavigationSection.Explore
                this += TabNavigationSection.Inbox
                this += TabNavigationSection.Profile
                this += TabNavigationSection.Settings
                if (identityRepository.isLogged.value == true) {
                    this += TabNavigationSection.Bookmarks
                }
            }
        return availableSections.filter { section -> section !in excludeSections }
    }

    private fun handleReset() {
        viewModelScope.launch {
            val oldSections = uiState.value.sections
            val newSections = BottomNavItemsRepository.DEFAULT_ITEMS
            updateState {
                it.copy(
                    sections = newSections,
                    availableSections = getAvailableSections(newSections),
                    hasUnsavedChanges = newSections != oldSections,
                )
            }
        }
    }

    private fun handleAdd(section: TabNavigationSection) {
        val newSections = uiState.value.sections + section
        viewModelScope.launch {
            updateState {
                it.copy(
                    sections = newSections,
                    availableSections = getAvailableSections(newSections),
                    hasUnsavedChanges = true,
                )
            }
        }
    }

    private fun handleDelete(section: TabNavigationSection) {
        val newSections = uiState.value.sections - section
        viewModelScope.launch {
            updateState {
                it.copy(
                    sections = newSections,
                    availableSections = getAvailableSections(newSections),
                    hasUnsavedChanges = true,
                )
            }
        }
    }

    private fun handleSwap(from: Int, to: Int) {
        val newSections =
            uiState.value.sections.toMutableList().apply {
                val element = removeAt(from)
                add(to, element)
            }
        viewModelScope.launch {
            updateState {
                it.copy(
                    sections = newSections,
                    hasUnsavedChanges = true,
                )
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            val currentSections = uiState.value.sections
            val accountId = accountRepository.getActive()?.id
            bottomNavItemsRepository.update(accountId, currentSections)
            updateState {
                it.copy(hasUnsavedChanges = false)
            }
            settingsRepository.changeCurrentBottomBarSections(currentSections.toInts())
        }
    }
}
