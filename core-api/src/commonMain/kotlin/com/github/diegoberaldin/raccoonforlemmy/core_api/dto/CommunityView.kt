package com.github.diegoberaldin.raccoonforlemmy.core_api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityView(
    @SerialName("community") val community: Community,
    @SerialName("subscribed") val subscribed: SubscribedType,
    @SerialName("blocked") val blocked: Boolean,
    @SerialName("counts") val counts: CommunityAggregates,
)