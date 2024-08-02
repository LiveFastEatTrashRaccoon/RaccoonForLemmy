package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.di

import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsViewModel
import org.koin.dsl.module

val configureSwipeActionsModule =
    module {
        factory<ConfigureSwipeActionsMviModel> {
            ConfigureSwipeActionsViewModel(
                settingsRepository = get(),
                accountRepository = get(),
                notificationCenter = get(),
                identityRepository = get(),
                siteRepository = get(),
                lemmyValueCache = get(),
            )
        }
    }
