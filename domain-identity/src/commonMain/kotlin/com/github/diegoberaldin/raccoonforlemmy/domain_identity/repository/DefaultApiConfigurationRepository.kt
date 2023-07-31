package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive

internal class DefaultApiConfigurationRepository(
    private val keyStore: TemporaryKeyStore,
    private val serviceProvider: ServiceProvider,
) : ApiConfigurationRepository {

    override val instance: Flow<String> = channelFlow {
        while (isActive) {
            val value = getInstance()
            trySend(value)
            delay(1000)
        }
    }.distinctUntilChanged()

    override fun getInstance() = serviceProvider.currentInstance

    override fun changeInstance(value: String) {
        serviceProvider.changeInstance(value)
        keyStore.save(KeyStoreKeys.LastIntance, value)
    }
}