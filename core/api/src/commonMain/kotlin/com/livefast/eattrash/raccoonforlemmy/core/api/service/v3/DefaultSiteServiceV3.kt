package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class DefaultSiteServiceV3(val baseUrl: String, val client: HttpClient) : SiteServiceV3 {
    override suspend fun get(authHeader: String?, auth: String?): GetSiteResponse = client.get("$baseUrl/v3/site") {
        header("Authorization", authHeader)
        parameter("auth", auth)
    }.body()

    override suspend fun block(authHeader: String?, form: BlockInstanceForm): BlockInstanceResponse =
        client.post("$baseUrl/v3/site/block") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
