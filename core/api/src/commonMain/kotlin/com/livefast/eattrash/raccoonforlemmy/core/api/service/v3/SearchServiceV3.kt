package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveObjectResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType

interface SearchServiceV3 {
    suspend fun search(
        authHeader: String? = null,
        q: String,
        communityId: CommunityId? = null,
        communityName: String? = null,
        creatorId: PersonId? = null,
        type: SearchType? = null,
        sort: SortType? = null,
        listingType: ListingType? = null,
        page: Int? = null,
        limit: Int? = null,
        auth: String? = null,
    ): SearchResponse

    suspend fun resolveObject(authHeader: String? = null, q: String): ResolveObjectResponse
}
