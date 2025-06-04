package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoriteCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.FavoriteCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultFavoriteCommunityRepository(
    private val dao: FavoriteCommunityDao,
) : FavoriteCommunityRepository {

    override suspend fun getAll(accountId: Long?): List<FavoriteCommunityModel> =
        withContext(Dispatchers.IO) {
            dao
                .getAll(accountId)
                .executeAsList()
                .map { it.toModel() }
        }

    override suspend fun getBy(
        accountId: Long?,
        communityId: Long,
    ): FavoriteCommunityModel? =
        withContext(Dispatchers.IO) {
            dao
                .getBy(
                    communityId = communityId,
                    accountId = accountId,
                ).executeAsOneOrNull()
                ?.toModel()
        }

    override suspend fun create(
        model: FavoriteCommunityModel,
        accountId: Long,
    ): Long =
        withContext(Dispatchers.IO) {
            val communityId = model.communityId ?: return@withContext 0L
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
            id ?: 0L
        }

    override suspend fun delete(
        accountId: Long?,
        model: FavoriteCommunityModel,
    ) = withContext(Dispatchers.IO) {
        val communityId = model.communityId ?: return@withContext
        val id =
            dao
                .getBy(
                    communityId = communityId,
                    accountId = accountId,
                ).executeAsOneOrNull()
                ?.id ?: return@withContext
        dao.delete(id)
    }
}

private fun FavoriteCommunityEntity.toModel() =
    FavoriteCommunityModel(
        id = id,
        communityId = communityId,
    )
