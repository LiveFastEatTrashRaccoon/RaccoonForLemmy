package com.livefast.eattrash.raccoonforlemmy.unit.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DraftRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.epochMillis
import com.livefast.eattrash.raccoonforlemmy.core.utils.isValidUrl
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val editedPostId: Long,
    private val crossPostId: Long,
    private val draftId: Long,
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val itemCache: LemmyItemCache,
    private val communityRepository: CommunityRepository,
    private val accountRepository: AccountRepository,
    private val draftRepository: DraftRepository,
    private val notificationCenter: NotificationCenter,
    private val communityPreferredLanguageRepository: CommunityPreferredLanguageRepository,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<CreatePostMviModel.Intent, CreatePostMviModel.UiState, CreatePostMviModel.Effect>
    by DefaultMviModelDelegate(initialState = CreatePostMviModel.UiState()),
    CreatePostMviModel {
    init {
        viewModelScope.launch {
            val editedPost =
                editedPostId.takeIf { it != 0L }?.let {
                    itemCache.getPost(it)
                }
            val crossPost =
                crossPostId.takeIf { it != 0L }?.let {
                    itemCache.getPost(it)
                }
            updateState { it.copy(editedPost = editedPost, crossPost = crossPost) }

            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            showScores = settings.showScores,
                        )
                    }
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(downVoteEnabled = value)
                    }
                }.launchIn(this)

            if (uiState.value.currentUser.isEmpty()) {
                val auth = identityRepository.authToken.value.orEmpty()
                val currentUser = siteRepository.getCurrentUser(auth)
                val languages = siteRepository.getLanguages(auth)
                if (currentUser != null) {
                    updateState {
                        it.copy(
                            currentUser = currentUser.name,
                            currentInstance = currentUser.host,
                            availableLanguages = languages,
                        )
                    }
                }
            }
        }
    }

    override fun reduce(intent: CreatePostMviModel.Intent) {
        when (intent) {
            is CreatePostMviModel.Intent.SetCommunity -> {
                updateCommunity(intent.value)
            }

            is CreatePostMviModel.Intent.SetTitle -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(title = intent.value)
                    }
                }
            }

            is CreatePostMviModel.Intent.ChangeNsfw -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(nsfw = intent.value)
                    }
                }
            }

            is CreatePostMviModel.Intent.SetUrl -> {
                viewModelScope.launch {
                    updateState {
                        it.copy(url = intent.value)
                    }
                }
            }

            is CreatePostMviModel.Intent.ImageSelected -> {
                loadImageAndObtainUrl(intent.value)
            }

            is CreatePostMviModel.Intent.InsertImageInBody -> {
                loadImageAndAppendUrlInBody(intent.value)
            }

            is CreatePostMviModel.Intent.ChangeSection ->
                viewModelScope.launch {
                    updateState {
                        it.copy(section = intent.value)
                    }
                }

            is CreatePostMviModel.Intent.ChangeLanguage ->
                viewModelScope.launch {
                    updateState {
                        it.copy(currentLanguageId = intent.value)
                    }
                }

            is CreatePostMviModel.Intent.ChangeBodyValue ->
                viewModelScope.launch {
                    updateState {
                        it.copy(bodyValue = intent.value)
                    }
                }

            CreatePostMviModel.Intent.Send -> submit()

            CreatePostMviModel.Intent.SaveDraft -> saveDraft()
            CreatePostMviModel.Intent.AutoFillTitle -> autofillTitle()
        }
    }

    private fun updateCommunity(community: CommunityModel) {
        val preferNicknames = uiState.value.preferNicknames
        val communityId = community.id
        val name = community.readableName(preferNicknames)

        viewModelScope.launch {
            val (actualName, actualHandle) =
                if (name.isEmpty()) {
                    val auth = identityRepository.authToken.value.orEmpty()
                    val remoteCommunity = communityRepository.get(auth = auth, id = communityId)
                    remoteCommunity?.name.orEmpty() to remoteCommunity?.readableHandle.orEmpty()
                } else {
                    name to community.readableHandle
                }

            val preferredLanguageId =
                communityPreferredLanguageRepository.get(actualHandle)
            val defaultLanguageId = settingsRepository.currentSettings.value.defaultLanguageId

            updateState {
                it.copy(
                    communityId = communityId,
                    communityInfo = actualName,
                    currentLanguageId = preferredLanguageId ?: defaultLanguageId,
                )
            }
        }
    }

    private fun loadImageAndObtainUrl(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            updateState {
                it.copy(
                    url = url.orEmpty(),
                    loading = false,
                )
            }
        }
    }

    private fun loadImageAndAppendUrlInBody(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            if (url != null) {
                val newValue =
                    uiState.value.bodyValue.let {
                        it.copy(text = it.text + "\n![]($url)")
                    }
                updateState {
                    it.copy(
                        loading = false,
                        bodyValue = newValue,
                    )
                }
            } else {
                updateState {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun submit() {
        val currentState = uiState.value
        if (currentState.loading) {
            return
        }

        viewModelScope.launch {
            updateState {
                it.copy(
                    titleError = null,
                    urlError = null,
                    bodyError = null,
                )
            }
        }

        val communityId = currentState.communityId
        val title = currentState.title.trim()
        val url = currentState.url.takeIf { it.isNotEmpty() }?.trim()
        val body =
            currentState.bodyValue.text
                .takeIf { it.isNotBlank() }
                ?.trim()
        val nsfw = currentState.nsfw
        val languageId = currentState.currentLanguageId
        var valid = true
        if (title.isEmpty()) {
            viewModelScope.launch {
                updateState {
                    it.copy(titleError = ValidationError.MissingField)
                }
            }
            valid = false
        }
        if (!url.isNullOrEmpty() && !url.isValidUrl()) {
            viewModelScope.launch {
                updateState {
                    it.copy(urlError = ValidationError.InvalidField)
                }
            }
            valid = false
        }
        if (communityId == null) {
            viewModelScope.launch {
                updateState {
                    it.copy(communityError = ValidationError.MissingField)
                }
            }
            valid = false
        }

        if (!valid) {
            return
        }

        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                when {
                    editedPostId != 0L -> {
                        postRepository.edit(
                            postId = editedPostId,
                            title = title,
                            body = body,
                            url = url,
                            nsfw = nsfw,
                            languageId = languageId,
                            auth = auth,
                        )
                    }

                    communityId != null -> {
                        postRepository.create(
                            communityId = communityId,
                            title = title,
                            body = body,
                            url = url,
                            nsfw = nsfw,
                            languageId = languageId,
                            auth = auth,
                        )
                    }
                }
                if (draftId != 0L) {
                    deleteDraft()
                }
                emitEffect(CreatePostMviModel.Effect.Success)
            } catch (e: Throwable) {
                val message = e.message
                emitEffect(CreatePostMviModel.Effect.Failure(message))
            } finally {
                updateState { it.copy(loading = false) }
            }
        }
    }

    private fun saveDraft() {
        val currentState = uiState.value
        if (currentState.loading) {
            return
        }
        val communityId = currentState.communityId
        val title = currentState.title
        val url = currentState.url.takeIf { it.isNotEmpty() }?.trim()
        val body = currentState.bodyValue.text
        val nsfw = currentState.nsfw
        val languageId = currentState.currentLanguageId

        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value
            val community =
                communityId?.let {
                    communityRepository.get(auth = auth, id = communityId)
                }
            val draft =
                DraftModel(
                    id = draftId,
                    type = DraftType.Post,
                    body = body,
                    title = title,
                    url = url,
                    nsfw = nsfw,
                    communityId = communityId,
                    languageId = languageId,
                    date = epochMillis(),
                    reference = community?.name,
                )
            if (draftId == 0L) {
                draftRepository.create(
                    model = draft,
                    accountId = accountId,
                )
            } else {
                draftRepository.update(draft)
            }
            updateState { it.copy(loading = false) }
            emitEffect(CreatePostMviModel.Effect.DraftSaved)
        }
    }

    private suspend fun deleteDraft() {
        if (draftId != 0L) {
            draftRepository.delete(draftId)
            notificationCenter.send(NotificationCenterEvent.DraftDeleted)
        }
    }

    private fun autofillTitle() {
        val url = uiState.value.url.takeUnless { it.isBlank() } ?: return
        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            val metadata = siteRepository.getMetadata(url)
            updateState { it.copy(loading = false) }

            when {
                metadata == null -> emitEffect(CreatePostMviModel.Effect.AutoFillError)
                metadata.title.isEmpty() -> emitEffect(CreatePostMviModel.Effect.AutoFillEmpty)
                else -> updateState { it.copy(title = metadata.title) }
            }
        }
    }
}
