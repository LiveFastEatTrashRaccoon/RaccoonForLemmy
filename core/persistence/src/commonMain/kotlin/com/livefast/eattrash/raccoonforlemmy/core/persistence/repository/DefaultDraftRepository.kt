package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.DraftEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DraftDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftType

class DefaultDraftRepository(private val dao: DraftDao) : DraftRepository {
    override suspend fun getAll(type: DraftType, accountId: Long): List<DraftModel> = dao
        .getAllBy(type = type.toLong(), accountId = accountId)
        .executeAsList()
        .map { it.toModel() }

    override suspend fun getBy(id: Long): DraftModel? = dao
        .getBy(id)
        .executeAsOneOrNull()
        ?.toModel()

    override suspend fun create(model: DraftModel, accountId: Long) {
        dao.create(
            type = model.type.toLong(),
            body = model.body,
            title = model.title,
            url = model.url,
            communityId = model.communityId,
            postId = model.postId,
            parentId = model.parentId,
            languageId = model.languageId,
            nsfw = model.nsfw?.let { if (it) 0L else 1L },
            date = model.date,
            info = model.reference,
            accountId = accountId,
        )
    }

    override suspend fun update(model: DraftModel) {
        dao.update(
            id = model.id ?: 0,
            body = model.body,
            title = model.title,
            url = model.url,
            communityId = model.communityId,
            languageId = model.languageId,
            date = model.date,
            info = model.reference,
            nsfw = model.nsfw?.let { if (it) 0L else 1L },
        )
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }
}

private fun DraftType.toLong(): Long = when (this) {
    DraftType.Comment -> 1L
    DraftType.Post -> 0L
}

private fun Long.toDraftType(): DraftType = when (this) {
    1L -> DraftType.Comment
    else -> DraftType.Post
}

private fun DraftEntity.toModel() = DraftModel(
    id = id,
    type = type.toDraftType(),
    title = title,
    body = body,
    url = url,
    communityId = communityId,
    languageId = languageId,
    postId = postId,
    parentId = parentId,
    nsfw = nsfw?.let { it != 0L },
    date = date,
    reference = info,
)
