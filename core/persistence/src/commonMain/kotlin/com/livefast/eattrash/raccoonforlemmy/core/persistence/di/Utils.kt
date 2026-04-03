package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import org.kodein.di.instance

fun getAccountRepository(): AccountRepository {
    val res by RootDI.di.instance<AccountRepository>()
    return res
}

@Composable
fun rememberAccountRepository(): AccountRepository = remember { getAccountRepository() }

fun getSettingsRepository(): SettingsRepository {
    val res by RootDI.di.instance<SettingsRepository>()
    return res
}

@Composable
fun rememberSettingsRepository(): SettingsRepository = remember { getSettingsRepository() }
