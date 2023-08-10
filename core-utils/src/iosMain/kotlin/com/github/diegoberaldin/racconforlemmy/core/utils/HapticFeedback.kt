package com.github.diegoberaldin.racconforlemmy.core.utils

import org.koin.dsl.module
import platform.UIKit.UIImpactFeedbackGenerator

class DefaultHapticFeedback() : HapticFeedback {
    override fun vibrate() {
        UIImpactFeedbackGenerator().apply {
            prepare()
            impactOccurred()
        }
    }
}

actual val hapticFeedbackModule = module {
    single<HapticFeedback> {
        DefaultHapticFeedback()
    }
}
