package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessageResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessagesResponse

interface PrivateMessageServiceV3 {
    suspend fun getAll(
        authHeader: String? = null,
        auth: String? = null,
        page: Int? = null,
        creatorId: PersonId? = null,
        limit: Int? = null,
        unreadOnly: Boolean? = null,
    ): PrivateMessagesResponse

    suspend fun create(authHeader: String? = null, form: CreatePrivateMessageForm): PrivateMessageResponse

    suspend fun edit(authHeader: String? = null, form: EditPrivateMessageForm): PrivateMessageResponse

    suspend fun markAsRead(authHeader: String? = null, form: MarkPrivateMessageAsReadForm): PrivateMessageResponse

    suspend fun delete(authHeader: String? = null, form: DeletePrivateMessageForm): PrivateMessageResponse
}
