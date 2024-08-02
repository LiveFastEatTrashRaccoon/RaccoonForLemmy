package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.DefaultZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import org.koin.dsl.module

val utilsModule =
    module {
        factory<ZombieModeHelper> {
            DefaultZombieModeHelper()
        }
    }
