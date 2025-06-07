package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.di

import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val configureSwipeActionsModule =
    DI.Module("ConfigureSwipeActionsModule") {
        bind<ConfigureSwipeActionsMviModel> {
            provider {
                ConfigureSwipeActionsViewModel(
                    settingsRepository = instance(),
                    accountRepository = instance(),
                    notificationCenter = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
