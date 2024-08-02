package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build

class DefaultAppInfoRepository(
    private val context: Context,
) : AppInfoRepository {
    override fun geInfo(): AppInfo =
        runCatching {
            with(context) {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                AppInfo(
                    versionCode =
                        buildString {
                            append(packageInfo.versionName)
                            append(" (")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                append(packageInfo.longVersionCode)
                            } else {
                                append(packageInfo.versionCode)
                            }
                            append(")")
                        },
                    isDebug = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0,
                )
            }
        }.getOrElse { AppInfo("", false) }
}
