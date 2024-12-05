package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.appearance.theme")
internal actual class AppearanceThemeModule

actual fun getThemeRepository(): ThemeRepository = CoreAppearanceHelper.repository

actual fun getColorSchemeProvider(): ColorSchemeProvider = CoreAppearanceHelper.colorSchemeProvider

actual fun getBarColorProvider(): BarColorProvider = CoreAppearanceHelper.barColorProvider

actual fun getAppColorRepository(): AppColorRepository = CoreAppearanceHelper.appColorRepository

internal object CoreAppearanceHelper : KoinComponent {
    internal val repository: ThemeRepository by inject()
    internal val colorSchemeProvider: ColorSchemeProvider by inject()
    internal val barColorProvider: BarColorProvider by inject()
    internal val appColorRepository: AppColorRepository by inject()
}
