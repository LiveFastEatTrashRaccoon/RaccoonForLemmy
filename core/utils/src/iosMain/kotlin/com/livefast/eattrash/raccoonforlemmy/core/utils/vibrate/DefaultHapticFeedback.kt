package com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

internal class DefaultHapticFeedback : HapticFeedback {
    override fun vibrate(effect: HapticEffect) {
        val nativeEffect =
            when (effect) {
                HapticEffect.Tick -> UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
                HapticEffect.Click -> UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
            }
        UIImpactFeedbackGenerator(style = nativeEffect).apply {
            prepare()
            impactOccurred()
        }
    }
}
