package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.AccountEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.AccountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

internal class DefaultAccountRepository(
    private val dao: AccountDao,
) : AccountRepository {

    override suspend fun getAll(): List<AccountModel> =
        withContext(Dispatchers.IO) {
            dao
                .getAll()
                .executeAsList()
                .map { it.toModel() }
        }

    override fun observeAll(): Flow<List<AccountModel>> =
        channelFlow {
            while (isActive) {
                send(getAll())
                delay(1000)
            }
        }.distinctUntilChanged()

    override suspend fun getBy(
        username: String,
        instance: String,
    ) = withContext(Dispatchers.IO) {
        dao
            .getBy(username.lowercase(), instance.lowercase())
            .executeAsOneOrNull()
            ?.toModel()
    }

    override suspend fun createAccount(account: AccountModel) =
        withContext(Dispatchers.IO) {
            dao.create(
                username = account.username,
                instance = account.instance,
                jwt = account.jwt,
                avatar = account.avatar,
            )
            val entity =
                dao
                    .getAll()
                    .executeAsList()
                    .firstOrNull { it.jwt == account.jwt }
            entity?.id ?: 0
        }

    override suspend fun setActive(
        id: Long,
        active: Boolean,
    ): Unit = withContext(Dispatchers.IO) {
        if (active) {
            dao.setActive(id)
        } else {
            dao.setInactive(id)
        }
    }

    override suspend fun getActive() =
        withContext(Dispatchers.IO) {
            val entity = dao.getActive().executeAsOneOrNull()
            entity?.toModel()
        }

    override suspend fun update(
        id: Long,
        avatar: String?,
        jwt: String?,
    ): Unit = withContext(Dispatchers.IO) {
        dao.update(
            jwt = jwt,
            avatar = avatar,
            id = id,
        )
    }

    override suspend fun delete(id: Long): Unit =
        withContext(Dispatchers.IO) {
            dao.delete(id)
        }
}

private fun AccountEntity.toModel() =
    AccountModel(
        id = id,
        username = username,
        instance = instance,
        jwt = jwt.orEmpty(),
        active = active != 0L,
        avatar = avatar,
    )
