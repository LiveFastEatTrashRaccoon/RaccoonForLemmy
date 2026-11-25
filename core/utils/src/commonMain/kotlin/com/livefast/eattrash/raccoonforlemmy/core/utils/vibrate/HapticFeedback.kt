package com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate

enum class HapticEffect {
    /** Light and short, to signal the start of a gesture. */
    Tick,
    /** Firmer, to confirm an action. */
    Click
}

interface HapticFeedback {
    fun vibrate(effect: HapticEffect = HapticEffect.Click)
}
