package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.di

import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val configureNavBarModule =
    DI.Module("ConfigureNavBarModule") {
        bind<ConfigureNavBarMviModel> {
            provider {
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
    }
