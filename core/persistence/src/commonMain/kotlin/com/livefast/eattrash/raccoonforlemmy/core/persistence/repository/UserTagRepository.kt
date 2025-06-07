package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType

interface UserTagRepository {
    suspend fun getAll(accountId: Long): List<UserTagModel>

    suspend fun getById(tagId: Long): UserTagModel?

    suspend fun getMembers(tagId: Long): List<UserTagMemberModel>

    suspend fun getTags(username: String, accountId: Long): List<UserTagModel>

    suspend fun create(model: UserTagModel, accountId: Long)

    suspend fun update(id: Long, name: String, color: Int? = null, type: Int)

    suspend fun delete(id: Long)

    suspend fun addMember(username: String, userTagId: Long)

    suspend fun removeMember(username: String, userTagId: Long)

    suspend fun getBelonging(accountId: Long, username: String): List<UserTagModel>

    suspend fun getSpecialTagColor(accountId: Long, type: UserTagType): Int?
}
