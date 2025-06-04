package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.DraftEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.DraftsQueries

internal class DefaultDraftDao(
    private val queries: DraftsQueries,
) : DraftDao {
    override fun getAllBy(type: Long, accountId: Long?): Query<DraftEntity> {
        return queries.getAllBy(type, accountId)
    }

    override fun getBy(id: Long): Query<DraftEntity> {
        return queries.getBy(id)
    }

    override fun create(
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
        accountId: Long?
    ) {
        queries.create(
            type = type,
            body = body,
            title = title,
            url = url,
            postId = postId,
            parentId = parentId,
            communityId = communityId,
            languageId = languageId,
            nsfw = nsfw,
            date = date,
            info = info,
            account_id = accountId,
        )
    }

    override fun update(
        body: String,
        title: String?,
        url: String?,
        communityId: Long?,
        languageId: Long?,
        nsfw: Long?,
        date: Long?,
        info: String?,
        id: Long
    ) {
        queries.update(
            body = body,
            title = title,
            url = url,
            communityId = communityId,
            languageId = languageId,
            nsfw = nsfw,
            date = date,
            info = info,
            id = id
        )
    }

    override fun delete(id: Long) {
        queries.delete(id)
    }
}
