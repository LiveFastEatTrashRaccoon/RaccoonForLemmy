package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.LoginResponse

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String,
        totp2faToken: String? = null,
    ): Result<LoginResponse>
}

