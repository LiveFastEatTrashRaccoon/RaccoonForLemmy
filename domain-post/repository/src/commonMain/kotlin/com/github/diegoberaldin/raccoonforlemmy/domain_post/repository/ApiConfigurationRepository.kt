package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider

class ApiConfigurationRepository(
    private val serviceProvider: ServiceProvider,
) {

    fun getInstance() = serviceProvider.currentInstance

    fun changeInstance(value: String) {
        serviceProvider.changeInstance(value)
    }
}