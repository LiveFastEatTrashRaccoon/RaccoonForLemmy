package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.cache.LruCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle

internal class DefaultUserTagHelper(
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val cache: LruCache<String, List<UserTagModel>> = LruCache(100),
) : UserTagHelper {
    override suspend fun UserModel?.withTags(): UserModel? = this?.let { user ->
        val handle = user.readableHandle
        val cachedValues = cache.get(handle)
        if (cachedValues != null) {
            user.with(tags = cachedValues)
        } else {
            val accountId = accountRepository.getActive()?.id ?: return@let user
            val tags = userTagRepository.getTags(username = handle, accountId = accountId)
            cache.put(handle, tags)
            user.with(tags = tags)
        }
    }

    private fun UserModel.with(tags: List<UserTagModel>): UserModel = copy(
        tags =
        tags.map {
            UserTagModel(
                name = it.name,
                color = it.color,
                type = it.type,
            )
        },
    )

    override suspend fun clear() {
        cache.clear()
    }
}
