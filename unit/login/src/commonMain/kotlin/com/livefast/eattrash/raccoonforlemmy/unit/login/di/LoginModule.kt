package com.livefast.eattrash.raccoonforlemmy.unit.login.di

import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val loginModule =
    DI.Module("LoginModule") {
        bind<LoginMviModel> {
            provider {
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
    }
