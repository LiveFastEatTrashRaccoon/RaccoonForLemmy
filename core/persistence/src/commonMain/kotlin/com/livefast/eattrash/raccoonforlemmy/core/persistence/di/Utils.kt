package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import org.koin.core.module.Module

internal expect val persistenceInnerModule: Module

expect fun getAccountRepository(): AccountRepository

expect fun getSettingsRepository(): SettingsRepository
