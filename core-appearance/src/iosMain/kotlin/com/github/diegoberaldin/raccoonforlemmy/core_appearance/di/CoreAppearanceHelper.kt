package com.github.diegoberaldin.raccoonforlemmy.core_appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.DefaultThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun getThemeRepository(): ThemeRepository = CoreAppearanceHelper.repository

object CoreAppearanceHelper : KoinComponent {
    internal val repository: ThemeRepository by inject()
}

actual val coreAppearanceModule = module {
    singleOf<ThemeRepository>(::DefaultThemeRepository)
}