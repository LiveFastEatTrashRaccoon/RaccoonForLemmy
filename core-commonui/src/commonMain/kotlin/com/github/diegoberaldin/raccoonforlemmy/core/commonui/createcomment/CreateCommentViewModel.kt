package com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.MR.strings.message_missing_field
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreateCommentViewModel(
    private val postId: Int?,
    private val parentId: Int?,
    private val editedCommentId: Int?,
    private val mvi: DefaultMviModel<CreateCommentMviModel.Intent, CreateCommentMviModel.UiState, CreateCommentMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val commentRepository: CommentRepository,
    private val themeRepository: ThemeRepository,
) : ScreenModel,
    MviModel<CreateCommentMviModel.Intent, CreateCommentMviModel.UiState, CreateCommentMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        mvi.scope?.launch {
            themeRepository.postLayout.onEach { layout ->
                mvi.updateState { it.copy(postLayout = layout) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: CreateCommentMviModel.Intent) {
        when (intent) {
            is CreateCommentMviModel.Intent.SetText -> {
                mvi.updateState {
                    it.copy(text = intent.value)
                }
            }

            is CreateCommentMviModel.Intent.ChangeSection -> mvi.updateState {
                it.copy(section = intent.value)
            }

            CreateCommentMviModel.Intent.Send -> submit()
        }
    }

    private fun submit() {
        if (mvi.uiState.value.loading) {
            return
        }

        mvi.updateState {
            it.copy(
                textError = null,
            )
        }
        val text = uiState.value.text
        var valid = true
        if (text.isEmpty()) {
            mvi.updateState {
                it.copy(
                    textError = message_missing_field.desc(),
                )
            }
            valid = false
        }
        if (!valid) {
            return
        }

        mvi.updateState { it.copy(loading = true) }
        mvi.scope?.launch(Dispatchers.IO) {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                if (postId != null) {
                    commentRepository.create(
                        postId = postId,
                        parentId = parentId,
                        text = text,
                        auth = auth,
                    )
                } else if (editedCommentId != null) {
                    commentRepository.edit(
                        commentId = editedCommentId,
                        text = text,
                        auth = auth,
                    )
                }
                mvi.emitEffect(CreateCommentMviModel.Effect.Success)
            } catch (e: Throwable) {
                val message = e.message
                mvi.emitEffect(CreateCommentMviModel.Effect.Failure(message))
            } finally {
                mvi.updateState { it.copy(loading = false) }
            }
        }
    }
}