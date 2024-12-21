package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import org.kodein.di.instance

fun getThemeRepository(): ThemeRepository {
    val res by RootDI.di.instance<ThemeRepository>()
    return res
}

fun getColorSchemeProvider(): ColorSchemeProvider {
    val res by RootDI.di.instance<ColorSchemeProvider>()
    return res
}

fun getBarColorProvider(): BarColorProvider {
    val res by RootDI.di.instance<BarColorProvider>()
    return res
}

fun getAppColorRepository(): AppColorRepository {
    val res by RootDI.di.instance<AppColorRepository>()
    return res
}
