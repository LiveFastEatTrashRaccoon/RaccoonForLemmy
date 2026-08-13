package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel
import kotlinx.coroutines.CancellationException

internal class DefaultPrivateMessageRepository(private val services: ServiceProvider) : PrivateMessageRepository {
    override suspend fun getAll(
        auth: String?,
        creatorId: Long?,
        page: Int,
        limit: Int,
        unreadOnly: Boolean,
    ): List<PrivateMessageModel>? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun create(message: String, auth: String?, recipientId: Long): PrivateMessageModel? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun edit(messageId: Long, message: String, auth: String?): PrivateMessageModel? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun markAsRead(messageId: Long, auth: String?, read: Boolean): PrivateMessageModel? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun delete(messageId: Long, auth: String?) {
        try {
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
        } catch (e: Exception) {
            if (e is CancellationException) throw e
        }
    }
}
