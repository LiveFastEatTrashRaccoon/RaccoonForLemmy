package com.livefast.eattrash.raccoonforlemmy.feature.home.di

import com.livefast.eattrash.raccoonforlemmy.unit.postlist.di.postListModule
import org.koin.dsl.module

val homeTabModule =
    module {
        includes(
            postListModule,
        )
    }
