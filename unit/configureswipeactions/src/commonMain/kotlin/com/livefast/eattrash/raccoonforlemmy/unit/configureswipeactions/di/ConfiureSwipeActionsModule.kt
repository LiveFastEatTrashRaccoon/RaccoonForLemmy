package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val configureSwipeActionsModule =
    DI.Module("ConfigureSwipeActionsModule") {
        bindViewModel {
            ConfigureSwipeActionsViewModel(
                settingsRepository = instance(),
                accountRepository = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
