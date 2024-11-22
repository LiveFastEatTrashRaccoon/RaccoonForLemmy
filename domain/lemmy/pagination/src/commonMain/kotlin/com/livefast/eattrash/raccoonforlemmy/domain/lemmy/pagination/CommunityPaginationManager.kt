package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel

interface CommunityPaginationManager {
    val canFetchMore: Boolean
    val history: List<CommunityModel>

    fun reset(specification: CommunityPaginationSpecification)

    suspend fun loadNextPage(): List<CommunityModel>

    suspend fun fetchAll(): List<CommunityModel>
}
