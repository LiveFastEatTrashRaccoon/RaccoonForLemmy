package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel

interface UserTagRepository {
    suspend fun getAll(accountId: Long): List<UserTagModel>

    suspend fun getMembers(tagId: Long): List<UserTagMemberModel>

    suspend fun getTags(
        username: String,
        accountId: Long,
    ): List<UserTagModel>

    suspend fun create(
        model: UserTagModel,
        accountId: Long,
    )

    suspend fun update(
        id: Long,
        name: String,
    )

    suspend fun delete(id: Long)

    suspend fun addMember(
        username: String,
        userTagId: Long,
    )

    suspend fun removeMember(
        username: String,
        userTagId: Long,
    )

    suspend fun getBelonging(
        accountId: Long,
        username: String,
    ): List<UserTagModel>
}
