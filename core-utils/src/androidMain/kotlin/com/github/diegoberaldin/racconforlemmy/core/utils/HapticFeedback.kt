package com.github.diegoberaldin.racconforlemmy.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import org.koin.dsl.module

class DefaultHapticFeedback(
    private val context: Context,
) : HapticFeedback {
    @SuppressLint("MissingPermission")
    override fun vibrate() {
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator.vibrate(VibrationEffect.createOneShot(50L, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}

actual val hapticFeedbackModule = module {
    single<HapticFeedback> {
        DefaultHapticFeedback(
            context = get(),
        )
    }
}
