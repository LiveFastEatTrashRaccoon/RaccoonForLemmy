package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import platform.Foundation.NSUserDefaults

internal class DefaultCrashReportConfiguration : CrashReportConfiguration {
    companion object {
        const val KEY = "crashReportEnabled"
    }

    override fun isEnabled(): Boolean = NSUserDefaults.standardUserDefaults.boolForKey(KEY)

    override fun setEnabled(value: Boolean) {
        NSUserDefaults.standardUserDefaults.setBool(value, KEY)
    }
}
