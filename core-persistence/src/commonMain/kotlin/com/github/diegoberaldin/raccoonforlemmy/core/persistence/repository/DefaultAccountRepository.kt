package com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.AccountEntity
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.DatabaseProvider
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DefaultAccountRepository(
    private val provider: DatabaseProvider,
) : AccountRepository {

    private val db = provider.getDatabase()

    override suspend fun getAll(): List<AccountModel> = withContext(Dispatchers.IO) {
        db.schemaQueries.getAllAccounts().executeAsList().map { it.toModel() }
    }

    override suspend fun createAccount(
        account: AccountModel,
    ) = withContext(Dispatchers.IO) {
        db.schemaQueries.createAccount(
            username = account.username,
            instance = account.instance,
            jwt = account.jwt,
        )
        val entity = db.schemaQueries.getAllAccounts()
            .executeAsList()
            .firstOrNull { it.jwt == account.jwt }
        entity?.id ?: 0
    }

    override suspend fun setActive(id: Long, active: Boolean) = withContext(Dispatchers.IO) {
        if (active) {
            db.schemaQueries.markAccountActive(id)
        } else {
            db.schemaQueries.markAccountInactive(id)
        }
    }

    override suspend fun getActive() = withContext(Dispatchers.IO) {
        val entity = db.schemaQueries.getActiveAccount().executeAsOneOrNull()
        entity?.toModel()
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        db.schemaQueries.deleteAccount(id)
    }
}

private fun AccountEntity.toModel() = AccountModel(
    id = id,
    username = username.orEmpty(),
    instance = instance.orEmpty(),
    jwt = jwt.orEmpty(),
)