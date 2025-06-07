package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoriteCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoritecommunitiesQueries

internal class DefaultFavoriteCommunityDao(private val queries: FavoritecommunitiesQueries) : FavoriteCommunityDao {
    override fun getAll(accountId: Long?): Query<FavoriteCommunityEntity> = queries.getAll(accountId)

    override fun getBy(communityId: Long, accountId: Long?): Query<FavoriteCommunityEntity> =
        queries.getBy(communityId, accountId)

    override fun create(communityId: Long, accountId: Long?) {
        queries.create(communityId, accountId)
    }

    override fun delete(id: Long) {
        queries.delete(id)
    }
}
