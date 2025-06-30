package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val configureNavBarModule =
    DI.Module("ConfigureNavBarModule") {
        bindViewModel {
            ConfigureNavBarViewModel(
                accountRepository = instance(),
                identityRepository = instance(),
                bottomNavItemsRepository = instance(),
                settingsRepository = instance(),
                hapticFeedback = instance(),
                notificationCenter = instance(),
            )
        }
    }
