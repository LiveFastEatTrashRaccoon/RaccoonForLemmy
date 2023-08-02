package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

data class CommentModel(
    val id: Int = 0,
    val text: String,
    val community: CommunityModel? = null,
)
