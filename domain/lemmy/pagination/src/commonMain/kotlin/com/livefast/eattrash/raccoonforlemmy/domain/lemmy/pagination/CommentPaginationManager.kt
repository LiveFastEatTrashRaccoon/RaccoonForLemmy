package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel

interface CommentPaginationManager {
    val canFetchMore: Boolean

    fun reset(specification: CommentPaginationSpecification)

    suspend fun loadNextPage(): List<CommentModel>
}
