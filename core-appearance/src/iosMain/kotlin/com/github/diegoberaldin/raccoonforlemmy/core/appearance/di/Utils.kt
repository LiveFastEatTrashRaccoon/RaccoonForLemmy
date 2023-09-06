package com.github.diegoberaldin.raccoonforlemmy.core.appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.DefaultColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual fun getThemeRepository(): ThemeRepository = CoreAppearanceHelper.repository

actual val nativeAppearanceModule = module {
    single<ColorSchemeProvider> {
        DefaultColorSchemeProvider()
    }
}

actual fun getColorSchemeProvider(): ColorSchemeProvider = CoreAppearanceHelper.colorSchemeProvider

object CoreAppearanceHelper : KoinComponent {
    internal val repository: ThemeRepository by inject()
    internal val colorSchemeProvider: ColorSchemeProvider by inject()
}
