package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.MultiCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.MultiCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel

internal class DefaultMultiCommunityRepository(
    private val dao: MultiCommunityDao,
) : MultiCommunityRepository {

    override suspend fun getAll(accountId: Long): List<MultiCommunityModel> =
        dao
            .getAll(accountId)
            .executeAsList()
            .map { it.toModel() }

    override suspend fun getById(id: Long): MultiCommunityModel? =
        dao
            .getById(id)
            .executeAsOneOrNull()
            ?.toModel()

    override suspend fun create(
        model: MultiCommunityModel,
        accountId: Long,
    ): Long {
        dao.create(
            name = model.name,
            icon = model.icon,
            communityIds = model.communityIds.joinToString(","),
            accountId = accountId,
        )
        val id =
            dao
                .getBy(name = model.name, accountId = accountId)
                .executeAsOneOrNull()
                ?.id
        return id ?: 0L
    }

    override suspend fun update(model: MultiCommunityModel): Unit =
        dao.update(
            name = model.name,
            icon = model.icon,
            communityIds = model.communityIds.joinToString(","),
            id = model.id ?: 0,
        )

    override suspend fun delete(model: MultiCommunityModel): Unit =
        dao.delete(model.id ?: 0)
}

private fun MultiCommunityEntity.toModel() =
    MultiCommunityModel(
        id = id,
        name = name,
        icon = icon,
        communityIds =
            communityIds
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toLong() },
    )
