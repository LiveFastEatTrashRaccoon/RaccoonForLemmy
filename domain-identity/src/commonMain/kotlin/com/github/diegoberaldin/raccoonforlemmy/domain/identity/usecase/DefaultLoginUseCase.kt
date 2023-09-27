package com.github.diegoberaldin.raccoonforlemmy.domain.identity.usecase

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.AccountModel
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.github.diegoberaldin.raccoonforlemmy.core.utils.Log
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository

internal class DefaultLoginUseCase(
    private val authRepository: AuthRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
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

                val account = AccountModel(
                    username = username,
                    instance = instance,
                    jwt = auth
                )
                val id = accountRepository.createAccount(account)
                val oldAccountId = accountRepository.getActive()?.id
                if (oldAccountId != null) {
                    accountRepository.setActive(oldAccountId, false)
                }
                accountRepository.setActive(id, true)
            }
        }
    }

    override suspend fun logout() {
        identityRepository.clearToken()
        val oldAccountId = accountRepository.getActive()?.id
        if (oldAccountId != null) {
            accountRepository.setActive(oldAccountId, false)
        }
    }
}
