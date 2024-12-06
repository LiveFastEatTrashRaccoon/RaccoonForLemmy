package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.core.annotation.Module

@Module
internal expect class ProviderModule()

expect fun getAppConfigStore(): AppConfigStore
