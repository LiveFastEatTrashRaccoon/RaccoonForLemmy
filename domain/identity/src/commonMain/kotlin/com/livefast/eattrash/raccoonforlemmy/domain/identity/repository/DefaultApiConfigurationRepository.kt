package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class DefaultApiConfigurationRepository(
    private val serviceProvider: ServiceProvider,
    private val keyStore: TemporaryKeyStore,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : ApiConfigurationRepository {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        scope.launch {
            val instance =
                keyStore.get(KEY_LAST_INSTANCE, "")
                    .takeIf { it.isNotEmpty() } ?: serviceProvider.currentInstance
            changeInstance(instance)
        }
    }

    override val instance =
        channelFlow {
            while (isActive) {
                val value = serviceProvider.currentInstance
                trySend(value)
                delay(1_000)
            }
        }.distinctUntilChanged().stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "",
        )

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
