package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class CommunityVisibility {
    @SerialName("Public")
    Public,

    @SerialName("LocalOnly")
    LocalOnly,
}
