package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.DefaultThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultAppColorRepository
import org.koin.dsl.module

val coreAppearanceModule =
    module {
        includes(nativeAppearanceModule)

        single<ThemeRepository> {
            DefaultThemeRepository()
        }
        single<AppColorRepository> {
            DefaultAppColorRepository()
        }
    }
