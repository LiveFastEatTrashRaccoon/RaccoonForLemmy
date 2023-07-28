package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.LoginForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.LoginResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.AuthService

internal class DefaultAuthRepository(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun login(
        username: String,
        password: String,
        totp2faToken: String?,
    ): Result<LoginResponse> = runCatching {
        val data = LoginForm(
            username = username,
            password = password,
            totp2faToken = totp2faToken,
        )
        val response = authService.login(data)
        if (!response.isSuccessful) {
            // TODO: better API error handling
            val error = response.errorBody().toString()
            throw Exception(error)
        }
        response.body() ?: throw Exception("No reponse from login endpoint")
    }
}