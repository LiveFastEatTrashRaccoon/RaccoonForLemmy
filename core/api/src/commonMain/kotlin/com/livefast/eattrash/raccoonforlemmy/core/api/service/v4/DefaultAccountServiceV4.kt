package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MyUserInfo
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class DefaultAccountServiceV4(val baseUrl: String, val client: HttpClient) : AccountServiceV4 {
    override suspend fun get(authHeader: String?): MyUserInfo = client.get("$baseUrl/v4/account") {
        header("Authorization", authHeader)
    }.body()

    override suspend fun login(form: LoginForm): LoginResponse = client.post("$baseUrl/v4/account/auth/login") {
        contentType(ContentType.Application.Json)
        setBody(form)
    }.body()

    override suspend fun logout(): SuccessResponse = client.get("$baseUrl/v4/account/auth/logout").body()
}
