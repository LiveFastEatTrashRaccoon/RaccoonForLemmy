package com.livefast.eattrash.raccoonforlemmy.unit.manageban.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val manageBanModule =
    DI.Module("ManageBanModule") {
        bindViewModel {
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
