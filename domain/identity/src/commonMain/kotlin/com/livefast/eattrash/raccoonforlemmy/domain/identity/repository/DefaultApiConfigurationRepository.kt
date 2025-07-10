package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class DefaultApiConfigurationRepository(
    private val serviceProvider: ServiceProvider,
    private val keyStore: TemporaryKeyStore,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : ApiConfigurationRepository {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    private suspend fun getInitialInstance(): String =
        keyStore.get(KEY_LAST_INSTANCE, "").takeIf { it.isNotEmpty() } ?: serviceProvider.defaultInstance

    init {
        scope.launch {
            val initialValue = getInitialInstance()
            changeInstance(initialValue)
        }
    }

    override val instance = MutableStateFlow(serviceProvider.defaultInstance)

    override fun changeInstance(value: String) {
        scope.launch {
            serviceProvider.changeInstance(value)
            keyStore.save(KEY_LAST_INSTANCE, value)
        }
    }

    companion object {
        private const val KEY_LAST_INSTANCE = "lastInstance"
    }
}
