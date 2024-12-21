package com.livefast.eattrash.raccoonforlemmy.feature.profile.di

import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val profileTabModule =
    DI.Module("ProfileTabModule") {
        bind<ProfileMainMviModel> {
            provider {
                ProfileMainViewModel(
                    identityRepository = instance(),
                    logout = instance(),
                )
            }
        }
        bind<ProfileSideMenuMviModel> {
            provider {
                ProfileSideMenuViewModel(
                    settingsRepository = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
        bind<ProfileNotLoggedMviModel> {
            provider {
                ProfileNotLoggedViewModel(
                    identityRepository = instance(),
                )
            }
        }
    }
