package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivateMessageView(
    @SerialName("private_message")
    val privateMessage: PrivateMessage,
    @SerialName("creator")
    val creator: Person,
    @SerialName("recipient")
    val recipient: Person,
)
