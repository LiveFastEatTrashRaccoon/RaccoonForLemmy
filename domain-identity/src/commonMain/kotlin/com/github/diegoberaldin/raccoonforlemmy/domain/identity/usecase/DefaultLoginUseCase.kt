package com.github.diegoberaldin.raccoonforlemmy.domain.identity.usecase

import com.github.diegoberaldin.raccoonforlemmy.core.utils.Log
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository

internal class DefaultLoginUseCase(
    private val authRepository: AuthRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
) : LoginUseCase {

    override suspend fun login(
        instance: String,
        username: String,
        password: String,
        totp2faToken: String?,
    ): Result<Unit> {
        val oldInstance = apiConfigurationRepository.getInstance()
        apiConfigurationRepository.changeInstance(instance)

        val response = authRepository.login(
            username = username,
            password = password,
            totp2faToken = totp2faToken,
        )
        return response.onFailure {
            Log.d("Login failure: ${it.message}")
        }.map {
            val auth = it.token
            if (auth == null) {
                apiConfigurationRepository.changeInstance(oldInstance)
            } else {
                identityRepository.storeToken(auth)
            }
        }
    }

    override suspend fun logout() {
        identityRepository.clearToken()
    }
}
