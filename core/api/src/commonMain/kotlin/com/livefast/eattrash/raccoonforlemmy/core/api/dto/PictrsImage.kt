package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PictrsImage(
    @SerialName("file") val file: String,
    @SerialName("delete_token") val deleteToken: String,
)
