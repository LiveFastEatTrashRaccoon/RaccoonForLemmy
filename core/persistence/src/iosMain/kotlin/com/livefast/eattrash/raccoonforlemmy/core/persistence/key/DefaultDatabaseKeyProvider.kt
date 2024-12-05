package com.livefast.eattrash.raccoonforlemmy.core.persistence.key

import org.koin.core.annotation.Single

@Single
internal actual class DefaultDatabaseKeyProvider : DatabaseKeyProvider {
    actual override fun getKey(): ByteArray = byteArrayOf()

    actual override fun removeKey() = Unit
}
