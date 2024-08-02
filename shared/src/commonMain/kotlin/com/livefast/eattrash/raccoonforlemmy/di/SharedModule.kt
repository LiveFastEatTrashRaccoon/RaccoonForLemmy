package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.MainMviModel
import com.livefast.eattrash.raccoonforlemmy.MainViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.impl.DefaultDetailOpener
import org.koin.dsl.module

internal val internalSharedModule =
    module {
        factory<MainMviModel> {
            MainViewModel(
                inboxCoordinator = get(),
                identityRepository = get(),
                settingRepository = get(),
                userRepository = get(),
                notificationChecker = get(),
                lemmyValueCache = get(),
            )
        }
        single<DetailOpener> {
            DefaultDetailOpener(
                navigationCoordinator = get(),
                itemCache = get(),
                identityRepository = get(),
                communityRepository = get(),
            )
        }
    }
