package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagMemberEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UsertagmembersQueries
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usertagmembers.GetBy

internal class DefaultUserTagMemberDao(
    private val queries: UsertagmembersQueries,
) : UserTagMemberDao {
    override fun getBy(username: String, accountId: Long?): Query<GetBy> {
        return queries.getBy(username, accountId)
    }

    override fun getMembers(tagId: Long?): Query<UserTagMemberEntity> {
        return queries.getMembers(tagId)
    }

    override fun create(username: String, tagId: Long?) {
        queries.create(username, tagId)
    }

    override fun delete(username: String, tagId: Long?) {
        queries.delete(username, tagId)
    }
}
