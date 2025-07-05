package com.livefast.eattrash.raccoonforlemmy.unit.usertags.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.isSpecial
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import kotlinx.coroutines.launch

internal class UserTagsViewModel(
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val userTagHelper: UserTagHelper,
) : ViewModel(),
    MviModelDelegate<UserTagsMviModel.Intent, UserTagsMviModel.UiState, UserTagsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = UserTagsMviModel.UiState()),
    UserTagsMviModel {
    init {
        viewModelScope.launch {
            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: UserTagsMviModel.Intent) {
        when (intent) {
            UserTagsMviModel.Intent.Refresh -> viewModelScope.launch { refresh() }
            is UserTagsMviModel.Intent.Add ->
                addTag(name = intent.name, color = intent.color)

            is UserTagsMviModel.Intent.Edit ->
                editTag(
                    id = intent.id,
                    name = intent.name,
                    color = intent.color,
                    type = intent.type,
                )

            is UserTagsMviModel.Intent.Delete -> removeTag(intent.id)
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val accountId = accountRepository.getActive()?.id ?: return
        updateState {
            it.copy(
                initial = initial,
                refreshing = !initial,
            )
        }
        val (specialTags, regularTags) =
            userTagRepository
                .getAll(accountId)
                .partition { it.isSpecial }
                .let { (specialTags, regularTags) ->
                    specialTags.sortedBy { it.name } to regularTags.sortedBy { it.name }
                }
        updateState {
            it.copy(
                specialTags = specialTags,
                regularTags = regularTags,
                initial = false,
                refreshing = false,
            )
        }
    }

    private fun addTag(name: String, color: Int?) {
        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            val tag = UserTagModel(name = name, color = color)
            userTagRepository.create(tag, accountId)
            userTagHelper.clear()
            refresh()
        }
    }

    private fun editTag(id: Long, name: String, color: Int?, type: UserTagType) {
        viewModelScope.launch {
            userTagRepository.update(
                id = id,
                name = name,
                color = color,
                type = type.toInt(),
            )
            userTagHelper.clear()
            refresh()
        }
    }

    private fun removeTag(id: Long) {
        viewModelScope.launch {
            userTagRepository.delete(id)
            userTagHelper.clear()
            refresh()
        }
    }
}

private val UserTagModel.sortKey: Int get() = if (isSpecial) 0 else 1
