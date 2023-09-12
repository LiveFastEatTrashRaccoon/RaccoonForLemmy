package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

import kotlinx.serialization.Serializable

@Serializable
data class UserScoreModel(
    val postScore: Int = 0,
    val commentScore: Int = 0,
) : JavaSerializable