package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

internal class DefaultSiteServiceV4(val baseUrl: String, val client: HttpClient) : SiteServiceV4 {
    override suspend fun get(authHeader: String?): GetSiteResponse = client.get("$baseUrl/v4/site") {
        header("Authorization", authHeader)
    }.body()
}
