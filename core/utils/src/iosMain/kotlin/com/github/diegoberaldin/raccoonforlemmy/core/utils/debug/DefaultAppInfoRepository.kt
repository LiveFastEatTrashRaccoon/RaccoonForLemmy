package com.github.diegoberaldin.raccoonforlemmy.core.utils.debug

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

class DefaultAppInfoRepository : AppInfoRepository {
    @OptIn(ExperimentalNativeApi::class)
    override fun geInfo(): AppInfo {
        val versionCode =
            buildString {
                val dict = NSBundle.mainBundle.infoDictionary
                val buildNumber = dict?.get("CFBundleVersion") as? String ?: ""
                val versionName = dict?.get("CFBundleShortVersionString") as? String ?: ""
                if (versionName.isNotEmpty()) {
                    append(versionName)
                }
                if (buildNumber.isNotEmpty()) {
                    append(" (")
                    append(buildNumber)
                    append(")")
                }
            }
        val isDebug = Platform.isDebugBinary
        return AppInfo(
            versionCode = versionCode,
            isDebug = isDebug,
        )
    }
}
