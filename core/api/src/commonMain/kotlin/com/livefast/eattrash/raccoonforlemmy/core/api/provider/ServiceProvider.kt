package com.livefast.eattrash.raccoonforlemmy.core.api.provider

import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.V3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.V4

interface ServiceProvider {
    val currentInstance: String
    val defaultInstance: String
    val v3: V3
    val v4: V4

    fun changeInstance(value: String)

    suspend fun getApiVersion(): String
}
