package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getApiConfigurationRepository(): ApiConfigurationRepository {
    return ApiConfigurationRepositoryHelper.repository
}

object ApiConfigurationRepositoryHelper : KoinComponent {
    val repository: ApiConfigurationRepository by inject()
}
