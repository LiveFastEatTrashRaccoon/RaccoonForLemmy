package com.livefast.eattrash.raccoonforlemmy.core.persistence.data

data class UserTagMemberModel(
    val username: String,
    val userTagId: Long,
)

data class UserTagModel(
    val id: Long? = null,
    val name: String,
    val color: Int? = null,
)
