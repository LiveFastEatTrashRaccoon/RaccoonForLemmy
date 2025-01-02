package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlin.test.Test

class DefaultImportSettingsUseCaseTest {
    private val accountRepository = mockk<AccountRepository>()
    private val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
    private val sut = DefaultImportSettingsUseCase(settingsRepository, accountRepository)

    @Test
    fun whenInvoked_thenInteractionsAreAsExpected() =
        runTest {
            coEvery { accountRepository.getActive() } returns
                AccountModel(
                    id = 1,
                    username = "",
                    instance = "",
                    jwt = "",
                )
            val originalSettings = SettingsModel()
            val input = jsonSerializationStrategy.encodeToString(originalSettings.toData())

            sut.invoke(input)

            coVerify {
                accountRepository.getActive()
                settingsRepository.updateSettings(settings = originalSettings, accountId = 1)
                settingsRepository.changeCurrentSettings(originalSettings)
            }
        }
}
