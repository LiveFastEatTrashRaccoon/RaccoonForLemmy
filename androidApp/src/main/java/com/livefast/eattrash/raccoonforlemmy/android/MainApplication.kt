package com.livefast.eattrash.raccoonforlemmy.android

import android.app.Application
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportConfiguration
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportWriter
import com.livefast.eattrash.raccoonforlemmy.di.rootModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(rootModule)
        }.apply {
            val crashReportWriter: CrashReportWriter by inject()
            val crashReportConfig: CrashReportConfiguration by inject()

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
}
