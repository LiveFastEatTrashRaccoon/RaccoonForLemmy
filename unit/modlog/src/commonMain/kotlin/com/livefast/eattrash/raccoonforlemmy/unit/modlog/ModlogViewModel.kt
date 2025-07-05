package com.livefast.eattrash.raccoonforlemmy.unit.modlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
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
) : ViewModel(),
    MviModelDelegate<ModlogMviModel.Intent, ModlogMviModel.UiState, ModlogMviModel.Effect> by
    DefaultMviModelDelegate(initialState = ModlogMviModel.UiState()),
    ModlogMviModel {
    private var currentPage: Int = 1

    init {
        viewModelScope.launch {
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
                viewModelScope.launch {
                    loadNextPage()
                }
        }
    }

    private fun refresh(initial: Boolean = false) {
        viewModelScope.launch {
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
            viewModelScope.launch {
                updateState { it.copy(refreshing = false) }
            }
            return
        }

        viewModelScope.launch {
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
