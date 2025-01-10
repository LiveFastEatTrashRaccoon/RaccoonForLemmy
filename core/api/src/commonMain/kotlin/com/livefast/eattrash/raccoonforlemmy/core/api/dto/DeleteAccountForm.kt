package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountForm(
    @SerialName("delete_content")
    val deleteContent: Boolean,
    @SerialName("password")
    val password: String,
    @SerialName("auth")
    val auth: String,
)
