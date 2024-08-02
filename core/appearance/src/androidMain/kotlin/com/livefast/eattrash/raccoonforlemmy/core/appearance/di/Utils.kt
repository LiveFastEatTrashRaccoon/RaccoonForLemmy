package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultBarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultColorSchemeProvider
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual fun getThemeRepository(): ThemeRepository {
    val res: ThemeRepository by inject(ThemeRepository::class.java)
    return res
}

actual val nativeAppearanceModule =
    module {
        single<ColorSchemeProvider> {
            DefaultColorSchemeProvider(context = get())
        }
        single<BarColorProvider> {
            DefaultBarColorProvider()
        }
    }

actual fun getColorSchemeProvider(): ColorSchemeProvider {
    val res by inject<ColorSchemeProvider>(ColorSchemeProvider::class.java)
    return res
}

actual fun getBarColorProvider(): BarColorProvider {
    val res by inject<BarColorProvider>(BarColorProvider::class.java)
    return res
}

actual fun getAppColorRepository(): AppColorRepository {
    val res by inject<AppColorRepository>(AppColorRepository::class.java)
    return res
}
