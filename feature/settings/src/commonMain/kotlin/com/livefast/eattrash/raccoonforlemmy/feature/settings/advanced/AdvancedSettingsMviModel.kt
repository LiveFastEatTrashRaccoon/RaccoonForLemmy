package com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced

import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface AdvancedSettingsMviModel :
    ScreenModel,
    MviModel<AdvancedSettingsMviModel.Intent, AdvancedSettingsMviModel.UiState, AdvancedSettingsMviModel.Effect> {
    sealed interface Intent {
        data class ChangeEnableDoubleTapAction(
            val value: Boolean,
        ) : Intent

        data class ChangeAutoLoadImages(
            val value: Boolean,
        ) : Intent

        data class ChangeAutoExpandComments(
            val value: Boolean,
        ) : Intent

        data class ChangeHideNavigationBarWhileScrolling(
            val value: Boolean,
        ) : Intent

        data class ChangeMarkAsReadWhileScrolling(
            val value: Boolean,
        ) : Intent

        data class ChangeMarkAsReadOnInteraction(
            val value: Boolean,
        ) : Intent

        data class ChangeNavBarTitlesVisible(
            val value: Boolean,
        ) : Intent

        data class ChangeSearchPostTitleOnly(
            val value: Boolean,
        ) : Intent

        data class ChangeInfiniteScrollDisabled(
            val value: Boolean,
        ) : Intent

        data class ChangeImageSourcePath(
            val value: Boolean,
        ) : Intent

        data class ChangeDefaultLanguage(
            val value: Long?,
        ) : Intent

        data class ChangeFadeReadPosts(
            val value: Boolean,
        ) : Intent

        data class ChangeShowUnreadComments(
            val value: Boolean,
        ) : Intent

        data object ExportSettings : Intent

        data class ImportSettings(
            val content: String,
        ) : Intent

        data class ChangeEnableButtonsToScrollBetweenComments(
            val value: Boolean,
        ) : Intent

        data class ChangeEnableToggleFavoriteInNavDrawer(
            val value: Boolean,
        ) : Intent

        data class ChangeUseAvatarAsProfileNavigationIcon(
            val value: Boolean,
        ) : Intent

        data class ChangeOpenPostWebPageOnImageClick(
            val value: Boolean,
        ) : Intent

        data class ChangeEnableAlternateMarkdownRendering(
            val value: Boolean,
        ) : Intent

        data class ChangeRestrictLocalUserSearch(
            val value: Boolean,
        ) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val autoLoadImages: Boolean = false,
        val defaultExploreType: ListingType = ListingType.All,
        val defaultInboxUnreadOnly: Boolean = true,
        val navBarTitlesVisible: Boolean = false,
        val enableDoubleTapAction: Boolean = true,
        val autoExpandComments: Boolean = false,
        val hideNavigationBarWhileScrolling: Boolean = true,
        val zombieModeInterval: Duration = 2.5.seconds,
        val zombieModeScrollAmount: Float = 100f,
        val markAsReadWhileScrolling: Boolean = true,
        val searchPostTitleOnly: Boolean = false,
        val infiniteScrollDisabled: Boolean = false,
        val systemBarTheme: UiBarTheme = UiBarTheme.Transparent,
        val imageSourceSupported: Boolean = true,
        val imageSourcePath: Boolean = false,
        val defaultLanguageId: Long? = null,
        val availableLanguages: List<LanguageModel> = emptyList(),
        val inboxBackgroundCheckPeriod: Duration? = null,
        val appIconChangeSupported: Boolean = false,
        val fadeReadPosts: Boolean = false,
        val showUnreadComments: Boolean = false,
        val supportSettingsImportExport: Boolean = true,
        val loading: Boolean = false,
        val enableButtonsToScrollBetweenComments: Boolean = false,
        val enableToggleFavoriteInNavDrawer: Boolean = false,
        val inboxPreviewMaxLines: Int? = null,
        val defaultExploreResultType: SearchResultType = SearchResultType.Communities,
        val useAvatarAsProfileNavigationIcon: Boolean = false,
        val openPostWebPageOnImageClick: Boolean = true,
        val alternateMarkdownRenderingItemVisible: Boolean = false,
        val enableAlternateMarkdownRendering: Boolean = false,
        val restrictLocalUserSearch: Boolean = false,
        val isBarThemeSupported: Boolean = false,
        val markAsReadOnInteraction: Boolean = true,
    )

    sealed interface Effect {
        data class SaveSettings(
            val content: String,
        ) : Effect
    }
}
