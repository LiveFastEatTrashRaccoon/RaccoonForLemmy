package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.shouldUseV4
import kotlinx.coroutines.CancellationException

internal class DefaultAuthRepository(
    private val services: ServiceProvider,
    private val siteVersionDataSource: SiteVersionDataSource,
) : AuthRepository {
    override suspend fun login(username: String, password: String, totp2faToken: String?): Result<LoginResponse> = try {
        val data =
            LoginForm(
                username = username,
                password = password,
                totp2faToken = totp2faToken,
            )
        val result = if (siteVersionDataSource.shouldUseV4()) {
            services.v4.account.login(data)
        } else {
            services.v3.auth.login(data)
        }
        Result.success(result)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    override suspend fun logout() = try {
        val response =
            if (siteVersionDataSource.shouldUseV4()) {
                services.v4.account.logout()
            } else {
                services.v3.auth.logout()
            }
        require(response.success)
        Result.success(Unit)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}
