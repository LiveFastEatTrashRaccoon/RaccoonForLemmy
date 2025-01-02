package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultExportSettingsUseCaseTest {
    private val settingsRepository = mockk<SettingsRepository>()
    private val sut = DefaultExportSettingsUseCase(settingsRepository)

    @Test
    fun whenInvoked_thenResultIsAsExpected() =
        runTest {
            val originalSettings = SettingsModel()
            every { settingsRepository.currentSettings } returns MutableStateFlow(originalSettings)

            val res = sut()

            val expected = jsonSerializationStrategy.encodeToString(originalSettings.toData())
            assertEquals(expected, res)
            verify { settingsRepository.currentSettings }
        }
}
