package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import kotlinx.coroutines.flow.StateFlow

@Stable
interface SettingsRepository {
    val currentSettings: StateFlow<SettingsModel>
    val currentBottomBarSections: StateFlow<List<Int>>

    suspend fun createSettings(settings: SettingsModel, accountId: Long)

    suspend fun getSettings(accountId: Long?): SettingsModel

    suspend fun updateSettings(settings: SettingsModel, accountId: Long?)

    fun changeCurrentSettings(settings: SettingsModel)

    fun changeCurrentBottomBarSections(sectionIds: List<Int>)
}
