package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationSpecification
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MultiCommunityEditorViewModel(
    private val communityId: Long?,
    private val multiCommunityRepository: MultiCommunityRepository,
    private val communityPaginationManager: CommunityPaginationManager,
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<
        MultiCommunityEditorMviModel.Intent,
        MultiCommunityEditorMviModel.UiState,
        MultiCommunityEditorMviModel.Effect,
        >
    by DefaultMviModelDelegate(initialState = MultiCommunityEditorMviModel.UiState()),
    MultiCommunityEditorMviModel {
    init {
        viewModelScope.launch {
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
                    refresh()
                }.launchIn(this)

            if (uiState.value.communities.isEmpty()) {
                val editedCommunity =
                    communityId?.let {
                        multiCommunityRepository.getById(it)
                    }
                updateState {
                    it.copy(selectedCommunityIds = editedCommunity?.communityIds.orEmpty())
                }
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: MultiCommunityEditorMviModel.Intent) {
        when (intent) {
            is MultiCommunityEditorMviModel.Intent.SelectImage -> selectImage(intent.index)
            is MultiCommunityEditorMviModel.Intent.SetName ->
                viewModelScope.launch {
                    updateState { it.copy(name = intent.value) }
                }

            is MultiCommunityEditorMviModel.Intent.ToggleCommunity -> toggleCommunity(intent.id)
            is MultiCommunityEditorMviModel.Intent.SetSearch -> setSearch(intent.value)
            MultiCommunityEditorMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            MultiCommunityEditorMviModel.Intent.Submit -> submit()
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val editedCommunity =
            communityId?.let {
                multiCommunityRepository.getById(it)
            }
        val searchText = uiState.value.searchText
        communityPaginationManager.reset(
            CommunityPaginationSpecification.Subscribed(
                searchText = searchText,
            ),
        )
        updateState {
            it.copy(
                name = editedCommunity?.name.orEmpty(),
                icon = editedCommunity?.icon,
                refreshing = !initial,
                loading = false,
                canFetchMore = true,
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
                refreshing = false,
            )
        }
    }

    private fun setSearch(value: String) {
        viewModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private fun selectImage(index: Int?) {
        viewModelScope.launch {
            val image =
                if (index == null) {
                    null
                } else {
                    uiState.value.availableIcons[index]
                }
            updateState { it.copy(icon = image) }
        }
    }

    private fun toggleCommunity(communityId: Long) {
        viewModelScope.launch {
            val currentCommunityIds = uiState.value.selectedCommunityIds
            val shouldBeRemoved = currentCommunityIds.contains(communityId)
            val iconUrl =
                uiState.value.communities
                    .firstOrNull { c -> c.id == communityId }
                    ?.icon
            if (shouldBeRemoved) {
                updateState {
                    it.copy(
                        selectedCommunityIds = currentCommunityIds.filter { id -> id != communityId },
                        availableIcons = it.availableIcons.filter { url -> url != iconUrl },
                    )
                }
            } else {
                updateState {
                    it.copy(
                        selectedCommunityIds = currentCommunityIds + communityId,
                        availableIcons =
                        if (iconUrl != null) {
                            it.availableIcons + iconUrl
                        } else {
                            it.availableIcons
                        },
                    )
                }
            }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            updateState { it.copy(nameError = null) }
        }
        val currentState = uiState.value
        var valid = true
        val name = currentState.name
        if (name.isEmpty()) {
            viewModelScope.launch {
                updateState { it.copy(nameError = ValidationError.MissingField) }
            }
            valid = false
        }
        if (!valid) {
            return
        }

        viewModelScope.launch {
            val icon = currentState.icon
            val communityIds = currentState.selectedCommunityIds
            val editedCommunity =
                communityId?.let {
                    multiCommunityRepository.getById(it)
                }
            val multiCommunity =
                editedCommunity?.copy(
                    name = name,
                    icon = icon,
                    communityIds = communityIds,
                ) ?: MultiCommunityModel(
                    name = name,
                    icon = icon,
                    communityIds = communityIds,
                )
            val accountId = accountRepository.getActive()?.id ?: return@launch
            if (multiCommunity.id == null) {
                val id = multiCommunityRepository.create(multiCommunity, accountId)
                notificationCenter.send(
                    NotificationCenterEvent.MultiCommunityCreated(multiCommunity.copy(id = id)),
                )
            } else {
                multiCommunityRepository.update(multiCommunity)
                notificationCenter.send(NotificationCenterEvent.MultiCommunityCreated(multiCommunity))
            }
            emitEffect(MultiCommunityEditorMviModel.Effect.Close)
        }
    }
}
