package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository

internal class DefaultCreateSpecialTagsUseCase(
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
) : CreateSpecialTagsUseCase {
    override suspend fun invoke() {
        val accountId = accountRepository.getActive()?.id ?: return
        val allTags = userTagRepository.getAll(accountId)

        if (allTags.none { it.type == UserTagType.Admin }) {
            val model =
                UserTagModel(
                    name = "admin",
                    type = UserTagType.Admin,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
        if (allTags.none { it.type == UserTagType.Bot }) {
            val model =
                UserTagModel(
                    name = "bot",
                    type = UserTagType.Bot,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
        if (allTags.none { it.type == UserTagType.Me }) {
            val model =
                UserTagModel(
                    name = "me",
                    type = UserTagType.Me,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
        if (allTags.none { it.type == UserTagType.Me }) {
            val model =
                UserTagModel(
                    name = "me",
                    type = UserTagType.Me,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
        if (allTags.none { it.type == UserTagType.Moderator }) {
            val model =
                UserTagModel(
                    name = "mod",
                    type = UserTagType.Moderator,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
        if (allTags.none { it.type == UserTagType.OriginalPoster }) {
            val model =
                UserTagModel(
                    name = "op",
                    type = UserTagType.OriginalPoster,
                )
            userTagRepository.create(model = model, accountId = accountId)
        }
    }
}
