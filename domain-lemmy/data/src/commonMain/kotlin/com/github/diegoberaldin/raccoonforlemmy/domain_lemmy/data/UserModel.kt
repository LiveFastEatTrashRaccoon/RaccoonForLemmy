package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data

data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String? = null,
    val host: String = "",
    val score: UserScoreModel? = null,
    val accountAge: String = "",
)
