package com.github.diegoberaldin.raccoonforlemmy.feature.settings.main

import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.FontScale
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface SettingsMviModel :
    MviModel<SettingsMviModel.Intent, SettingsMviModel.UiState, SettingsMviModel.Effect> {

    sealed interface Intent {
        data class ChangeTheme(val value: ThemeState) : Intent
        data class ChangeContentFontSize(val value: Float) : Intent
        data class ChangeLanguage(val value: String) : Intent
        data class ChangeDefaultListingType(val value: ListingType) : Intent
        data class ChangePostLayout(val value: PostLayout) : Intent
        data class ChangeDefaultPostSortType(val value: SortType) : Intent
        data class ChangeDefaultCommentSortType(val value: SortType) : Intent
        data class ChangeNavBarTitlesVisible(val value: Boolean) : Intent
        data class ChangeDynamicColors(val value: Boolean) : Intent
        data class ChangeIncludeNsfw(val value: Boolean) : Intent
        data class ChangeBlurNsfw(val value: Boolean) : Intent
        data class ChangeOpenUrlsInExternalBrowser(val value: Boolean) : Intent
        data class ChangeEnableSwipeActions(val value: Boolean) : Intent
        data class ChangeCustomSeedColor(val value: Color?) : Intent
        data class ChangeCrashReportEnabled(val value: Boolean) : Intent
        data class ChangeSeparateUpAndDownVotes(val value: Boolean) : Intent
        data class ChangeAutoLoadImages(val value: Boolean) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val currentTheme: ThemeState = ThemeState.Light,
        val customSeedColor: Color? = null,
        val currentFontScale: FontScale = FontScale.Normal,
        val lang: String = "",
        val postLayout: PostLayout = PostLayout.Card,
        val defaultListingType: ListingType = ListingType.Local,
        val defaultPostSortType: SortType = SortType.Active,
        val defaultCommentSortType: SortType = SortType.New,
        val navBarTitlesVisible: Boolean = false,
        val supportsDynamicColors: Boolean = false,
        val dynamicColors: Boolean = false,
        val includeNsfw: Boolean = true,
        val blurNsfw: Boolean = true,
        val openUrlsInExternalBrowser: Boolean = false,
        val enableSwipeActions: Boolean = true,
        val crashReportEnabled: Boolean = false,
        val separateUpAndDownVotes: Boolean = false,
        val autoLoadImages: Boolean = false,
        val appVersion: String = "",
    )

    sealed interface Effect
}