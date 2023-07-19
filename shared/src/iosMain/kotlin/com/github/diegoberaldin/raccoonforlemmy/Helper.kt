package com.github.diegoberaldin.raccoonforlemmy

import com.github.diegoberaldin.raccoonforlemmy.example.di.greetingModule
import com.github.diegoberaldin.raccoonforlemmy.example.di.platformModule
import com.github.diegoberaldin.raccoonforlemmy.main.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            greetingModule,
            platformModule,
            appModule,
        )
    }
}