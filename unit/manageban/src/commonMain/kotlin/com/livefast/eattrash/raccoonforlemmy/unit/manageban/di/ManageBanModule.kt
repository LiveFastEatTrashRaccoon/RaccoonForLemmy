package com.livefast.eattrash.raccoonforlemmy.unit.manageban.di

import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val manageBanModule =
    DI.Module("ManageBanModule") {
        bind<ManageBanMviModel> {
            provider {
                ManageBanViewModel(
                    identityRepository = instance(),
                    accountRepository = instance(),
                    siteRepository = instance(),
                    settingsRepository = instance(),
                    userRepository = instance(),
                    communityRepository = instance(),
                    blocklistRepository = instance(),
                    stopWordRepository = instance(),
                )
            }
        }
    }
