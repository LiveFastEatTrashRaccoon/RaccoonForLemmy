package com.livefast.eattrash.raccoonforlemmy.core.persistence

interface DatabaseKeyProvider {
    fun getKey(): ByteArray

    fun removeKey()
}
