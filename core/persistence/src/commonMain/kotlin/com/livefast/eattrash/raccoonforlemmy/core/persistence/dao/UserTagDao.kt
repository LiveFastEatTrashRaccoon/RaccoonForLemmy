package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagEntity

interface UserTagDao {
    fun getAllBy(accountId: Long?): Query<UserTagEntity>

    fun getBy(id: Long): Query<UserTagEntity>

    fun create(
        name: String,
        color: Long?,
        accountId: Long?,
        type: Long,
    )

    fun update(
        name: String,
        color: Long?,
        type: Long,
        id: Long,
    )

    fun delete(id: Long)
}
