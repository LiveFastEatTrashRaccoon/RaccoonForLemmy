package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import org.koin.core.annotation.Single
import platform.Foundation.NSUserDefaults

@Single
internal actual class DefaultCrashReportConfiguration : CrashReportConfiguration {
    companion object {
        const val KEY = "crashReportEnabled"
    }

    actual override fun isEnabled(): Boolean = NSUserDefaults.standardUserDefaults.boolForKey(KEY)

    actual override fun setEnabled(value: Boolean) {
        NSUserDefaults.standardUserDefaults.setBool(value, KEY)
    }
}
