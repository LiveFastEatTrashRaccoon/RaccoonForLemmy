package com.livefast.eattrash.raccoonforlemmy.unit.modlog

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.ModlogRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ModlogViewModel(
    private val communityId: Long,
    private val themeRepository: ThemeRepository,
    private val identityRepository: IdentityRepository,
    private val modlogRepository: ModlogRepository,
    private val settingsRepository: SettingsRepository,
) : DefaultMviModel<ModlogMviModel.Intent, ModlogMviModel.UiState, ModlogMviModel.Effect>(
    initialState = ModlogMviModel.UiState(),
),
    ModlogMviModel {
    private var currentPage: Int = 1

    init {
        screenModelScope.launch {
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            settingsRepository.currentSettings.onEach { settings ->
                updateState {
                    it.copy(
                        autoLoadImages = settings.autoLoadImages,
                        preferNicknames = settings.preferUserNicknames,
                    )
                }
            }

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: ModlogMviModel.Intent) {
        when (intent) {
            ModlogMviModel.Intent.Refresh -> refresh()
            ModlogMviModel.Intent.LoadNextPage ->
                screenModelScope.launch {
                    loadNextPage()
                }
        }
    }

    private fun refresh(initial: Boolean = false) {
        screenModelScope.launch {
            currentPage = 1
            updateState {
                it.copy(
                    canFetchMore = true,
                    refreshing = !initial,
                    initial = initial,
                    loading = false,
                )
            }
            loadNextPage()
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            screenModelScope.launch {
                updateState { it.copy(refreshing = false) }
            }
            return
        }

        screenModelScope.launch {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val refreshing = currentState.refreshing
            val itemList =
                modlogRepository.getItems(
                    auth = auth,
                    communityId = communityId.takeIf { it != 0L },
                    page = currentPage,
                )
            val itemsToAdd = itemList.orEmpty()
            updateState {
                val modlogItems =
                    if (refreshing) {
                        itemsToAdd
                    } else {
                        it.items + itemsToAdd
                    }
                it.copy(
                    items = modlogItems,
                    loading = false,
                    canFetchMore = itemList?.isEmpty() != true,
                    refreshing = false,
                    initial = false,
                )
            }
            if (!itemList.isNullOrEmpty()) {
                currentPage++
            }
        }
    }
}
