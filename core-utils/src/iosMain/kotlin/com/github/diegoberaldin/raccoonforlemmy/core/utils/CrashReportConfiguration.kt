package com.github.diegoberaldin.raccoonforlemmy.core.utils

import org.koin.dsl.module
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.stringByAppendingPathComponent

class DefaultCrashReportConfiguration(
) : CrashReportConfiguration {

    companion object {
        const val KEY = "crashReportEnabled"
    }

    override fun isEnabled(): Boolean =
        NSUserDefaults.standardUserDefaults.boolForKey(KEY)

    override fun setEnabled(value: Boolean) {
        NSUserDefaults.standardUserDefaults.setBool(value, KEY)
    }
}

class DefaultCrashReportWriter : CrashReportWriter {

    companion object {
        const val FILE_NAME = "crash_report.txt"
    }

    override fun write(reportText: String) {
        val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true);
        val file = (paths.first() as NSString).stringByAppendingPathComponent(FILE_NAME)
        val data = NSString.create(string = reportText).dataUsingEncoding(NSUTF8StringEncoding)
        NSFileManager.defaultManager.createFileAtPath(file, data, null)
    }
}

actual val crashReportModule = module {
    single<CrashReportConfiguration> {
        DefaultCrashReportConfiguration()
    }
    single<CrashReportWriter> {
        DefaultCrashReportWriter()
    }
}