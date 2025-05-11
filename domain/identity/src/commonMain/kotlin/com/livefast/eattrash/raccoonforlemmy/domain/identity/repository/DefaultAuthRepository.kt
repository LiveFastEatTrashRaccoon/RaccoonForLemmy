package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultAuthRepository(
    private val services: ServiceProvider,
) : AuthRepository {
    override suspend fun login(
        username: String,
        password: String,
        totp2faToken: String?,
    ): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    LoginForm(
                        username = username,
                        password = password,
                        totp2faToken = totp2faToken,
                    )
                services.v3.auth.login(data)
            }
        }
}
