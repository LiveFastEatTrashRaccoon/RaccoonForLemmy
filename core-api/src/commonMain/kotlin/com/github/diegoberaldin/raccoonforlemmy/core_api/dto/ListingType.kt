package com.github.diegoberaldin.raccoonforlemmy.core_api.dto

import kotlinx.serialization.SerialName

enum class ListingType {
    @SerialName("All")
    All,

    @SerialName("Local")
    Local,

    @SerialName("Subscribed")
    Subscribed
}