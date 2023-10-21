package com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive

internal class DefaultApiConfigurationRepository(
    private val serviceProvider: ServiceProvider,
) : ApiConfigurationRepository {

    private val scope = CoroutineScope(SupervisorJob())

    override val instance = channelFlow {
        while (isActive) {
            val value = serviceProvider.currentInstance
            trySend(value)
            delay(1000)
        }
    }.distinctUntilChanged().stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = "",
    )

    override fun changeInstance(value: String) {
        serviceProvider.changeInstance(value)
    }
}
