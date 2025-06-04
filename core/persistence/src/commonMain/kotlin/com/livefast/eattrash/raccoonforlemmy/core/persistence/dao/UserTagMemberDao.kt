package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagMemberEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usertagmembers.GetBy

interface UserTagMemberDao {
    fun getBy(username: String, accountId: Long?): Query<GetBy>

    fun getMembers(tagId: Long?): Query<UserTagMemberEntity>

    fun create(username: String, tagId: Long?)

    fun delete(username: String, tagId: Long?)
}
