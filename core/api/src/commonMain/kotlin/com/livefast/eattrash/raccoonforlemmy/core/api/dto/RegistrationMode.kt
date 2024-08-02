package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class RegistrationMode {
    @SerialName("Closed")
    Closed,

    @SerialName("RequireApplication")
    RequireApplication,

    @SerialName("Open")
    Open,
}
