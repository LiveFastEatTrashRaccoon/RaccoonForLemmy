package com.livefast.eattrash.raccoonforlemmy.unit.medialist

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory(binds = [MediaListMviModel::class])
class MediaListViewModel(
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val mediaRepository: MediaRepository,
    private val settingsRepository: SettingsRepository,
    private val themeRepository: ThemeRepository,
) : DefaultMviModel<MediaListMviModel.Intent, MediaListMviModel.State, MediaListMviModel.Effect>(
        initialState = MediaListMviModel.State(),
    ),
    MediaListMviModel {
    private var currentPage = 1

    init {
        screenModelScope.launch {
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoloadImages = settings.autoLoadImages,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                        )
                    }
                }.launchIn(this)

            apiConfigurationRepository.instance
                .onEach { instance ->
                    updateState { it.copy(currentInstance = instance) }
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: MediaListMviModel.Intent) {
        when (intent) {
            MediaListMviModel.Intent.Refresh ->
                screenModelScope.launch {
                    refresh()
                }

            MediaListMviModel.Intent.LoadNextPage ->
                screenModelScope.launch {
                    loadNextPage()
                }

            is MediaListMviModel.Intent.Delete -> deleteMedia(intent.media)
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
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        val refreshing = currentState.refreshing
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        updateState { it.copy(loading = true) }

        val auth = identityRepository.authToken.value
        val itemsToAdd =
            mediaRepository.getAll(
                auth = auth,
                page = currentPage,
            )
        if (itemsToAdd.isNotEmpty()) {
            currentPage++
        }
        updateState {
            val newItems =
                if (refreshing) {
                    itemsToAdd
                } else {
                    it.media + itemsToAdd
                }
            it.copy(
                media = newItems,
                loading = false,
                canFetchMore = itemsToAdd.isNotEmpty(),
                refreshing = false,
                initial = false,
            )
        }
    }

    private fun deleteMedia(media: MediaModel) {
        screenModelScope.launch {
            try {
                val auth = identityRepository.authToken.value
                mediaRepository.delete(
                    auth = auth,
                    media = media,
                )
                updateState { it.copy(media = it.media.filter { m -> m.alias != media.alias }) }
                emitEffect(MediaListMviModel.Effect.Success)
            } catch (e: Exception) {
                emitEffect(MediaListMviModel.Effect.Failure(e.message))
            }
        }
    }
}
