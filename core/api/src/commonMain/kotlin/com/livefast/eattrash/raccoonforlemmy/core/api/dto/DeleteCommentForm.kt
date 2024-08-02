package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteCommentForm(
    @SerialName("comment_id") val commentId: CommentId,
    @SerialName("deleted") val deleted: Boolean,
)
