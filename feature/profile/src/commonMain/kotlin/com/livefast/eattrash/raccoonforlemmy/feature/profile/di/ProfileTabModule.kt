package com.livefast.eattrash.raccoonforlemmy.feature.profile.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val profileTabModule =
    DI.Module("ProfileTabModule") {
        bindViewModel {
            ProfileMainViewModel(
                identityRepository = instance(),
                logout = instance(),
            )
        }
        bindViewModel {
            ProfileSideMenuViewModel(
                settingsRepository = instance(),
                lemmyValueCache = instance(),
            )
        }
        bindViewModel {
            ProfileNotLoggedViewModel(
                identityRepository = instance(),
            )
        }
    }
