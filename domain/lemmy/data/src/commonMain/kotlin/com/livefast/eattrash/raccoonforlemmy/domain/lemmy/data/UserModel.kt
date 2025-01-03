package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import kotlin.jvm.Transient

data class UserModel(
    val id: Long = 0,
    val instanceId: Long = 0,
    val name: String = "",
    val displayName: String = "",
    val avatar: String? = null,
    val bio: String? = null,
    val matrixUserId: String? = null,
    val banner: String? = null,
    val host: String = "",
    val score: UserScoreModel? = null,
    val accountAge: String = "",
    val banned: Boolean = false,
    val updateDate: String? = null,
    val bot: Boolean = false,
    @Transient val tags: List<TagModel> = emptyList(),
)

fun List<UserModel>.containsId(value: Long?): Boolean = any { it.id == value }

fun UserModel.readableName(preferNickname: Boolean): String =
    if (preferNickname) {
        displayName.takeIf { it.isNotEmpty() } ?: readableHandle
    } else {
        readableHandle
    }

val UserModel.readableHandle: String
    get() =
        buildString {
            append(name)
            if (host.isNotEmpty()) {
                append("@$host")
            }
        }
