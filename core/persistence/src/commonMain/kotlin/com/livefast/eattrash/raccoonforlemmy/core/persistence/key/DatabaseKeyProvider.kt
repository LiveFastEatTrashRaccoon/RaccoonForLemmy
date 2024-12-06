package com.livefast.eattrash.raccoonforlemmy.core.persistence.key

interface DatabaseKeyProvider {
    fun getKey(): ByteArray

    fun removeKey()
}
