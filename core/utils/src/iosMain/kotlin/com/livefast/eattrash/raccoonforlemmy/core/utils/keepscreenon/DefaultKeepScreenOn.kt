package com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.annotation.Single
import platform.UIKit.UIApplication

@Single
internal actual class DefaultKeepScreenOn : KeepScreenOn {
    actual override fun activate() {
        UIApplication.sharedApplication().idleTimerDisabled = true
    }

    actual override fun deactivate() {
        UIApplication.sharedApplication().idleTimerDisabled = false
    }
}

@Composable
actual fun rememberKeepScreenOn(): KeepScreenOn =
    remember {
        DefaultKeepScreenOn()
    }
