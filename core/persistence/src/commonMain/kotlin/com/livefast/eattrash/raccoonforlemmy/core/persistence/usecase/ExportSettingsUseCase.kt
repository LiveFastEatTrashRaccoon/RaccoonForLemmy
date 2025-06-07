package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository

interface ExportSettingsUseCase {
    suspend operator fun invoke(): String
}

internal class DefaultExportSettingsUseCase(private val settingsRepository: SettingsRepository) :
    ExportSettingsUseCase {
    override suspend fun invoke(): String {
        val settings = settingsRepository.currentSettings.value
        val data = settings.toData()
        return jsonSerializationStrategy.encodeToString(data)
    }
}
