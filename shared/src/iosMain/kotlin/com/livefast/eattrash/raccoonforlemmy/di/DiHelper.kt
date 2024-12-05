package com.livefast.eattrash.raccoonforlemmy.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(rootModule)
    }
}
