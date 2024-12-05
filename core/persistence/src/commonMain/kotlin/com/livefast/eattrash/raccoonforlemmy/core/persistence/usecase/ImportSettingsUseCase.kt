package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

interface ImportSettingsUseCase {
    suspend operator fun invoke(content: String)
}

@Single
internal class DefaultImportSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
) : ImportSettingsUseCase {
    override suspend fun invoke(content: String) =
        withContext(Dispatchers.IO) {
            val data: SerializableSettings = jsonSerializationStrategy.decodeFromString(content)
            val settings = data.toModel()
            val accountId = accountRepository.getActive()?.id
            settingsRepository.updateSettings(settings, accountId)
            settingsRepository.changeCurrentSettings(settings)
        }
}
