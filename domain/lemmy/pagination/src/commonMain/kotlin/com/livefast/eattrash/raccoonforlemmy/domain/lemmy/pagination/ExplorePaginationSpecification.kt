package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

data class ExplorePaginationSpecification(
    val resultType: SearchResultType = SearchResultType.Communities,
    val listingType: ListingType = ListingType.All,
    val sortType: SortType = SortType.Active,
    val includeNsfw: Boolean = true,
    val searchPostTitleOnly: Boolean = false,
    val restrictLocalUserSearch: Boolean = false,
    val otherInstance: String? = null,
    val query: String? = null,
)
