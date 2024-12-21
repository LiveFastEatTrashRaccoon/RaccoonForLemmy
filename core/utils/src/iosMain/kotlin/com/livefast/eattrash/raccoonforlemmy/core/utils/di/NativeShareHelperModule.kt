package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.share.DefaultShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeShareHelperModule =
    DI.Module("NativeShareHelperModule") {
        bind<ShareHelper> {
            singleton {
                DefaultShareHelper()
            }
        }
    }
