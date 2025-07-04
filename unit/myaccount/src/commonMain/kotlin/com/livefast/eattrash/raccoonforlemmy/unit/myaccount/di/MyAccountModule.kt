package com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val myAccountModule =
    DI.Module("MyAccountModule") {
        bindViewModel {
            ProfileLoggedViewModel(
                identityRepository = instance(),
                apiConfigurationRepository = instance(),
                postPaginationManager = instance(),
                commentPaginationManager = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                themeRepository = instance(),
                settingsRepository = instance(),
                shareHelper = instance(),
                notificationCenter = instance(),
                hapticFeedback = instance(),
                postNavigationManager = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
