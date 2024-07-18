package com.github.diegoberaldin.raccoonforlemmy.core.utils.debug

data class AppInfo(
    val versionCode: String,
    val isDebug: Boolean,
)

interface AppInfoRepository {
    fun geInfo(): AppInfo
}
