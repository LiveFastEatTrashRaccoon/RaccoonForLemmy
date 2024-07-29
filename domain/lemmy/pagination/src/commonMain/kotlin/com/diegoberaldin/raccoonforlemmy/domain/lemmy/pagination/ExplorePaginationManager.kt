package com.diegoberaldin.raccoonforlemmy.domain.lemmy.pagination

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SearchResult

interface ExplorePaginationManager {
    val canFetchMore: Boolean

    fun reset(specification: ExplorePaginationSpecification)

    suspend fun loadNextPage(): List<SearchResult>
}
