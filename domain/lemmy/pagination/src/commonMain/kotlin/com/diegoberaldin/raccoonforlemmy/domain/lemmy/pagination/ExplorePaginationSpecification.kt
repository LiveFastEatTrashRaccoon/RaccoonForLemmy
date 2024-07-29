package com.diegoberaldin.raccoonforlemmy.domain.lemmy.pagination

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

data class ExplorePaginationSpecification(
    val resultType: SearchResultType = SearchResultType.Communities,
    val listingType: ListingType = ListingType.All,
    val sortType: SortType = SortType.Active,
    val includeNsfw: Boolean = true,
    val searchPostTitleOnly: Boolean = false,
    val otherInstance: String? = null,
    val query: String? = null,
)
