package com.livefast.eattrash.raccoonforlemmy.core.utils.network

import org.koin.core.annotation.Single

@Single
internal expect class DefaultNetworkManager : NetworkManager {
    override suspend fun isNetworkAvailable(): Boolean
}
