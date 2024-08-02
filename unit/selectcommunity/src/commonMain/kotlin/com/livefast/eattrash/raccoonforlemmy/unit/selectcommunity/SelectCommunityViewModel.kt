package com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity

import cafe.adriel.voyager.core.model.screenModelScope
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SelectCommunityViewModel(
    private val settingsRepository: SettingsRepository,
    private val communityPaginationManager: CommunityPaginationManager,
) : DefaultMviModel<SelectCommunityMviModel.Intent, SelectCommunityMviModel.UiState, SelectCommunityMviModel.Effect>(
        initialState = SelectCommunityMviModel.UiState(),
    ),
    SelectCommunityMviModel {
    init {
        screenModelScope.launch {
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                        )
                    }
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    if (!uiState.value.initial) {
                        refresh()
                    }
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: SelectCommunityMviModel.Intent) {
        when (intent) {
            is SelectCommunityMviModel.Intent.SetSearch -> setSearch(intent.value)
            SelectCommunityMviModel.Intent.LoadNextPage ->
                screenModelScope.launch {
                    loadNextPage()
                }
        }
    }

    private fun setSearch(value: String) {
        screenModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val searchText = uiState.value.searchText
        communityPaginationManager.reset(
            CommunityPaginationSpecification.Subscribed(
                searchText = searchText,
            ),
        )
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                loading = false,
                refreshing = true,
            )
        }
        loadNextPage()
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            return
        }
        val itemsToAdd = communityPaginationManager.loadNextPage()
        updateState {
            it.copy(
                communities = itemsToAdd,
                canFetchMore = communityPaginationManager.canFetchMore,
                loading = false,
                initial = false,
                refreshing = false,
            )
        }
    }
}
