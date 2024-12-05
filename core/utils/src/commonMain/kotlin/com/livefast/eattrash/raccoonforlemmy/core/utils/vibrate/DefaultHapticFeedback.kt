package com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate

import org.koin.core.annotation.Single

@Single
internal expect class DefaultHapticFeedback : HapticFeedback {
    override fun vibrate()
}
