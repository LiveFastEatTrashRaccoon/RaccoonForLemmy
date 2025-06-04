package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UsertagsQueries

internal class DefaultUserTagDao(
    private val queries: UsertagsQueries
) : UserTagDao {
    override fun getAllBy(accountId: Long?): Query<UserTagEntity> {
        return queries.getAllBy(accountId)
    }

    override fun getBy(id: Long): Query<UserTagEntity> {
        return queries.getBy(id)
    }

    override fun create(name: String, color: Long?, accountId: Long?, type: Long) {
        queries.create(name, color, accountId, type)
    }

    override fun update(name: String, color: Long?, type: Long, id: Long) {
        queries.update(name, color, type, id)
    }

    override fun delete(id: Long) {
        queries.delete(id)
    }
}
