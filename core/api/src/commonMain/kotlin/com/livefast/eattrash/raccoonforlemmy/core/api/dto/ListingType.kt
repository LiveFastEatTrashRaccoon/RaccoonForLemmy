package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class ListingType {
    @SerialName("All")
    All,

    @SerialName("Local")
    Local,

    @SerialName("Subscribed")
    Subscribed,

    @SerialName("ModeratorView")
    ModeratorView,
}
