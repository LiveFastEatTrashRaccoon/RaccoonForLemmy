package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.MultiCommunityEntity

interface MultiCommunityDao {
    fun getAll(accountId: Long?): Query<MultiCommunityEntity>

    fun getBy(name: String, accountId: Long?): Query<MultiCommunityEntity>

    fun getById(id: Long): Query<MultiCommunityEntity>

    fun create(
        name: String,
        icon: String?,
        communityIds: String,
        accountId: Long?,
    )

    fun update(
        name: String,
        icon: String?,
        communityIds: String,
        id: Long,
    )

    fun delete(id: Long)
}
