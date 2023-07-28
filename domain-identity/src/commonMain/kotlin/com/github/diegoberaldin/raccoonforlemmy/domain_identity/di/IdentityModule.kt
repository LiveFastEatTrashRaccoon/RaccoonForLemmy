package com.github.diegoberaldin.raccoonforlemmy.domain_identity.di

import com.github.diegoberaldin.raccoonforlemmy.domain_identity.usecase.DefaultLoginUseCase
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.usecase.LoginUseCase
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.AuthRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.DefaultApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.DefaultAuthRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.DefaultIdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.IdentityRepository
import org.koin.dsl.module

val coreIdentityModule = module {
    single<ApiConfigurationRepository> {
        DefaultApiConfigurationRepository(get())
    }
    single<IdentityRepository> {
        DefaultIdentityRepository(get())
    }
    single<AuthRepository> {
        DefaultAuthRepository(get())
    }
    single<LoginUseCase> {
        DefaultLoginUseCase(
            apiConfigurationRepository = get(),
            authRepository = get(),
            identityRepository = get(),
        )
    }
}
