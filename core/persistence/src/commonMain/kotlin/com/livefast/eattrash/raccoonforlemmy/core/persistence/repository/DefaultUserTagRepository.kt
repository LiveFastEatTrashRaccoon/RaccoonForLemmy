package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagMemberEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.UserTagDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.UserTagMemberDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toUserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultUserTagRepository(
    private val dao: UserTagDao,
    private val membersDao: UserTagMemberDao,
) : UserTagRepository {

    override suspend fun getAll(accountId: Long): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            dao
                .getAllBy(accountId)
                .executeAsList()
                .map { it.toModel() }
        }

    override suspend fun getById(tagId: Long): UserTagModel? =
        withContext(Dispatchers.IO) {
            dao
                .getBy(tagId)
                .executeAsOneOrNull()
                ?.toModel()
        }

    override suspend fun getMembers(tagId: Long): List<UserTagMemberModel> =
        withContext(Dispatchers.IO) {
            membersDao
                .getMembers(tagId)
                .executeAsList()
                .map { it.toModel() }
        }

    override suspend fun getTags(
        username: String,
        accountId: Long,
    ): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            membersDao
                .getBy(username, accountId)
                .executeAsList()
                .map { e ->
                    UserTagModel(
                        name = e.name,
                        id = e.user_tag_id ?: 0,
                        color = e.color?.toInt(),
                        type = e.type.toInt().toUserTagType(),
                    )
                }
        }

    override suspend fun create(
        model: UserTagModel,
        accountId: Long,
    ): Unit = withContext(Dispatchers.IO) {
        dao.create(
            name = model.name,
            color = model.color?.toLong(),
            accountId = accountId,
            type = model.type.toInt().toLong(),
        )
    }

    override suspend fun update(
        id: Long,
        name: String,
        color: Int?,
        type: Int,
    ): Unit = withContext(Dispatchers.IO) {
        dao.update(
            id = id,
            name = name,
            color = color?.toLong(),
            type = type.toLong(),
        )
    }

    override suspend fun delete(id: Long): Unit =
        withContext(Dispatchers.IO) {
            dao.delete(id)
        }

    override suspend fun addMember(
        username: String,
        userTagId: Long,
    ): Unit = withContext(Dispatchers.IO) {
        membersDao.create(
            username = username,
            tagId = userTagId,
        )
    }

    override suspend fun removeMember(
        username: String,
        userTagId: Long,
    ): Unit = withContext(Dispatchers.IO) {
        membersDao.delete(
            username = username,
            tagId = userTagId,
        )
    }

    override suspend fun getBelonging(
        accountId: Long,
        username: String,
    ): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            membersDao
                .getBy(
                    username = username,
                    accountId = accountId,
                ).executeAsList()
                .map { e ->
                    UserTagModel(
                        name = e.name,
                        id = e.user_tag_id ?: 0,
                        color = e.color?.toInt(),
                        type = e.type.toInt().toUserTagType(),
                    )
                }
        }

    override suspend fun getSpecialTagColor(
        accountId: Long,
        type: UserTagType,
    ): Int? {
        val allTags = getAll(accountId)
        return allTags.firstOrNull { it.type == type }?.color
    }
}

private fun UserTagEntity.toModel() =
    UserTagModel(
        id = id,
        name = name,
        color = color?.toInt(),
        type = type.toInt().toUserTagType(),
    )

private fun UserTagMemberEntity.toModel() =
    UserTagMemberModel(
        username = username,
        userTagId = user_tag_id ?: 0,
    )
