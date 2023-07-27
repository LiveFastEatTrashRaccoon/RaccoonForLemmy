package com.github.diegoberaldin.raccoonforlemmy.data

data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String? = null,
    val host: String = "",
)