package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.Site
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.SiteServiceV4
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient

interface SiteServiceV3 {
    @GET("v3/site")
    suspend fun get(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
    ): GetSiteResponse

    @POST("v3/site/block")
    @Headers("Content-Type: application/json")
    suspend fun block(
        @Header("Authorization") authHeader: String? = null,
        @Body form: BlockInstanceForm,
    ): BlockInstanceResponse
}

internal class DefaultSiteServiceV3(val baseUrl: String, val client: HttpClient) : SiteServiceV3 {
    override suspend fun get(authHeader: String?, auth: String?): GetSiteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun block(authHeader: String?, form: BlockInstanceForm): BlockInstanceResponse {
        TODO("Not yet implemented")
    }
}
