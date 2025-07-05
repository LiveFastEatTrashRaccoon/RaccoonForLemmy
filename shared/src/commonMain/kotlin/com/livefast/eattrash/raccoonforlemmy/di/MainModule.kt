package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.main.MainViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal val mainModule =
    DI.Module("MainModule") {
        bindViewModel {
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
