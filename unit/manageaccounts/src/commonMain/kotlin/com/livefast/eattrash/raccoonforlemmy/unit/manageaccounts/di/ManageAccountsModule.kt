package com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val manageAccountsModule =
    DI.Module("ManageAccountsModule") {
        bindViewModel {
            ManageAccountsViewModel(
                accountRepository = instance(),
                settingsRepository = instance(),
                switchAccount = instance(),
                logout = instance(),
                deleteAccount = instance(),
                notificationCenter = instance(),
            )
        }
    }
