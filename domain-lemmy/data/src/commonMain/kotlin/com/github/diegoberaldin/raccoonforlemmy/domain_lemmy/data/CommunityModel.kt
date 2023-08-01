package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data

data class CommunityModel(
    val id: Int = 0,
    val name: String = "",
    val host: String = "",
    val icon: String? = null,
)
