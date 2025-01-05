package com.livefast.eattrash.raccoonforlemmy.core.persistence.data

data class UserTagModel(
    val id: Long? = null,
    val name: String,
    val color: Int? = null,
    val type: UserTagType = UserTagType.Regular,
)

val UserTagModel.isSpecial: Boolean get() = type != UserTagType.Regular
