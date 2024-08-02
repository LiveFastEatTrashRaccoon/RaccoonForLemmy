package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel

interface DeleteAccountUseCase {
    suspend operator fun invoke(account: AccountModel)
}
