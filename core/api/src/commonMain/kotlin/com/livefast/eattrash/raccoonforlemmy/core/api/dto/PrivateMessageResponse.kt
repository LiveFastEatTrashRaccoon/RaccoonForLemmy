package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivateMessageResponse(
    @SerialName("private_message_view")
    val privateMessageView: PrivateMessageView,
)
