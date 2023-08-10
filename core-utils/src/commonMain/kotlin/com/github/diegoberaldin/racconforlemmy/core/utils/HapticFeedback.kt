package com.github.diegoberaldin.racconforlemmy.core.utils

import org.koin.core.module.Module

interface HapticFeedback {
    fun vibrate()
}

expect val hapticFeedbackModule: Module
