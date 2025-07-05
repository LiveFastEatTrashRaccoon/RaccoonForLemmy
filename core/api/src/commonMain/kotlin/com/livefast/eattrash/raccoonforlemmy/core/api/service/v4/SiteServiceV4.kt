package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import io.ktor.client.HttpClient

interface SiteServiceV4 {
    @GET("v4/site")
    suspend fun get(@Header("Authorization") authHeader: String? = null): GetSiteResponse
}

internal class DefaultSiteServiceV4(val baseUrl: String, val client: HttpClient) : SiteServiceV4 {
    override suspend fun get(authHeader: String?): GetSiteResponse {
        TODO("Not yet implemented")
    }
}
