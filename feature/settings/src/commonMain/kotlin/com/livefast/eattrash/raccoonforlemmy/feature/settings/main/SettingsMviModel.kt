package com.livefast.eattrash.raccoonforlemmy.feature.settings.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

interface SettingsMviModel :
    MviModel<SettingsMviModel.Intent, SettingsMviModel.UiState, SettingsMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data class ChangeIncludeNsfw(
            val value: Boolean,
        ) : Intent

        data class ChangeBlurNsfw(
            val value: Boolean,
        ) : Intent

        data class ChangeEnableSwipeActions(
            val value: Boolean,
        ) : Intent

        data class ChangeCrashReportEnabled(
            val value: Boolean,
        ) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val uiTheme: UiTheme? = null,
        val lang: String = "",
        val defaultListingType: ListingType = ListingType.All,
        val defaultPostSortType: SortType = SortType.Hot,
        val defaultCommentSortType: SortType = SortType.New,
        val enableSwipeActions: Boolean = true,
        val includeNsfw: Boolean = false,
        val blurNsfw: Boolean = true,
        val urlOpeningMode: UrlOpeningMode = UrlOpeningMode.External,
        val crashReportEnabled: Boolean = false,
        val availableSortTypesForPosts: List<SortType> = emptyList(),
        val availableSortTypesForComments: List<SortType> = emptyList(),
        val customTabsEnabled: Boolean = true,
        val supportsHiddenPosts: Boolean = false,
        val supportsMediaList: Boolean = false,
    )

    sealed interface Effect
}
