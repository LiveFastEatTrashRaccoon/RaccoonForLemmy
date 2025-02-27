package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAVideo
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAnImage
import com.livefast.eattrash.raccoonforlemmy.core.utils.normalizeImgurUrl

data class PostModel(
    val id: Long = 0,
    val originalUrl: String? = null,
    val title: String = "",
    val text: String = "",
    val score: Int = 0,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val comments: Int = 0,
    val unreadComments: Int? = null,
    val thumbnailUrl: String? = null,
    val url: String? = null,
    val community: CommunityModel? = null,
    val creator: UserModel? = null,
    val saved: Boolean = false,
    val myVote: Int = 0,
    val publishDate: String? = null,
    val updateDate: String? = null,
    val nsfw: Boolean = false,
    val read: Boolean = false,
    val crossPosts: List<PostModel> = emptyList(),
    val featuredCommunity: Boolean = false,
    val featuredLocal: Boolean = false,
    val removed: Boolean = false,
    val deleted: Boolean = false,
    val locked: Boolean = false,
    val languageId: Long = 0,
)

val PostModel.imageUrl: String
    get() =
        url
            ?.normalizeImgurUrl()
            ?.takeIf { it.looksLikeAnImage } ?: thumbnailUrl
            .orEmpty()

val PostModel.videoUrl: String
    get() =
        url
            ?.takeIf { it.looksLikeAVideo }
            .orEmpty()
