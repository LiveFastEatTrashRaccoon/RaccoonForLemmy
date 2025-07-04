package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftType

interface DraftRepository {
    suspend fun getAll(type: DraftType, accountId: Long): List<DraftModel>

    suspend fun getBy(id: Long): DraftModel?

    suspend fun create(model: DraftModel, accountId: Long)

    suspend fun update(model: DraftModel)

    suspend fun delete(id: Long)
}
