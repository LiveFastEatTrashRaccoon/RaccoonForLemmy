package com.livefast.eattrash.raccoonforlemmy.core.persistence.key

internal class DefaultDatabaseKeyProvider : DatabaseKeyProvider {
    override fun getKey(): ByteArray = byteArrayOf()

    override fun removeKey() = Unit
}
