package com.livefast.eattrash.raccoonforlemmy.unit.login.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val loginModule =
    DI.Module("LoginModule") {
        bindViewModel {
            LoginViewModel(
                apiConfigurationRepository = instance(),
                identityRepository = instance(),
                accountRepository = instance(),
                siteRepository = instance(),
                communityRepository = instance(),
                login = instance(),
                notificationCenter = instance(),
            )
        }
    }
