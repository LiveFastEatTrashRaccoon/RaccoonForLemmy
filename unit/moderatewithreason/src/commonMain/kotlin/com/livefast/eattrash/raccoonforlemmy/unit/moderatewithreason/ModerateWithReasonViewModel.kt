package com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.launch

class ModerateWithReasonViewModel(
    private val actionId: Int,
    private val contentId: Long,
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
) : DefaultMviModel<ModerateWithReasonMviModel.Intent, ModerateWithReasonMviModel.UiState, ModerateWithReasonMviModel.Effect>(
        initialState = ModerateWithReasonMviModel.UiState(),
    ),
    ModerateWithReasonMviModel {
    init {
        screenModelScope.launch {
            updateState { it.copy(action = actionId.toModerateWithReasonAction()) }
        }
    }

    override fun reduce(intent: ModerateWithReasonMviModel.Intent) {
        when (intent) {
            is ModerateWithReasonMviModel.Intent.SetText -> {
                screenModelScope.launch {
                    updateState {
                        it.copy(text = intent.value)
                    }
                }
            }

            ModerateWithReasonMviModel.Intent.Submit -> submit()
        }
    }

    private fun submit() {
        if (uiState.value.loading) {
            return
        }
        val text = uiState.value.text

        screenModelScope.launch {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            try {
                when (uiState.value.action) {
                    is ModerateWithReasonAction.HideCommunity -> {
                        communityRepository.hide(
                            communityId = contentId,
                            reason = text,
                            auth = auth,
                            hidden = true,
                        )
                    }

                    is ModerateWithReasonAction.PurgeComment -> {
                        commentRepository.purge(
                            commentId = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.PurgeCommunity -> {
                        communityRepository.purge(
                            communityId = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.PurgePost -> {
                        postRepository.purge(
                            postId = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.PurgeUser -> {
                        userRepository.purge(
                            id = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.RemoveComment -> {
                        commentRepository.remove(
                            commentId = contentId,
                            reason = text,
                            removed = true,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.RemovePost -> {
                        postRepository.remove(
                            postId = contentId,
                            reason = text,
                            removed = true,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.ReportComment -> {
                        commentRepository.report(
                            commentId = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }

                    is ModerateWithReasonAction.ReportPost -> {
                        postRepository.report(
                            postId = contentId,
                            reason = text,
                            auth = auth,
                        )
                    }
                }
                emitEffect(ModerateWithReasonMviModel.Effect.Success)
            } catch (e: Throwable) {
                val message = e.message
                emitEffect(ModerateWithReasonMviModel.Effect.Failure(message))
            } finally {
                updateState { it.copy(loading = false) }
            }
        }
    }
}
