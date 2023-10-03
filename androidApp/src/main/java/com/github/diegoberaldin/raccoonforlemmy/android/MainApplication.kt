package com.github.diegoberaldin.raccoonforlemmy.android

import android.app.Application
import android.content.Context
import com.github.diegoberaldin.raccoonforlemmy.core.utils.AppInfo
import com.github.diegoberaldin.raccoonforlemmy.core.utils.CrashReportConfiguration
import com.github.diegoberaldin.raccoonforlemmy.di.sharedHelperModule
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import org.acra.config.coreConfiguration
import org.acra.config.mailSender
import org.acra.config.notification
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        initAcra {
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON
            coreConfiguration {
                additionalSharedPreferences = listOf(CrashReportConfiguration.PREFERENCES_NAME)
            }
            notification {
                title = getString(R.string.crash_notification_title)
                text = getString(R.string.crash_notification_text)
                channelName = "RaccoonForLemmy"
                sendButtonText = MR.strings.button_confirm.getString(base)
            }
            mailSender {
                mailTo = BuildConfig.CRASH_REPORT_EMAIL
                subject = BuildConfig.CRASH_REPORT_SUBJECT
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                sharedHelperModule,
            )

            AppInfo.versionCode = buildString {
                append(BuildConfig.VERSION_NAME)
                append(" (")
                append(BuildConfig.VERSION_CODE)
                append(")")
            }
        }
    }
}
