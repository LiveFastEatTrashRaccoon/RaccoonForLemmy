package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class DefaultDeleteAccountUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val accountRepository = mockk<AccountRepository>(relaxUnitFun = true)
    private val sut =
        DefaultDeleteAccountUseCase(
            accountRepository = accountRepository,
        )

    @Test
    fun whenExecute_thenInteractionsAreAsExpected() =
        runTest {
            val account = AccountModel(id = 1, username = "test", jwt = "fake-token", instance = "test")
            sut(account)

            coVerify { accountRepository.delete(1) }
        }
}
