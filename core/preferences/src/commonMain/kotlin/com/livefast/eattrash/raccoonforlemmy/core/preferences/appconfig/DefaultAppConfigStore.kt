package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class DefaultAppConfigStore(
    @Named("local") private val localDataSource: AppConfigDataSource,
    @Named("remote") private val remoteDataSource: AppConfigDataSource,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AppConfigStore {
    override val appConfig = MutableStateFlow(AppConfig())
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun initialize() {
        scope.launch {
            val localValue = localDataSource.get()
            appConfig.update { localValue }

            val remoteValue = remoteDataSource.get()
            localDataSource.update(remoteValue)
            appConfig.update { remoteValue }
        }
    }
}
