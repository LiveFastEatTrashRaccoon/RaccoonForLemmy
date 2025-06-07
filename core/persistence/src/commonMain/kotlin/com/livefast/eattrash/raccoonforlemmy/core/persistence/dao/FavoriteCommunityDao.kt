package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoriteCommunityEntity

interface FavoriteCommunityDao {
    fun getAll(accountId: Long?): Query<FavoriteCommunityEntity>

    fun getBy(communityId: Long, accountId: Long?): Query<FavoriteCommunityEntity>

    fun create(communityId: Long, accountId: Long?)

    fun delete(id: Long)
}
