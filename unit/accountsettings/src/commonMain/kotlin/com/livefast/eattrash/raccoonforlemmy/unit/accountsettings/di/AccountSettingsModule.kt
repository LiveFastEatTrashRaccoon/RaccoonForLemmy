package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.di

import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsViewModel
import org.koin.dsl.module

val accountSettingsModule =
    module {
        factory<AccountSettingsMviModel> {
            AccountSettingsViewModel(
                siteRepository = get(),
                identityRepository = get(),
                mediaRepository = get(),
                getSortTypesUseCase = get(),
                notificationCenter = get(),
            )
        }
    }
