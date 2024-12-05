package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import android.content.Context
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportConfiguration.Companion.PREFERENCES_NAME
import org.koin.core.annotation.Single

@Single
internal actual class DefaultCrashReportConfiguration(
    private val context: Context,
) : CrashReportConfiguration {
    companion object {
        const val KEY = "crashReportEnabled"
    }

    actual override fun isEnabled(): Boolean =
        context
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY, false)

    actual override fun setEnabled(value: Boolean) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).apply {
            edit().putBoolean(KEY, value).apply()
        }
    }
}
