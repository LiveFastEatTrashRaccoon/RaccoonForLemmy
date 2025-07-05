package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val accountSettingsModule =
    DI.Module("AccountSettingsModule") {
        bindViewModel {
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
