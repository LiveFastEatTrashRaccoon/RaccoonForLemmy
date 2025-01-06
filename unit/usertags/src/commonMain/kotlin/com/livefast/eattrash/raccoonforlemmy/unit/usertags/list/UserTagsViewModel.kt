package com.livefast.eattrash.raccoonforlemmy.unit.usertags.list

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
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
) : DefaultMviModel<UserTagsMviModel.Intent, UserTagsMviModel.UiState, UserTagsMviModel.Effect>(
        initialState = UserTagsMviModel.UiState(),
    ),
    UserTagsMviModel {
    init {
        screenModelScope.launch {
            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: UserTagsMviModel.Intent) {
        when (intent) {
            UserTagsMviModel.Intent.Refresh -> screenModelScope.launch { refresh() }
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
        val tags =
            userTagRepository
                .getAll(accountId)
                .partition { it.isSpecial }
                .let { (specialTags, regularTags) ->
                    val sortedSpecial = specialTags.sortedBy { it.name }
                    val sortedRegular = regularTags.sortedBy { it.name }
                    sortedSpecial + sortedRegular
                }
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
        type: UserTagType,
    ) {
        screenModelScope.launch {
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
        screenModelScope.launch {
            userTagRepository.delete(id)
            userTagHelper.clear()
            refresh()
        }
    }
}

private val UserTagModel.sortKey: Int get() = if (isSpecial) 0 else 1
