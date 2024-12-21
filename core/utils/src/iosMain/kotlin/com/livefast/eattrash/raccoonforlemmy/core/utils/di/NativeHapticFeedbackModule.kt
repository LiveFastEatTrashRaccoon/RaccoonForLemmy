package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.DefaultHapticFeedback
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeHapticFeedbackModule =
    DI.Module("NativeHapticFeedbackModule") {
        bind<HapticFeedback> {
            singleton {
                DefaultHapticFeedback()
            }
        }
    }
