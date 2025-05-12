package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MyUserInfo
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header

interface AccountServiceV4 {
    @GET("v4/account")
    suspend fun get(
        @Header("Authorization") authHeader: String? = null,
    ): MyUserInfo
}
