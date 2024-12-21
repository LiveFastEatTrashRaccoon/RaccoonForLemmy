package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import org.kodein.di.instance

fun getAccountRepository(): AccountRepository {
    val res by RootDI.di.instance<AccountRepository>()
    return res
}

fun getSettingsRepository(): SettingsRepository {
    val res by RootDI.di.instance<SettingsRepository>()
    return res
}
