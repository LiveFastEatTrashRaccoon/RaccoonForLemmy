package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import com.livefast.eattrash.raccoonforlemmy.resources.SharedResources
import org.koin.dsl.module

internal val coreResourceModule =
    module {
        single<CoreResources> {
            SharedResources()
        }
    }
