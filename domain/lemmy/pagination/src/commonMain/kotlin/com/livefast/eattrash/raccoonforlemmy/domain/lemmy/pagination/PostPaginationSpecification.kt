package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

sealed interface PostPaginationSpecification {
    data class Listing(
        val listingType: ListingType = ListingType.All,
        val sortType: SortType = SortType.Active,
        val includeNsfw: Boolean = true,
    ) : PostPaginationSpecification

    data class MultiCommunity(
        val communityIds: List<Long>,
        val sortType: SortType = SortType.Active,
        val includeNsfw: Boolean = true,
    ) : PostPaginationSpecification

    data class Community(
        val id: Long? = null,
        val name: String? = null,
        val otherInstance: String? = null,
        val query: String? = null,
        val sortType: SortType = SortType.Active,
        val includeNsfw: Boolean = true,
    ) : PostPaginationSpecification

    data class User(
        val id: Long? = null,
        val name: String? = null,
        val otherInstance: String? = null,
        val sortType: SortType = SortType.New,
        val includeNsfw: Boolean = true,
        val includeDeleted: Boolean = false,
    ) : PostPaginationSpecification

    data class Votes(val liked: Boolean = true, val sortType: SortType = SortType.New) : PostPaginationSpecification

    data class Saved(val sortType: SortType = SortType.Active) : PostPaginationSpecification

    data class Hidden(val sortType: SortType = SortType.Active) : PostPaginationSpecification
}
