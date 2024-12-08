package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.driver")
internal actual class DriverModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.key")
internal actual class KeyModule

actual fun getAccountRepository(): AccountRepository = PersistenceDiHelper.accountRepository

actual fun getSettingsRepository(): SettingsRepository = PersistenceDiHelper.settingsRepository

internal object PersistenceDiHelper : KoinComponent {
    val accountRepository: AccountRepository by inject()
    val settingsRepository: SettingsRepository by inject()
}
