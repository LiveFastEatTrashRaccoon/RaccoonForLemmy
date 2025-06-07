package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.DraftEntity

interface DraftDao {
    fun getAllBy(type: Long, accountId: Long?): Query<DraftEntity>

    fun getBy(id: Long): Query<DraftEntity>

    fun create(
        type: Long,
        body: String,
        title: String?,
        url: String?,
        postId: Long?,
        parentId: Long?,
        communityId: Long?,
        languageId: Long?,
        nsfw: Long?,
        date: Long?,
        info: String?,
        accountId: Long?,
    )

    fun update(
        body: String,
        title: String?,
        url: String?,
        communityId: Long?,
        languageId: Long?,
        nsfw: Long?,
        date: Long?,
        info: String?,
        id: Long,
    )

    fun delete(id: Long)
}
