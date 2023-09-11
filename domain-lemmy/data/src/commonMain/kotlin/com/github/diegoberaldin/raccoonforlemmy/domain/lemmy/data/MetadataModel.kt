package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

import kotlinx.serialization.Serializable

@Serializable
data class MetadataModel(
    val title: String = "",
    val description: String = "",
)
