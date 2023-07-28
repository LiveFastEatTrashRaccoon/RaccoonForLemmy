package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider

internal class DefaultApiConfigurationRepository(
    private val serviceProvider: ServiceProvider,
) : ApiConfigurationRepository {

    override fun getInstance() = serviceProvider.currentInstance

    override fun changeInstance(value: String) {
        serviceProvider.changeInstance(value)
    }
}