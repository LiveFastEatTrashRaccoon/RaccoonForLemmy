package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

import kotlinx.serialization.Serializable

@Serializable
data class PersonMentionModel(
    val id: Int = 0,
    val post: PostModel,
    val creator: UserModel,
    val comment: CommentModel,
    val community: CommunityModel,
    val score: Int,
    val myVote: Int,
    val saved: Boolean,
    val isOwnPost: Boolean = false,
    val publishDate: String? = null,
)
