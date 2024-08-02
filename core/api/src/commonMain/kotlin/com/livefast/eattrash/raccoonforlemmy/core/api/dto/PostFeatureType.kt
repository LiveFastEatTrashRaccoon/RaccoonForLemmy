package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class PostFeatureType {
    @SerialName("Local")
    Local,

    @SerialName("Community")
    Community,
}
