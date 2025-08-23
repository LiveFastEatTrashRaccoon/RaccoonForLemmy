package com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.ConfigureContentViewViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val configureContentViewModule =
    DI.Module("ConfigureContentViewModule") {
        bindViewModel {
            ConfigureContentViewViewModel(
                themeRepository = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
