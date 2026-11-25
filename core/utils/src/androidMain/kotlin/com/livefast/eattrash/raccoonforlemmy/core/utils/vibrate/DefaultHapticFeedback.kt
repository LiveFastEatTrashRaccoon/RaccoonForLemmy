package com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

internal class DefaultHapticFeedback(
    private val context: Context,
) : HapticFeedback {
    @SuppressLint("MissingPermission")
    override fun vibrate(effect: HapticEffect) {
        val vibrator = context.getSystemService(Vibrator::class.java)
        val nativeEffect =
            if (Build.VERSION.SDK_INT >= 29) {
                VibrationEffect.createPredefined(
                    when (effect) {
                        HapticEffect.Tick -> VibrationEffect.EFFECT_TICK
                        HapticEffect.Click -> VibrationEffect.EFFECT_CLICK
                    },
                )
            } else {
                VibrationEffect.createOneShot(
                    1L,
                    VibrationEffect.DEFAULT_AMPLITUDE,
                )
            }
        vibrator.vibrate(nativeEffect)
    }
}
