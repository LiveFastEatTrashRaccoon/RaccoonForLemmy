package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveObjectResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

internal class DefaultSearchServiceV3(val baseUrl: String, val client: HttpClient) : SearchServiceV3 {
    override suspend fun search(
        authHeader: String?,
        q: String,
        communityId: CommunityId?,
        communityName: String?,
        creatorId: PersonId?,
        type: SearchType?,
        sort: SortType?,
        listingType: ListingType?,
        page: Int?,
        limit: Int?,
        auth: String?,
    ): SearchResponse = client.get("$baseUrl/v3/search") {
        header("Authorization", authHeader)
        parameter("q", q)
        parameter("community_id", communityId)
        parameter("community_name", communityName)
        parameter("creator_id", creatorId)
        parameter("type_", type)
        parameter("sort", sort)
        parameter("listing_type", listingType)
        parameter("page", page)
        parameter("limit", limit)
        parameter("auth", auth)
    }.body()

    override suspend fun resolveObject(authHeader: String?, q: String): ResolveObjectResponse =
        client.get("$baseUrl/v3/resolve_object") {
            header("Authorization", authHeader)
            parameter("q", q)
        }.body()
}
