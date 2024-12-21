package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.AppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.DefaultAppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.DefaultThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val appearanceModule =
    DI.Module("AppearanceModule") {
        import(nativeAppearanceModule)

        bind<ThemeRepository> {
            singleton {
                DefaultThemeRepository()
            }
        }
        bind<AppColorRepository> {
            singleton {
                DefaultAppColorRepository()
        }
    }
}
