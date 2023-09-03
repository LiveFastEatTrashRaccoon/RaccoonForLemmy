package com.github.diegoberaldin.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkCommentReplyAsReadForm(
    @SerialName("comment_reply_id")
    val replyId: CommentReplyId,
    @SerialName("read")
    val read: Boolean,
    @SerialName("auth")
    val auth: String,
)