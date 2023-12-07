package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

internal class DefaultPrivateMessageRepository(
    private val services: ServiceProvider,
) : PrivateMessageRepository {

    override suspend fun getAll(
        auth: String?,
        page: Int,
        limit: Int,
        unreadOnly: Boolean,
    ): List<PrivateMessageModel>? = runCatching {
        val response = services.privateMessages.getPrivateMessages(
            authHeader = auth.toAuthHeader(),
            auth = auth,
            limit = limit,
            page = page,
            unreadOnly = unreadOnly,
        )
        val dto = response.body() ?: return@runCatching emptyList()
        dto.privateMessages.map { it.toModel() }
    }.getOrNull()

    override suspend fun create(
        message: String,
        auth: String?,
        recipiendId: Int,
    ) {
        val data = CreatePrivateMessageForm(
            content = message,
            auth = auth.orEmpty(),
            recipientId = recipiendId,
        )
        services.privateMessages.createPrivateMessage(
            authHeader = auth.toAuthHeader(),
            form = data,
        )
    }

    override suspend fun markAsRead(
        messageId: Int,
        auth: String?,
        read: Boolean,
    ) {
        val data = MarkPrivateMessageAsReadForm(
            privateMessageId = messageId,
            auth = auth.orEmpty(),
            read = read,
        )
        services.privateMessages.markPrivateMessageAsRead(
            authHeader = auth.toAuthHeader(),
            form = data,
        )
    }
}
