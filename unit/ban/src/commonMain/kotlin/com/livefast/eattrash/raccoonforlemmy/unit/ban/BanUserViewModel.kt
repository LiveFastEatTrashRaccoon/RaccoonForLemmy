package com.livefast.eattrash.raccoonforlemmy.unit.ban

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import kotlinx.coroutines.launch

class BanUserViewModel(
    private val userId: Long,
    private val communityId: Long,
    private val newValue: Boolean,
    private val postId: Long,
    private val commentId: Long,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<BanUserMviModel.Intent, BanUserMviModel.UiState, BanUserMviModel.Effect>
    by DefaultMviModelDelegate(initialState = BanUserMviModel.UiState()),
    BanUserMviModel {
    init {
        viewModelScope.launch {
            updateState {
                it.copy(targetBanValue = newValue)
            }
        }
    }

    override fun reduce(intent: BanUserMviModel.Intent) {
        when (intent) {
            BanUserMviModel.Intent.IncrementDays -> incrementDays()
            BanUserMviModel.Intent.DecrementDays -> decrementDays()
            is BanUserMviModel.Intent.ChangePermanent ->
                viewModelScope.launch {
                    updateState { it.copy(permanent = intent.value) }
                }

            is BanUserMviModel.Intent.ChangeRemoveData ->
                viewModelScope.launch {
                    updateState { it.copy(removeData = intent.value) }
                }

            is BanUserMviModel.Intent.SetText ->
                viewModelScope.launch {
                    updateState { it.copy(text = intent.value) }
                }

            BanUserMviModel.Intent.Submit -> submit()
        }
    }

    private fun incrementDays() {
        viewModelScope.launch {
            val newValue = uiState.value.days + 1
            updateState { it.copy(days = newValue) }
        }
    }

    private fun decrementDays() {
        viewModelScope.launch {
            val newValue = (uiState.value.days - 1).coerceAtLeast(1)
            updateState { it.copy(days = newValue) }
        }
    }

    private fun submit() {
        val currentState = uiState.value
        if (currentState.loading) {
            return
        }
        val text = currentState.text
        val removeData = currentState.removeData.takeIf { newValue } ?: false
        val days = currentState.days.toLong().takeIf { newValue }

        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                val newUser =
                    communityRepository.banUser(
                        auth = auth,
                        userId = userId,
                        communityId = communityId,
                        ban = newValue,
                        expires = days,
                        reason = text,
                        removeData = removeData,
                    )
                if (newUser != null) {
                    postId.takeIf { it != 0L }?.also {
                        val evt =
                            NotificationCenterEvent.UserBannedPost(
                                postId = it,
                                user = newUser,
                            )
                        notificationCenter.send(evt)
                    }
                    commentId.takeIf { it != 0L }?.also {
                        val evt =
                            NotificationCenterEvent.UserBannedComment(
                                commentId = it,
                                user = newUser,
                            )
                        notificationCenter.send(evt)
                    }
                }
                emitEffect(BanUserMviModel.Effect.Success)
            } catch (e: Throwable) {
                val message = e.message
                emitEffect(BanUserMviModel.Effect.Failure(message))
            } finally {
                updateState { it.copy(loading = false) }
            }
        }
    }
}
