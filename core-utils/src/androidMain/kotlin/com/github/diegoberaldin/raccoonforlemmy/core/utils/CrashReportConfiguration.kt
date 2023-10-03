package com.github.diegoberaldin.raccoonforlemmy.core.utils

import android.content.Context
import com.github.diegoberaldin.raccoonforlemmy.core.utils.CrashReportConfiguration.Companion.PREFERENCES_NAME
import org.koin.dsl.module

class DefaultCrashReportConfiguration(
    private val context: Context,
) : CrashReportConfiguration {

    companion object {
        const val KEY = "acra.enable"
    }

    override fun isEnabled(): Boolean =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY, false)

    override fun setEnabled(value: Boolean) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).apply {
            edit().putBoolean(KEY, value).apply()
        }
    }
}

actual val crashReportModule = module {
    single<CrashReportConfiguration> {
        DefaultCrashReportConfiguration(
            context = get(),
        )
    }
}