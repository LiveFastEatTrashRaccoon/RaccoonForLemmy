package com.github.diegoberaldin.raccoonforlemmy.feature.settings.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface SettingsScreenMviModel :
    MviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect> {

    sealed interface Intent {
        data class ChangeTheme(val value: ThemeState) : Intent
        data class ChangeLanguage(val value: String) : Intent
        data class ChangeDefaultListingType(val value: ListingType) : Intent
        data class ChangeDefaultPostSortType(val value: SortType) : Intent
        data class ChangeDefaultCommentSortType(val value: SortType) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val currentTheme: ThemeState = ThemeState.Light,
        val lang: String = "",
        val defaultListingType: ListingType = ListingType.Local,
        val defaultPostSortType: SortType = SortType.Active,
        val defaultCommentSortType: SortType = SortType.New,
    )

    sealed interface Effect
}
