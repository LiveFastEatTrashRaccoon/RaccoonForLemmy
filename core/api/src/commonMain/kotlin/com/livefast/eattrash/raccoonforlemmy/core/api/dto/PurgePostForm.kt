package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurgePostForm(@SerialName("post_id") val postId: PostId, @SerialName("reason") val reason: String?)
