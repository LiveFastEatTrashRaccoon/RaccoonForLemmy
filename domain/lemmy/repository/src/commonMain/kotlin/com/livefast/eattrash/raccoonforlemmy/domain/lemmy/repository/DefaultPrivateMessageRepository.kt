package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel

internal class DefaultPrivateMessageRepository(
    private val services: ServiceProvider,
) : PrivateMessageRepository {
    override suspend fun getAll(
        auth: String?,
        creatorId: Long?,
        page: Int,
        limit: Int,
        unreadOnly: Boolean,
    ): List<PrivateMessageModel>? =
        runCatching {
            val response =
                services.v3.privateMessages.getAll(
                    authHeader = auth.toAuthHeader(),
                    auth = auth,
                    creatorId = creatorId,
                    limit = limit,
                    page = page,
                    unreadOnly = unreadOnly,
                )
            response.privateMessages.map { it.toModel() }
        }.getOrNull()

    override suspend fun create(
        message: String,
        auth: String?,
        recipientId: Long,
    ): PrivateMessageModel? =
        runCatching {
            val data =
                CreatePrivateMessageForm(
                    content = message,
                    auth = auth.orEmpty(),
                    recipientId = recipientId,
                )
            val response =
                services.v3.privateMessages.create(
                    authHeader = auth.toAuthHeader(),
                    form = data,
                )
            response.privateMessageView.toModel()
        }.getOrNull()

    override suspend fun edit(
        messageId: Long,
        message: String,
        auth: String?,
    ): PrivateMessageModel? =
        runCatching {
            val data =
                EditPrivateMessageForm(
                    content = message,
                    auth = auth.orEmpty(),
                    privateMessageId = messageId,
                )
            val response =
                services.v3.privateMessages.edit(
                    authHeader = auth.toAuthHeader(),
                    form = data,
                )
            response.privateMessageView.toModel()
        }.getOrNull()

    override suspend fun markAsRead(
        messageId: Long,
        auth: String?,
        read: Boolean,
    ): PrivateMessageModel? =
        runCatching {
            val data =
                MarkPrivateMessageAsReadForm(
                    privateMessageId = messageId,
                    auth = auth.orEmpty(),
                    read = read,
                )
            val response =
                services.v3.privateMessages.markAsRead(
                    authHeader = auth.toAuthHeader(),
                    form = data,
                )
            response.privateMessageView.toModel()
        }.getOrNull()

    override suspend fun delete(
        messageId: Long,
        auth: String?,
    ) {
        runCatching {
            val data =
                DeletePrivateMessageForm(
                    auth = auth.orEmpty(),
                    privateMessageId = messageId,
                    deleted = true,
                )
            services.v3.privateMessages.delete(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
        }
    }
}
