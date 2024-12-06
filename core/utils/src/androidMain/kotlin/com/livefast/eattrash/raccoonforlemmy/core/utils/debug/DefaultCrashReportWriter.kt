package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import android.content.Context
import android.os.Environment
import org.koin.core.annotation.Single
import java.io.File
import java.io.FileWriter

@Single
internal actual class DefaultCrashReportWriter(
    private val context: Context,
) : CrashReportWriter {
    companion object {
        const val FILE_NAME = "crash_report.txt"
    }

    actual override fun write(reportText: String) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val state = Environment.getExternalStorageState()
        val logFile =
            if (Environment.MEDIA_MOUNTED == state) {
                File(dir, FILE_NAME)
            } else {
                File(context.cacheDir, FILE_NAME)
            }
        FileWriter(logFile, true).use { writer ->
            writer.append(reportText)
            writer.append("\n")
            writer.flush()
        }
    }
}
