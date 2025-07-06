package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetModlogResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ModlogActionType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

internal class DefaultModlogServiceV3(val baseUrl: String, val client: HttpClient) : ModlogServiceV3 {
    override suspend fun getItems(
        authHeader: String?,
        auth: String?,
        page: Int?,
        communityId: CommunityId?,
        limit: Int?,
        modId: PersonId?,
        otherId: PersonId?,
        type: ModlogActionType?,
    ): GetModlogResponse = client.get("$baseUrl/v3/modlog") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("page", page)
        parameter("community_id", communityId)
        parameter("limit", limit)
        parameter("mod_person_id", modId)
        parameter("other_person_id", otherId)
        parameter("type_", type)
    }.body()
}
