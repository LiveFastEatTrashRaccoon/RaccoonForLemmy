package com.livefast.eattrash.raccoonforlemmy.core.api.di

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.DefaultServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val apiModule =
    DI.Module("ApiModule") {
        bind<ServiceProvider>(tag = "default") {
            singleton {
                DefaultServiceProvider(appInfoRepository = instance())
            }
        }
        bind<ServiceProvider>(tag = "custom") {
            singleton {
                DefaultServiceProvider(appInfoRepository = instance())
            }
        }
    }
