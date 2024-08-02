package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItemType

interface ModlogRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getItems(
        auth: String? = null,
        communityId: Long?,
        limit: Int = DEFAULT_PAGE_SIZE,
        page: Int,
        type: ModlogItemType = ModlogItemType.All,
    ): List<ModlogItem>?
}
