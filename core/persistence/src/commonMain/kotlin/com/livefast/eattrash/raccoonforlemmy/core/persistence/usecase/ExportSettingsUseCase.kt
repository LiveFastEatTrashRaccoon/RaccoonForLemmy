package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.koin.core.annotation.Single

interface ExportSettingsUseCase {
    suspend operator fun invoke(): String
}

@Single
internal class DefaultExportSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) : ExportSettingsUseCase {
    override suspend fun invoke(): String =
        withContext(Dispatchers.IO) {
            val settings = settingsRepository.currentSettings.value
            val data = settings.toData()
            jsonSerializationStrategy.encodeToString(data)
        }
}
