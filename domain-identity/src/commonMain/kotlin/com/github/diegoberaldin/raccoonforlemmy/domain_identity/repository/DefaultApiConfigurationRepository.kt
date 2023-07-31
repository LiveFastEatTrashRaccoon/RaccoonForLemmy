package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore

internal class DefaultApiConfigurationRepository(
    private val keyStore: TemporaryKeyStore,
    private val serviceProvider: ServiceProvider,
) : ApiConfigurationRepository {

    override fun getInstance() = serviceProvider.currentInstance

    override fun changeInstance(value: String) {
        serviceProvider.changeInstance(value)
        keyStore.save(KeyStoreKeys.LastIntance, value)
    }
}