package com.github.diegoberaldin.raccoonforlemmy.core.appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.DefaultThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.AppColorRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.DefaultAppColorRepository
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
