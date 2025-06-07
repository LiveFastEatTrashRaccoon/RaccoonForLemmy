package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.AccountEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.AccountsQueries

internal class DefaultAccountDao(private val queries: AccountsQueries) : AccountDao {
    override fun getAll(): Query<AccountEntity> = queries.getAll()

    override fun getBy(username: String?, instance: String?): Query<AccountEntity> = queries.getBy(username, instance)

    override fun getActive(): Query<AccountEntity> = queries.getActive()

    override fun create(username: String, instance: String, jwt: String?, avatar: String?) {
        queries.create(username, instance, jwt, avatar)
    }

    override fun setActive(id: Long) {
        queries.setActive(id)
    }

    override fun setInactive(id: Long) {
        queries.setInactive(id)
    }

    override fun update(jwt: String?, avatar: String?, id: Long) {
        queries.update(jwt, avatar, id)
    }

    override fun delete(id: Long) {
        queries.delete(id)
    }
}
