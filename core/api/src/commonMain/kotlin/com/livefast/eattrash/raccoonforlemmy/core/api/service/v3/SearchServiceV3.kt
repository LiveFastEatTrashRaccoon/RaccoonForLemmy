package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveObjectResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Query

interface SearchServiceV3 {
    @GET("v3/search")
    suspend fun search(
        @Header("Authorization") authHeader: String? = null,
        @Query("q") q: String,
        @Query("community_id") communityId: CommunityId? = null,
        @Query("community_name") communityName: String? = null,
        @Query("creator_id") creatorId: PersonId? = null,
        @Query("type_") type: SearchType? = null,
        @Query("sort") sort: SortType? = null,
        @Query("listing_type") listingType: ListingType? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("auth") auth: String? = null,
    ): SearchResponse

    @GET("v3/resolve_object")
    suspend fun resolveObject(
        @Header("Authorization") authHeader: String? = null,
        @Query("q") q: String,
    ): ResolveObjectResponse
}
