package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.di

import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val accountSettingsModule =
    DI.Module("AccountSettingsModule") {
        bind<AccountSettingsMviModel> {
            provider {
                AccountSettingsViewModel(
                    siteRepository = instance(),
                    identityRepository = instance(),
                    mediaRepository = instance(),
                    userRepository = instance(),
                    getSortTypesUseCase = instance(),
                    logoutUseCase = instance(),
                    notificationCenter = instance(),
                )
            }
        }
    }
