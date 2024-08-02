package com.livefast.eattrash.raccoonforlemmy.feature.profile.di

import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.login.di.loginModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di.manageAccountsModule
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di.myAccountModule
import org.koin.dsl.module

val profileTabModule =
    module {
        includes(
            loginModule,
            manageAccountsModule,
            myAccountModule,
        )
        factory<ProfileMainMviModel> {
            ProfileMainViewModel(
                identityRepository = get(),
                logout = get(),
            )
        }
        factory<ProfileNotLoggedMviModel> {
            ProfileNotLoggedViewModel(
                identityRepository = get(),
            )
        }
        factory<ProfileSideMenuMviModel> {
            ProfileSideMenuViewModel(
                settingsRepository = get(),
                lemmyValueCache = get(),
            )
        }
    }
