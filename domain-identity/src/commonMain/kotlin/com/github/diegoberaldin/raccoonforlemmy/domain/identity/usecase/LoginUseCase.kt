package com.github.diegoberaldin.raccoonforlemmy.domain.identity.usecase

interface LoginUseCase {
    suspend fun login(
        instance: String,
        username: String,
        password: String,
        totp2faToken: String? = null,
    ): Result<Unit>

    suspend fun logout()
}
