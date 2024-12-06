package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.preferences.provider")
internal actual class ProviderModule

actual fun getAppConfigStore(): AppConfigStore = AppConfigDiHelper.appConfigStore

internal object AppConfigDiHelper : KoinComponent {
    val appConfigStore: AppConfigStore by inject()
}
