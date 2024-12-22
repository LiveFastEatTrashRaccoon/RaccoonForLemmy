package com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di

import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val myAccountModule =
    DI.Module("MyAccountModule") {
        bind<ProfileLoggedMviModel> {
            provider {
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
}
