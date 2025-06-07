package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HidePostForm(@SerialName("post_ids") val postIds: List<PostId>, @SerialName("hidden") val hidden: Boolean)
