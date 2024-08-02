package com.livefast.eattrash.raccoonforlemmy.unit.manageban.di

import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanViewModel
import org.koin.dsl.module

val manageBanModule =
    module {
        factory<ManageBanMviModel> {
            ManageBanViewModel(
                identityRepository = get(),
                accountRepository = get(),
                siteRepository = get(),
                settingsRepository = get(),
                userRepository = get(),
                communityRepository = get(),
                blocklistRepository = get(),
                stopWordRepository = get(),
            )
        }
    }
