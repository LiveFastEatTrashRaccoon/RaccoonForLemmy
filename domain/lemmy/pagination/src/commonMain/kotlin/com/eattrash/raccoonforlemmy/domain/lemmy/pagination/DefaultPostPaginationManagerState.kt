package com.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel

internal data class DefaultPostPaginationManagerState(
    val specification: PostPaginationSpecification? = null,
    val currentPage: Int = 1,
    val pageCursor: String? = null,
    val history: List<PostModel> = emptyList(),
    val blockedDomains: List<String>? = null,
    val stopWords: List<String>? = null,
) : PostPaginationManagerState
