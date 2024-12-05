package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.preferences.provider")
internal actual class ProviderModule

actual fun getAppConfigStore(): AppConfigStore {
    val res by inject<AppConfigStore>(AppConfigStore::class.java)
    return res
}
