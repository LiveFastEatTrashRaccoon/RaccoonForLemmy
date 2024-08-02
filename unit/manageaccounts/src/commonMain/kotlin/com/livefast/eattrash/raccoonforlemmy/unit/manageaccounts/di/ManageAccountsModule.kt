package com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di

import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsViewModel
import org.koin.dsl.module

val manageAccountsModule =
    module {
        factory<ManageAccountsMviModel> {
            ManageAccountsViewModel(
                accountRepository = get(),
                settingsRepository = get(),
                switchAccount = get(),
                logout = get(),
                deleteAccount = get(),
                notificationCenter = get(),
            )
        }
    }
