package com.github.diegoberaldin.raccoonforlemmy.core.utils

import org.koin.core.module.Module

interface CrashReportConfiguration {

    companion object {
        const val PREFERENCES_NAME = "AcraPreferences"
    }

    fun isEnabled(): Boolean

    fun setEnabled(value: Boolean)
}

expect val crashReportModule: Module