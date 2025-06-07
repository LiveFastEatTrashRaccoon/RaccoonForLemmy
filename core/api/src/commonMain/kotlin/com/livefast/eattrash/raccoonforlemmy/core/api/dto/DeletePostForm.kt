package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeletePostForm(@SerialName("post_id") val postId: PostId, @SerialName("deleted") val deleted: Boolean)
