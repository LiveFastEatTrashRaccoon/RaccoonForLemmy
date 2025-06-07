package com.livefast.eattrash.raccoonforlemmy.android

import android.app.Application
import android.content.Context
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportConfiguration
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportWriter
import com.livefast.eattrash.raccoonforlemmy.di.initDi
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initDi {
            bind<Context> { provider { applicationContext } }
        }
        configureCrashReport()
    }

    private fun configureCrashReport() {
        val di = RootDI.di
        val crashReportWriter: CrashReportWriter by di.instance()
        val crashReportConfig: CrashReportConfiguration by di.instance()

        Thread.currentThread().apply {
            val original = uncaughtExceptionHandler
            setUncaughtExceptionHandler { t, exception ->
                if (crashReportConfig.isEnabled()) {
                    val stackTrace = exception.stackTraceToString()
                    crashReportWriter.write(stackTrace)
                }
                original?.uncaughtException(t, exception)
            }
        }
    }
}
