package com.github.diegoberaldin.raccoonforlemmy.domain_identity.di

import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.ApiConfigurationRepository
import org.koin.java.KoinJavaComponent.inject

actual fun getApiConfigurationRepository(): ApiConfigurationRepository {
    val res: ApiConfigurationRepository by inject(ApiConfigurationRepository::class.java)
    return res
}