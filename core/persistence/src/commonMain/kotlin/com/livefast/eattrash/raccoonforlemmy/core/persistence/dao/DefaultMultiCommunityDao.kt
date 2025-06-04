package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.MultiCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.MulticommunitiesQueries

internal class DefaultMultiCommunityDao(
    private val queries: MulticommunitiesQueries
) : MultiCommunityDao {
    override fun getAll(accountId: Long?): Query<MultiCommunityEntity> {
        return queries.getAll(accountId)
    }

    override fun getBy(name: String, accountId: Long?): Query<MultiCommunityEntity> {
        return queries.getBy(name, accountId)
    }

    override fun getById(id: Long): Query<MultiCommunityEntity> {
        return queries.getById(id)
    }

    override fun create(name: String, icon: String?, communityIds: String, accountId: Long?) {
        queries.create(name, icon, communityIds, accountId)
    }

    override fun update(name: String, icon: String?, communityIds: String, id: Long) {
        queries.update(name, icon, communityIds, id)
    }

    override fun delete(id: Long) {
        queries.delete(id)
    }
}
