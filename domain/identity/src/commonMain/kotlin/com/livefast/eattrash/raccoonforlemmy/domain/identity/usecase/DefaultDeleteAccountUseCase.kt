package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository

internal class DefaultDeleteAccountUseCase(
    private val accountRepository: AccountRepository,
) : DeleteAccountUseCase {
    override suspend fun invoke(account: AccountModel) {
        account.id?.also { id ->
            accountRepository.delete(id)
        }
    }
}
