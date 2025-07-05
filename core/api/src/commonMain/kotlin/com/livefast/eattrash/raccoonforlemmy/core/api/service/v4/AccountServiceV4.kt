package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MyUserInfo
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse

interface AccountServiceV4 {
    suspend fun get(authHeader: String? = null): MyUserInfo

    suspend fun login(form: LoginForm): LoginResponse

    suspend fun logout(): SuccessResponse
}
