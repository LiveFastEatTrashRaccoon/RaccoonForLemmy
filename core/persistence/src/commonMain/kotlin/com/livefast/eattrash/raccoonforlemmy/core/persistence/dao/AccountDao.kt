package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.AccountEntity

interface AccountDao {
    fun getAll(): Query<AccountEntity>

    fun getBy(username: String?, instance: String?): Query<AccountEntity>

    fun getActive(): Query<AccountEntity>

    fun create(username: String, instance: String, jwt: String?, avatar: String?)

    fun setActive(id: Long)

    fun setInactive(id: Long)

    fun update(jwt: String?, avatar: String?, id: Long)

    fun delete(id: Long)
}
