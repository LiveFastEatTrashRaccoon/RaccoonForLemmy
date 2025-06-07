package com.livefast.eattrash.raccoonforlemmy.core.persistence.data

sealed interface UserTagType {
    data object Admin : UserTagType

    data object Bot : UserTagType

    data object Me : UserTagType

    data object Moderator : UserTagType

    data object OriginalPoster : UserTagType

    data object Regular : UserTagType
}

fun UserTagType.toInt(): Int = when (this) {
    UserTagType.Admin -> 1
    UserTagType.Bot -> 2
    UserTagType.Me -> 3
    UserTagType.Moderator -> 4
    UserTagType.OriginalPoster -> 5
    else -> 0
}

fun Int.toUserTagType(): UserTagType = when (this) {
    1 -> UserTagType.Admin
    2 -> UserTagType.Bot
    3 -> UserTagType.Me
    4 -> UserTagType.Moderator
    5 -> UserTagType.OriginalPoster
    else -> UserTagType.Regular
}
