package com.livefast.eattrash.raccoonforlemmy.feature.search.di

import com.livefast.eattrash.raccoonforlemmy.unit.explore.di.exploreModule
import org.koin.dsl.module

val searchTabModule =
    module {
        includes(exploreModule)
    }
