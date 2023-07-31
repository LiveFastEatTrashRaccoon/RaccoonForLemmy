package com.github.diegoberaldin.raccoonforlemmy.core_appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.DefaultThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreAppearanceModule = module {
    singleOf<ThemeRepository>(::DefaultThemeRepository)
}
