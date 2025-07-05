package com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import kotlinx.coroutines.launch

internal class UserTagDetailViewModel(private val tagId: Long, private val userTagRepository: UserTagRepository) :
    ViewModel(),
    MviModelDelegate<UserTagDetailMviModel.Intent, UserTagDetailMviModel.UiState, UserTagDetailMviModel.Effect>
    by DefaultMviModelDelegate(initialState = UserTagDetailMviModel.UiState()),
    UserTagDetailMviModel {
    init {
        viewModelScope.launch {
            if (uiState.value.initial) {
                val tag = userTagRepository.getById(tagId)
                updateState { it.copy(tag = tag) }
                refresh()
            }
        }
    }

    override fun reduce(intent: UserTagDetailMviModel.Intent) {
        when (intent) {
            UserTagDetailMviModel.Intent.Refresh -> viewModelScope.launch { refresh() }
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
        viewModelScope.launch {
            userTagRepository.removeMember(
                username = username,
                userTagId = tagId,
            )
            refresh()
        }
    }
}
