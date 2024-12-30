package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface UserTagHelper {
    suspend fun UserModel?.withTags(): UserModel?

    suspend fun clear()
}
