package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import org.koin.core.annotation.Single

@Single
internal expect class DefaultAppInfoRepository : AppInfoRepository {
    override fun geInfo(): AppInfo
}
