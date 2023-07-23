package com.github.diegoberaldin.raccoonforlemmy.core_api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPostsResponse(
    @SerialName("posts") val posts: List<PostView>,
)
