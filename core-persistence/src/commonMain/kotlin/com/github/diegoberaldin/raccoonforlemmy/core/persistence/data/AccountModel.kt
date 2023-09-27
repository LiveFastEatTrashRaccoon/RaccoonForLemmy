package com.github.diegoberaldin.raccoonforlemmy.core.persistence.data

data class AccountModel(
    val id: Long? = null,
    val username: String,
    val instance: String,
    val jwt: String,
)