package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class CommentSortType {
    @SerialName("Hot")
    Hot,

    @SerialName("Top")
    Top,

    @SerialName("New")
    New,

    @SerialName("Old")
    Old,

    @SerialName("Controversial")
    Controversial,
}
