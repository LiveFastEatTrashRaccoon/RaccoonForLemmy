package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection

@Stable
interface ConfigureNavBarMviModel :
    MviModel<ConfigureNavBarMviModel.Intent, ConfigureNavBarMviModel.UiState, ConfigureNavBarMviModel.Effect> {
    sealed interface Intent {
        data object Reset : Intent

        data object HapticFeedback : Intent

        data class SwapItems(val from: Int, val to: Int) : Intent

        data class Delete(val section: TabNavigationSection) : Intent

        data object Save : Intent

        data class Add(val value: TabNavigationSection) : Intent
    }

    data class UiState(
        val sections: List<TabNavigationSection> = emptyList(),
        val availableSections: List<TabNavigationSection> = emptyList(),
        val hasUnsavedChanges: Boolean = false,
    )

    sealed interface Effect
}
