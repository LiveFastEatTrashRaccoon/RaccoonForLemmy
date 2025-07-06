package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class DefaultAuthServiceV3(val baseUrl: String, val client: HttpClient) : AuthServiceV3 {
    override suspend fun login(form: LoginForm): LoginResponse = client.post("$baseUrl/v3/user/login") {
        contentType(ContentType.Application.Json)
        setBody(form)
    }.body()

    override suspend fun logout(): SuccessResponse = client.post("$baseUrl/v3/user/logout").body()
}
