package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String,
        totp2faToken: String? = null,
    ): Result<LoginResponse>

    suspend fun logout(): Result<Unit>
}
