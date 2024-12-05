package com.livefast.eattrash.raccoonforlemmy.core.persistence.key

import org.koin.core.annotation.Single

@Single
internal expect class DefaultDatabaseKeyProvider : DatabaseKeyProvider {
    override fun getKey(): ByteArray

    override fun removeKey()
}
