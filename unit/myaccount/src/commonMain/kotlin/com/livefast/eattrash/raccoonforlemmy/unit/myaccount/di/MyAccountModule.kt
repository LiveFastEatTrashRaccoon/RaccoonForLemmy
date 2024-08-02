package com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di

import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedViewModel
import org.koin.dsl.module

val myAccountModule =
    module {
        factory<ProfileLoggedMviModel> {
            ProfileLoggedViewModel(
                identityRepository = get(),
                apiConfigurationRepository = get(),
                postRepository = get(),
                commentRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                shareHelper = get(),
                notificationCenter = get(),
                hapticFeedback = get(),
                postPaginationManager = get(),
                commentPaginationManager = get(),
                postNavigationManager = get(),
                lemmyValueCache = get(),
            )
        }
    }
