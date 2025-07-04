package com.livefast.eattrash.raccoonforlemmy.unit.editcommunity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityVisibilityType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditCommunityViewModel(
    private val communityId: Long?,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val mediaRepository: MediaRepository,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<EditCommunityMviModel.Intent, EditCommunityMviModel.UiState, EditCommunityMviModel.Effect>
    by DefaultMviModelDelegate(initialState = EditCommunityMviModel.UiState()),
    EditCommunityMviModel {
    private var originalCommunity: CommunityModel? = null

    init {
        viewModelScope.launch {
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeCommunityVisibility::class)
                .onEach { event ->
                    updateVisibility(event.value)
                }.launchIn(this)
        }
    }

    override fun reduce(intent: EditCommunityMviModel.Intent) {
        when (intent) {
            EditCommunityMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            is EditCommunityMviModel.Intent.IconSelected -> loadImageIcon(intent.value)
            is EditCommunityMviModel.Intent.BannerSelected -> loadImageBanner(intent.value)

            is EditCommunityMviModel.Intent.ChangeDescription -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            description = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }
            }

            is EditCommunityMviModel.Intent.ChangeTitle -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            title = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }
            }

            is EditCommunityMviModel.Intent.ChangeName -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            name = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }
            }

            is EditCommunityMviModel.Intent.ChangeNsfw -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            nsfw = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }
            }

            is EditCommunityMviModel.Intent.ChangePostingRestrictedToMods -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            postingRestrictedToMods = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }
            }

            EditCommunityMviModel.Intent.Submit -> submit()
        }
    }

    private suspend fun refresh() {
        if (communityId == null) {
            return
        }
        updateState { it.copy(loading = true) }
        val auth = identityRepository.authToken.value.orEmpty()
        val community =
            communityRepository.get(
                auth = auth,
                id = communityId,
            )
        originalCommunity = community
        if (community != null) {
            updateState {
                it.copy(
                    name = community.name,
                    title = community.title,
                    icon = community.icon.orEmpty(),
                    banner = community.banner.orEmpty(),
                    description = community.description,
                    nsfw = community.nsfw,
                    postingRestrictedToMods = community.postingRestrictedToMods == true,
                    loading = false,
                    visibilityType = community.visibilityType,
                )
            }
        }
    }

    private fun loadImageIcon(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            if (url != null) {
                updateState {
                    it.copy(
                        icon = url,
                        hasUnsavedChanges = true,
                        loading = false,
                    )
                }
            }
        }
    }

    private fun loadImageBanner(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            if (url != null) {
                updateState {
                    it.copy(
                        banner = url,
                        hasUnsavedChanges = true,
                        loading = false,
                    )
                }
            }
        }
    }

    private fun updateVisibility(value: CommunityVisibilityType) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    visibilityType = value,
                    hasUnsavedChanges = true,
                )
            }
        }
    }

    private fun submit() {
        val community = originalCommunity?.copy() ?: CommunityModel()
        val currentState = uiState.value

        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                val newValue =
                    community.copy(
                        title = currentState.title,
                        description = currentState.description,
                        icon = currentState.icon,
                        banner = currentState.banner,
                        nsfw = currentState.nsfw,
                        postingRestrictedToMods = currentState.postingRestrictedToMods,
                        visibilityType = currentState.visibilityType,
                    )
                if (community.id == 0L) {
                    // creating a new community
                    val newCommunity =
                        communityRepository.create(
                            auth = auth,
                            community = newValue.copy(name = currentState.name),
                        )
                    originalCommunity = newCommunity
                } else {
                    // editing and existing community
                    communityRepository.update(
                        auth = auth,
                        community = newValue,
                    )
                }
                updateState {
                    it.copy(
                        loading = false,
                        hasUnsavedChanges = false,
                    )
                }
                emitEffect(
                    EditCommunityMviModel.Effect.Success,
                )
            } catch (e: Exception) {
                updateState { it.copy(loading = false) }
                emitEffect(
                    EditCommunityMviModel.Effect.Failure,
                )
            }
        }
    }
}
