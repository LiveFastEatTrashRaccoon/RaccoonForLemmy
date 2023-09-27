package com.github.diegoberaldin.raccoonforlemmy.core.persistence.di

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.AccountRepository
import org.koin.core.module.Module

internal expect val persistenceInnerModule: Module

expect fun getAccountRepository(): AccountRepository