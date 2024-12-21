package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.DefaultAppIconManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeAppIconModule =
    DI.Module("NativeAppIconModule") {
        bind<AppIconManager> {
            singleton {
                DefaultAppIconManager()
            }
        }
    }
