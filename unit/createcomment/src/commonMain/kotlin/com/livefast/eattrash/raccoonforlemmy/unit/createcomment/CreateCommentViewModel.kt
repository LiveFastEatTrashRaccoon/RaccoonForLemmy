package com.livefast.eattrash.raccoonforlemmy.unit.createcomment

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
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreateCommentViewModel(
    private val postId: Long,
    private val parentId: Long,
    private val editedCommentId: Long,
    private val draftId: Long,
    private val identityRepository: IdentityRepository,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationCenter: NotificationCenter,
    private val itemCache: LemmyItemCache,
    private val accountRepository: AccountRepository,
    private val draftRepository: DraftRepository,
    private val communityPreferredLanguageRepository: CommunityPreferredLanguageRepository,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<CreateCommentMviModel.Intent, CreateCommentMviModel.UiState, CreateCommentMviModel.Effect>
    by DefaultMviModelDelegate(initialState = CreateCommentMviModel.UiState()),
    CreateCommentMviModel {
    init {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val originalPostFromCache = postId?.let { itemCache.getPost(it) }
            val originalCommentFromCache = parentId?.let { itemCache.getComment(it) }
            val editedComment = editedCommentId?.let { itemCache.getComment(it) }
            val originalPost =
                if (originalPostFromCache != null && originalPostFromCache.title.isEmpty()) {
                    postRepository.get(originalPostFromCache.id) ?: originalPostFromCache
                } else {
                    originalPostFromCache
                }
            val originalComment =
                if (originalCommentFromCache != null && originalCommentFromCache.text.isNullOrEmpty()) {
                    commentRepository.getBy(
                        id = originalCommentFromCache.id,
                        auth = auth,
                    ) ?: originalCommentFromCache
                } else {
                    originalCommentFromCache
                }
            val communityHandle = originalPost?.community?.readableHandle.orEmpty()
            val preferredLanguageId = communityPreferredLanguageRepository.get(communityHandle)
            updateState {
                it.copy(
                    originalPost = originalPost,
                    originalComment = originalComment,
                    editedComment = editedComment,
                )
            }

            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(downVoteEnabled = value)
                    }
                }.launchIn(this)

            if (uiState.value.currentUser.isEmpty()) {
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
                            currentLanguageId = preferredLanguageId ?: settings.defaultLanguageId,
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun reduce(intent: CreateCommentMviModel.Intent) {
        when (intent) {
            is CreateCommentMviModel.Intent.ChangeSection -> {
                viewModelScope.launch {
                    updateState { it.copy(section = intent.value) }
                }
            }

            is CreateCommentMviModel.Intent.ImageSelected -> {
                loadImageAndAppendUrlInBody(intent.value)
            }

            is CreateCommentMviModel.Intent.ChangeLanguage -> {
                viewModelScope.launch {
                    updateState { it.copy(currentLanguageId = intent.value) }
                }
            }

            is CreateCommentMviModel.Intent.ChangeTextValue -> {
                viewModelScope.launch {
                    updateState { it.copy(textValue = intent.value) }
                }
            }

            is CreateCommentMviModel.Intent.Send -> submit()
            is CreateCommentMviModel.Intent.SaveDraft -> saveDraft()
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
                    textError = null,
                )
            }
        }
        val text = currentState.textValue.text.trim()
        val languageId = currentState.currentLanguageId

        var valid = true
        if (text.isEmpty()) {
            viewModelScope.launch {
                updateState {
                    it.copy(
                        textError = ValidationError.MissingField,
                    )
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
                    editedCommentId != 0L -> {
                        commentRepository.edit(
                            commentId = editedCommentId,
                            text = text,
                            languageId = languageId,
                            auth = auth,
                        )
                    }

                    postId != 0L -> {
                        commentRepository.create(
                            postId = postId,
                            parentId = parentId,
                            text = text,
                            languageId = languageId,
                            auth = auth,
                        )
                    }
                }
                // the comment count has changed, emits update
                emitPostUpdateNotification()
                if (draftId != 0L) {
                    deleteDraft()
                }
                emitEffect(CreateCommentMviModel.Effect.Success(new = editedCommentId == 0L))
            } catch (e: Throwable) {
                val message = e.message
                emitEffect(CreateCommentMviModel.Effect.Failure(message))
            } finally {
                updateState { it.copy(loading = false) }
            }
        }
    }

    private suspend fun emitPostUpdateNotification() {
        val postId = postId.takeIf { it != 0L } ?: return
        val auth = identityRepository.authToken.value
        val newPost = postRepository.get(postId, auth)
        if (newPost != null) {
            notificationCenter.send(NotificationCenterEvent.PostUpdated(newPost))
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
                    uiState.value.textValue.let {
                        it.copy(text = it.text + "\n![]($url)")
                    }
                updateState {
                    it.copy(
                        textValue = newValue,
                        loading = false,
                    )
                }
            } else {
                updateState {
                    it.copy(
                        loading = false,
                    )
                }
            }
        }
    }

    private fun saveDraft() {
        val currentState = uiState.value
        if (currentState.loading) {
            return
        }
        val body = currentState.textValue.text
        val languageId = currentState.currentLanguageId

        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            updateState { it.copy(loading = true) }
            val draft =
                DraftModel(
                    id = draftId,
                    type = DraftType.Comment,
                    body = body,
                    postId = postId,
                    parentId = parentId,
                    languageId = languageId,
                    date = epochMillis(),
                    reference =
                    if (currentState.originalComment != null) {
                        currentState.originalComment.text
                    } else {
                        currentState.originalPost?.title
                    },
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
            emitEffect(CreateCommentMviModel.Effect.DraftSaved)
        }
    }

    private suspend fun deleteDraft() {
        if (draftId != 0L) {
            draftRepository.delete(draftId)
        }
        notificationCenter.send(NotificationCenterEvent.DraftDeleted)
    }
}
