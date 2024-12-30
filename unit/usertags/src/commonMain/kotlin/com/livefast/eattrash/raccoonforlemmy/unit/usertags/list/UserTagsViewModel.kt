package com.livefast.eattrash.raccoonforlemmy.unit.usertags.list

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import kotlinx.coroutines.launch

internal class UserTagsViewModel(
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val userTagHelper: UserTagHelper,
) : DefaultMviModel<UserTagsMviModel.Intent, UserTagsMviModel.UiState, UserTagsMviModel.Effect>(
        initialState = UserTagsMviModel.UiState(),
    ),
    UserTagsMviModel {
    init {
        screenModelScope.launch {
            if (uiState.value.initial) {
                refresh()
            }
        }
    }

    override fun reduce(intent: UserTagsMviModel.Intent) {
        when (intent) {
            UserTagsMviModel.Intent.Refresh -> screenModelScope.launch { refresh() }
            is UserTagsMviModel.Intent.Add ->
                addTag(name = intent.name, color = intent.color)

            is UserTagsMviModel.Intent.Edit ->
                editTag(id = intent.id, name = intent.name, color = intent.color)

            is UserTagsMviModel.Intent.Delete -> removeTag(intent.id)
        }
    }

    private suspend fun refresh() {
        val accountId = accountRepository.getActive()?.id ?: return
        val tags = userTagRepository.getAll(accountId)
        updateState {
            it.copy(
                tags = tags,
                initial = false,
                refreshing = false,
            )
        }
    }

    private fun addTag(
        name: String,
        color: Int?,
    ) {
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            val tag = UserTagModel(name = name, color = color)
            userTagRepository.create(tag, accountId)
            userTagHelper.clear()
            refresh()
        }
    }

    private fun editTag(
        id: Long,
        name: String,
        color: Int?,
    ) {
        screenModelScope.launch {
            userTagRepository.update(
                id = id,
                name = name,
                color = color,
            )
            userTagHelper.clear()
            refresh()
        }
    }

    private fun removeTag(id: Long) {
        screenModelScope.launch {
            userTagRepository.delete(id)
            userTagHelper.clear()
            refresh()
        }
    }
}
