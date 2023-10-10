package com.github.diegoberaldin.raccoonforlemmy.core.utils

import org.koin.core.module.Module

interface CrashReportConfiguration {

    companion object {
        const val PREFERENCES_NAME = "CrashReportPreferences"
    }

    fun isEnabled(): Boolean

    fun setEnabled(value: Boolean)
}

interface CrashReportWriter {
    fun write(reportText: String)
}

expect val crashReportModule: Module