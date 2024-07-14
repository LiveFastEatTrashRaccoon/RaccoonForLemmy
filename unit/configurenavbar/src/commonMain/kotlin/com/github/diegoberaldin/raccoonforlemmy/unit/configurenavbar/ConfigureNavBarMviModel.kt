package com.github.diegoberaldin.raccoonforlemmy.unit.configurenavbar

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.TabNavigationSection

@Stable
interface ConfigureNavBarMviModel :
    MviModel<ConfigureNavBarMviModel.Intent, ConfigureNavBarMviModel.UiState, ConfigureNavBarMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data object Reset : Intent

        data object HapticFeedback : Intent

        data class SwapItems(
            val from: Int,
            val to: Int,
        ) : Intent

        data class Delete(
            val section: TabNavigationSection,
        ) : Intent

        data object Save : Intent
    }

    data class UiState(
        val sections: List<TabNavigationSection> = emptyList(),
        val availableSections: List<TabNavigationSection> = emptyList(),
        val hasUnsavedChanges: Boolean = false,
    )

    sealed interface Effect
}
