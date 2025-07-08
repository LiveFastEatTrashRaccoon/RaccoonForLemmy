package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.navigation.DefaultMainRouter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val mainRouterModule =
    DI.Module("MainRouterModule") {
        bind<MainRouter> {
            singleton {
                DefaultMainRouter(
                    navigationCoordinator = instance(),
                    itemCache = instance(),
                    identityRepository = instance(),
                    communityRepository = instance(),
                )
            }
        }
    }
