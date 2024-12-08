package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.driver")
internal actual class DriverModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.key")
internal actual class KeyModule

actual fun getAccountRepository(): AccountRepository {
    val res: AccountRepository by KoinJavaComponent.inject(AccountRepository::class.java)
    return res
}

actual fun getSettingsRepository(): SettingsRepository {
    val res: SettingsRepository by KoinJavaComponent.inject(SettingsRepository::class.java)
    return res
}
