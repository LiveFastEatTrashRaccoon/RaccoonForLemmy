package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

data class CommunityModel(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val title: String = "",
    val host: String = "",
    val icon: String? = null,
    val banner: String? = null,
    val subscribed: Boolean? = null,
    val instanceUrl: String = "",
)
