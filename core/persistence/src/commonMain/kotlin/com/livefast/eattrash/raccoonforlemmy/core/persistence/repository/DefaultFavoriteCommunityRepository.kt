package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoriteCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.FavoriteCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel

internal class DefaultFavoriteCommunityRepository(private val dao: FavoriteCommunityDao) :
    FavoriteCommunityRepository {
    override suspend fun getAll(accountId: Long?): List<FavoriteCommunityModel> = dao
        .getAll(accountId)
        .executeAsList()
        .map { it.toModel() }

    override suspend fun getBy(accountId: Long?, communityId: Long): FavoriteCommunityModel? = dao
        .getBy(
            communityId = communityId,
            accountId = accountId,
        ).executeAsOneOrNull()
        ?.toModel()

    override suspend fun create(model: FavoriteCommunityModel, accountId: Long): Long {
        val communityId = model.communityId ?: return 0L
        dao.create(
            communityId = communityId,
            accountId = accountId,
        )
        val id =
            dao
                .getBy(
                    communityId = communityId,
                    accountId = accountId,
                ).executeAsOneOrNull()
                ?.id
        return id ?: 0L
    }

    override suspend fun delete(accountId: Long?, model: FavoriteCommunityModel) {
        val communityId = model.communityId ?: return
        val id =
            dao
                .getBy(
                    communityId = communityId,
                    accountId = accountId,
                ).executeAsOneOrNull()
                ?.id ?: return
        dao.delete(id)
    }
}

private fun FavoriteCommunityEntity.toModel() = FavoriteCommunityModel(
    id = id,
    communityId = communityId,
)
