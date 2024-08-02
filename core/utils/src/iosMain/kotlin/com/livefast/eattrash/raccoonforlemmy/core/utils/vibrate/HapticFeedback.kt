package com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate

import platform.UIKit.UIImpactFeedbackGenerator

class DefaultHapticFeedback() : HapticFeedback {
    override fun vibrate() {
        UIImpactFeedbackGenerator().apply {
            prepare()
            impactOccurred()
        }
    }
}
