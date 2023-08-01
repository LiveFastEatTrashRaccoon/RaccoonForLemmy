package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data

data class PostModel(
    val id: Int = 0,
    val title: String = "",
    val text: String = "",
    val score: Int = 0,
    val comments: Int = 0,
    val thumbnailUrl: String? = null,
    val community: CommunityModel? = null,
    val creator: UserModel? = null,
    val saved: Boolean = false,
    val myVote: Int = 0,
)
