package com.github.diegoberaldin.raccoonforlemmy.main.di

import com.github.diegoberaldin.raccoonforlemmy.main.AppComponent
import com.github.diegoberaldin.raccoonforlemmy.main.DefaultAppComponent
import org.koin.dsl.module

val appModule = module {
    single<AppComponent> {
        DefaultAppComponent()
    }
}