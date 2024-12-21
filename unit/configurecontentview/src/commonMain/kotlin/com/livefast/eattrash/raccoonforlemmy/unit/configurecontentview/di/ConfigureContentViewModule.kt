package com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.di

import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.ConfigureContentViewMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.ConfigureContentViewViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val configureContentViewModule =
    DI.Module("ConfigureContentViewModule") {
        bind<ConfigureContentViewMviModel> {
            provider {
                ConfigureContentViewViewModel(
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    accountRepository = instance(),
                    notificationCenter = instance(),
                    lemmyValueCache = instance(),
            )
        }
    }
}
