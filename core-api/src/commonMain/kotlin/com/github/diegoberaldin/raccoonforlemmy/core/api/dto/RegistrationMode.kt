package com.github.diegoberaldin.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName

enum class RegistrationMode {
    @SerialName("Closed")
    CLOSED,

    @SerialName("RequireApplication")
    REQUIRE_APPLICATION,

    @SerialName("Open")
    OPEN,
}
