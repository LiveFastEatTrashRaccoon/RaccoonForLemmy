package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class PostListingMode {
    @SerialName("List")
    List,

    @SerialName("Card")
    Card,

    @SerialName("SmallCard")
    SmallCard,
}
