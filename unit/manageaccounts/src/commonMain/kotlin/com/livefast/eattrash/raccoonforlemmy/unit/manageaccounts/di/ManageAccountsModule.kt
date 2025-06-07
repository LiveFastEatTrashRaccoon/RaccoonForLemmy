package com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di

import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val manageAccountsModule =
    DI.Module("ManageAccountsModule") {
        bind<ManageAccountsMviModel> {
            provider {
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
    }
