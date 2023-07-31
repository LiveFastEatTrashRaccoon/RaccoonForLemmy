package com.github.diegoberaldin.raccoonforlemmy.data

data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String? = null,
    val host: String = "",
    val score: UserScoreModel? = null,
    val accountAge: String = "",
)

data class UserScoreModel(
    val postScore: Int = 0,
    val commentScore: Int = 0,
)