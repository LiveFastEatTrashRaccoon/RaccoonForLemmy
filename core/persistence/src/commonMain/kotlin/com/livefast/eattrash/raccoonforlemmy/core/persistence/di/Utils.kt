package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository

@org.koin.core.annotation.Module
internal expect class DriverModule()

@org.koin.core.annotation.Module
internal expect class KeyModule()

expect fun getAccountRepository(): AccountRepository

expect fun getSettingsRepository(): SettingsRepository
