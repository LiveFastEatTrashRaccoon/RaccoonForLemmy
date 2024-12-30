package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagMemberEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultUserTagRepository(
    provider: DatabaseProvider,
) : UserTagRepository {
    private val db = provider.getDatabase()

    override suspend fun getAll(accountId: Long): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            db.usertagsQueries
                .getAllBy(accountId)
                .executeAsList()
                .map { it.toModel() }
        }

    override suspend fun getById(tagId: Long): UserTagModel? =
        withContext(Dispatchers.IO) {
            db.usertagsQueries
                .getBy(tagId)
                .executeAsOneOrNull()
                ?.toModel()
        }

    override suspend fun getMembers(tagId: Long): List<UserTagMemberModel> =
        withContext(Dispatchers.IO) {
            db.usertagmembersQueries
                .getMembers(tagId)
                .executeAsList()
                .map { it.toModel() }
        }

    override suspend fun getTags(
        username: String,
        accountId: Long,
    ): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            db.usertagmembersQueries
                .getBy(username, accountId)
                .executeAsList()
                .map { e ->
                    UserTagModel(
                        name = e.name,
                        id = e.user_tag_id ?: 0,
                        color = e.color?.toInt(),
                    )
                }
        }

    override suspend fun create(
        model: UserTagModel,
        accountId: Long,
    ) = withContext(Dispatchers.IO) {
        db.usertagsQueries.create(
            name = model.name,
            color = model.color?.toLong(),
            account_id = accountId,
        )
    }

    override suspend fun update(
        id: Long,
        name: String,
        color: Int?,
    ) = withContext(Dispatchers.IO) {
        db.usertagsQueries.update(
            id = id,
            name = name,
            color = color?.toLong(),
        )
    }

    override suspend fun delete(id: Long) =
        withContext(Dispatchers.IO) {
            db.usertagsQueries.delete(id)
        }

    override suspend fun addMember(
        username: String,
        userTagId: Long,
    ) = withContext(Dispatchers.IO) {
        db.usertagmembersQueries.create(
            username = username,
            user_tag_id = userTagId,
        )
    }

    override suspend fun removeMember(
        username: String,
        userTagId: Long,
    ) = withContext(Dispatchers.IO) {
        db.usertagmembersQueries.delete(
            username = username,
            user_tag_id = userTagId,
        )
    }

    override suspend fun getBelonging(
        accountId: Long,
        username: String,
    ): List<UserTagModel> =
        withContext(Dispatchers.IO) {
            db.usertagmembersQueries
                .getBy(
                    username = username,
                    account_id = accountId,
                ).executeAsList()
                .map { e ->
                    UserTagModel(
                        name = e.name,
                        id = e.user_tag_id ?: 0,
                        color = e.color?.toInt(),
                    )
                }
        }
}

private fun UserTagEntity.toModel() =
    UserTagModel(
        id = id,
        name = name,
        color = color?.toInt(),
    )

private fun UserTagMemberEntity.toModel() =
    UserTagMemberModel(
        username = username,
        userTagId = user_tag_id ?: 0,
    )
