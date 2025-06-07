package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

sealed interface CommunityPaginationSpecification {
    data class Subscribed(val sortType: SortType = SortType.Active, val searchText: String = "") :
        CommunityPaginationSpecification

    data class Instance(val otherInstance: String? = null, val sortType: SortType = SortType.New) :
        CommunityPaginationSpecification
}
