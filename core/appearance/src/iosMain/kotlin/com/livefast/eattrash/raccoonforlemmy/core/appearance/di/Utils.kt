package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultBarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultColorSchemeProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual fun getThemeRepository(): ThemeRepository = CoreAppearanceHelper.repository

actual val nativeAppearanceModule =
    module {
        single<ColorSchemeProvider> {
            DefaultColorSchemeProvider()
        }
        single<BarColorProvider> {
            DefaultBarColorProvider()
        }
    }

actual fun getColorSchemeProvider(): ColorSchemeProvider = CoreAppearanceHelper.colorSchemeProvider

actual fun getBarColorProvider(): BarColorProvider = CoreAppearanceHelper.barColorProvider

actual fun getAppColorRepository(): AppColorRepository = CoreAppearanceHelper.appColorRepository

internal object CoreAppearanceHelper : KoinComponent {
    internal val repository: ThemeRepository by inject()
    internal val colorSchemeProvider: ColorSchemeProvider by inject()
    internal val barColorProvider: BarColorProvider by inject()
    internal val appColorRepository: AppColorRepository by inject()
}
