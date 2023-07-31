package com.github.diegoberaldin.raccoonforlemmy.domain_identity.di

import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.ApiConfigurationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getApiConfigurationRepository(): ApiConfigurationRepository {
    return ApiConfigurationRepositoryHelper.repository
}

object ApiConfigurationRepositoryHelper : KoinComponent {
    val repository: ApiConfigurationRepository by inject()
}