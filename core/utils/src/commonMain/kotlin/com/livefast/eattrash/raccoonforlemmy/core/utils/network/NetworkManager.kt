package com.livefast.eattrash.raccoonforlemmy.core.utils.network

interface NetworkManager {
    suspend fun isNetworkAvailable(): Boolean
}
