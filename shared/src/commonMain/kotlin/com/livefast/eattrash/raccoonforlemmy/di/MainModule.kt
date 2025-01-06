package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.main.MainMviModel
import com.livefast.eattrash.raccoonforlemmy.main.MainViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

internal val mainModule =
    DI.Module("MainModule") {
        bind<MainMviModel> {
            provider {
                MainViewModel(
                    inboxCoordinator = instance(),
                    identityRepository = instance(),
                    settingRepository = instance(),
                    userRepository = instance(),
                    notificationChecker = instance(),
                    lemmyValueCache = instance(),
                    createSpecialTagsUseCase = instance(),
                )
            }
        }
    }
