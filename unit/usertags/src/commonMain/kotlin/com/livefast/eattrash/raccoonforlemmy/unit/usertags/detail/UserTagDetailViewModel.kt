package com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import kotlinx.coroutines.launch

internal class UserTagDetailViewModel(private val tagId: Long, private val userTagRepository: UserTagRepository) :
    DefaultMviModel<UserTagDetailMviModel.Intent, UserTagDetailMviModel.UiState, UserTagDetailMviModel.Effect>(
        initialState = UserTagDetailMviModel.UiState(),
    ),
    UserTagDetailMviModel {
    init {
        screenModelScope.launch {
            if (uiState.value.initial) {
                val tag = userTagRepository.getById(tagId)
                updateState { it.copy(tag = tag) }
                refresh()
            }
        }
    }

    override fun reduce(intent: UserTagDetailMviModel.Intent) {
        when (intent) {
            UserTagDetailMviModel.Intent.Refresh -> screenModelScope.launch { refresh() }
            is UserTagDetailMviModel.Intent.Remove -> removeUser(intent.username)
        }
    }

    private suspend fun refresh() {
        val users = userTagRepository.getMembers(tagId)
        updateState {
            it.copy(
                users = users,
                initial = false,
            )
        }
    }

    private fun removeUser(username: String) {
        screenModelScope.launch {
            userTagRepository.removeMember(
                username = username,
                userTagId = tagId,
            )
            refresh()
        }
    }
}
