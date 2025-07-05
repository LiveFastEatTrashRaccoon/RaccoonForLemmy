package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MyUserInfo
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.HttpClient

interface AccountServiceV4 {
    @GET("v4/account")
    suspend fun get(@Header("Authorization") authHeader: String? = null): MyUserInfo

    @POST("v4/account/auth/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body form: LoginForm): LoginResponse

    @GET("v4/account/auth/logout")
    suspend fun logout(): SuccessResponse
}

internal class DefaultAccountServiceV4(val baseUrl: String, val client: HttpClient) : AccountServiceV4 {
    override suspend fun get(authHeader: String?): MyUserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun login(form: LoginForm): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): SuccessResponse {
        TODO("Not yet implemented")
    }
}
