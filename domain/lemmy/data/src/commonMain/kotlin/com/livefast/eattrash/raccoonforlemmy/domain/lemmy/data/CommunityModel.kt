package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import kotlin.jvm.Transient

data class CommunityModel(
    val id: Long = 0,
    val instanceId: Long = 0,
    val name: String = "",
    val description: String = "",
    val title: String = "",
    val host: String = "",
    val icon: String? = null,
    val banner: String? = null,
    val subscribed: Boolean? = null,
    val instanceUrl: String = "",
    val nsfw: Boolean = false,
    val monthlyActiveUsers: Int = 0,
    val weeklyActiveUsers: Int = 0,
    val dailyActiveUsers: Int = 0,
    val subscribers: Int = 0,
    val posts: Int = 0,
    val comments: Int = 0,
    val creationDate: String? = null,
    val postingRestrictedToMods: Boolean? = null,
    @Transient val favorite: Boolean = false,
    val hidden: Boolean = false,
    val visibilityType: CommunityVisibilityType = CommunityVisibilityType.Public,
    val currentlyBanned: Boolean = false,
)

fun CommunityModel.readableName(preferNickname: Boolean): String = if (preferNickname) {
    title.takeIf { it.isNotEmpty() }?.replace("&amp;", "&") ?: readableHandle
} else {
    readableHandle
}

val CommunityModel.readableHandle: String
    get() =
        buildString {
            append(name)
            if (host.isNotEmpty()) {
                append("@$host")
            }
        }
