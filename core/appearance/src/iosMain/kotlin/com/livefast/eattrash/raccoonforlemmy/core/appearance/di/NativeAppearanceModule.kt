package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultBarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultColorSchemeProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

actual val nativeAppearanceModule =
    DI.Module("NativeAppearanceModule") {
        bind<ColorSchemeProvider> {
            singleton {
                DefaultColorSchemeProvider()
            }
        }
        bind<BarColorProvider> {
            singleton {
                DefaultBarColorProvider()
            }
        }
    }
