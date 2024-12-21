package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.url.CustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.DefaultCustomTabsHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativeCustomTabsModule =
    DI.Module("NativeCustomTabsModule") {
        bind<CustomTabsHelper> {
            singleton {
                DefaultCustomTabsHelper(context = instance())
            }
        }
    }
