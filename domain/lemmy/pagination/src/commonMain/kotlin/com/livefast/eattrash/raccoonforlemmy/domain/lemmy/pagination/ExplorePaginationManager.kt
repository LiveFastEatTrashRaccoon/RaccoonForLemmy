package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult

interface ExplorePaginationManager {
    val canFetchMore: Boolean

    fun reset(specification: ExplorePaginationSpecification)

    suspend fun loadNextPage(): List<SearchResult>
}
