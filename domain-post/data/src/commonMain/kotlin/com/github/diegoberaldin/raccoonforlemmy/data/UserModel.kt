package com.github.diegoberaldin.raccoonforlemmy.data

data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String? = null,
    val host: String = "",
)

data class UserCounterModel(
    val postCount: Int = 0,
    val postScore: Int = 0,
    val commentCount: Int = 0,
    val commentScore: Int = 0,
)