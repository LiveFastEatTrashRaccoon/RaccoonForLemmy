package com.github.diegoberaldin.raccoonforlemmy.core.utils

import android.content.Context
import android.os.Environment
import com.github.diegoberaldin.raccoonforlemmy.core.utils.CrashReportConfiguration.Companion.PREFERENCES_NAME
import org.koin.dsl.module
import java.io.File
import java.io.FileWriter

class DefaultCrashReportConfiguration(
    private val context: Context,
) : CrashReportConfiguration {

    companion object {
        const val KEY = "crashReportEnabled"
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

class DefaultCrashReportWriter(
    private val context: Context,
) : CrashReportWriter {

    companion object {
        const val FILE_NAME = "crash_report.txt"
    }

    override fun write(reportText: String) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val state = Environment.getExternalStorageState()
        val logFile = if (Environment.MEDIA_MOUNTED == state) {
            File(dir, FILE_NAME)
        } else {
            File(context.cacheDir, FILE_NAME)
        }
        try {
            val writer = FileWriter(logFile, false)
            writer.append(reportText)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

actual val crashReportModule = module {
    single<CrashReportConfiguration> {
        DefaultCrashReportConfiguration(
            context = get(),
        )
    }
    single<CrashReportWriter> {
        DefaultCrashReportWriter(
            context = get(),
        )
    }
}