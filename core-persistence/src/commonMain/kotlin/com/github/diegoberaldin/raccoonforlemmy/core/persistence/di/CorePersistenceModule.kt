package com.github.diegoberaldin.raccoonforlemmy.core.persistence.di

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.DatabaseProvider
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.DefaultDatabaseProvider
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.DefaultAccountRepository
import org.koin.dsl.module

val corePersistenceModule = module {
    includes(persistenceInnerModule)
    single<DatabaseProvider> {
        DefaultDatabaseProvider(
            driverFactory = get(),
        )
    }
    single<AccountRepository> {
        DefaultAccountRepository(
            provider = get(),
        )
    }
}