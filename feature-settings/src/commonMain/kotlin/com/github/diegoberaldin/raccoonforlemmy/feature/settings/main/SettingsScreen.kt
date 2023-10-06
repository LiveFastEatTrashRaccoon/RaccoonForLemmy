package com.github.diegoberaldin.raccoonforlemmy.feature.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ColorBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.FontScaleBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.LanguageBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.PostLayoutBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterContractKeys
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLanguageName
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.di.getSettingsScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.SettingsTab
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsColorRow
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsHeader
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsRow
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsSwitchRow
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.di.staticString
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc

class SettingsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getSettingsScreenModel() }
        model.bindToLifecycle(SettingsTab.key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val notificationCenter = remember { getNotificationCenter() }
        DisposableEffect(key) {
            onDispose {
                notificationCenter.removeObserver(key)
            }
        }

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                val languageRepository = remember { getLanguageRepository() }
                val lang by languageRepository.currentLanguage.collectAsState()
                val title by remember(lang) {
                    mutableStateOf(staticString(MR.strings.navigation_settings.desc()))
                }
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    SettingsHeader(
                        icon = Icons.Default.Palette,
                        title = stringResource(MR.strings.settings_section_appearance),
                    )
                    // language
                    SettingsRow(
                        title = stringResource(MR.strings.settings_language),
                        value = uiState.lang.toLanguageName(),
                        onTap = {
                            val sheet = LanguageBottomSheet()
                            notificationCenter.addObserver({ result ->
                                (result as? String)?.also { lang ->
                                    model.reduce(SettingsMviModel.Intent.ChangeLanguage(lang))
                                }
                            }, key, NotificationCenterContractKeys.ChangeLanguage)
                            bottomSheetNavigator.show(sheet)
                        },
                    )
                    // theme
                    SettingsRow(
                        title = stringResource(MR.strings.settings_ui_theme),
                        value = uiState.currentTheme.toReadableName(),
                        onTap = {
                            val sheet = ThemeBottomSheet()
                            notificationCenter.addObserver({ result ->
                                (result as? ThemeState)?.also { value ->
                                    model.reduce(SettingsMviModel.Intent.ChangeTheme(value))
                                }
                            }, key, NotificationCenterContractKeys.ChangeTheme)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // dynamic colors
                    if (uiState.supportsDynamicColors) {
                        SettingsSwitchRow(
                            title = stringResource(MR.strings.settings_dynamic_colors),
                            value = uiState.dynamicColors,
                            onValueChanged = { value ->
                                model.reduce(
                                    SettingsMviModel.Intent.ChangeDynamicColors(
                                        value
                                    )
                                )
                            }
                        )
                    }

                    val colorSchemeProvider = remember { getColorSchemeProvider() }
                    // custom scheme seed color
                    SettingsColorRow(
                        title = stringResource(MR.strings.settings_custom_seed_color),
                        value = uiState.customSeedColor ?: colorSchemeProvider.getColorScheme(
                            theme = uiState.currentTheme,
                            dynamic = uiState.dynamicColors,
                        ).primary,
                        onTap = {
                            val sheet = ColorBottomSheet()
                            notificationCenter.addObserver({ result ->
                                model.reduce(
                                    SettingsMviModel.Intent.ChangeCustomSeedColor(
                                        result as? Color?
                                    )
                                )
                            }, key, NotificationCenterContractKeys.ChangeColor)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // font scale
                    SettingsRow(
                        title = stringResource(MR.strings.settings_content_font_scale),
                        value = uiState.currentFontScale.toReadableName(),
                        onTap = {
                            val sheet = FontScaleBottomSheet()
                            notificationCenter.addObserver({ result ->
                                (result as? Float)?.also { value ->
                                    model.reduce(
                                        SettingsMviModel.Intent.ChangeContentFontSize(
                                            value
                                        )
                                    )
                                }
                            }, key, NotificationCenterContractKeys.ChangeFontSize)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // post layout
                    SettingsRow(
                        title = stringResource(MR.strings.settings_post_layout),
                        value = uiState.postLayout.toReadableName(),
                        onTap = {
                            val sheet = PostLayoutBottomSheet()
                            notificationCenter.addObserver({ result ->
                                (result as? PostLayout)?.also { value ->
                                    model.reduce(
                                        SettingsMviModel.Intent.ChangePostLayout(
                                            value
                                        )
                                    )
                                }
                            }, key, NotificationCenterContractKeys.ChangePostLayout)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // separate upvotes and downvotes
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_separate_up_and_downvotes),
                        value = uiState.separateUpAndDownVotes,
                        onValueChanged = { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeSeparateUpAndDownVotes(
                                    value
                                )
                            )
                        }
                    )

                    // navigation bar titles
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_navigation_bar_titles_visible),
                        value = uiState.navBarTitlesVisible,
                        onValueChanged = { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeNavBarTitlesVisible(
                                    value
                                )
                            )
                        }
                    )

                    SettingsHeader(
                        icon = Icons.Default.Tune,
                        title = stringResource(MR.strings.settings_section_feed),
                    )
                    // default listing type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_listing_type),
                        value = uiState.defaultListingType.toReadableName(),
                        onTap = {
                            val sheet = ListingTypeBottomSheet(
                                isLogged = uiState.isLogged,
                            )
                            notificationCenter.addObserver({ result ->
                                (result as? ListingType)?.also {
                                    model.reduce(
                                        SettingsMviModel.Intent.ChangeDefaultListingType(
                                            it
                                        )
                                    )
                                }
                            }, key, NotificationCenterContractKeys.ChangeFeedType)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // default post sort type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_post_sort_type),
                        value = uiState.defaultPostSortType.toReadableName(),
                        onTap = {
                            val sheet = SortBottomSheet(
                                expandTop = true,
                            )
                            notificationCenter.addObserver({
                                (it as? SortType)?.also { sortType ->
                                    model.reduce(
                                        SettingsMviModel.Intent.ChangeDefaultPostSortType(
                                            sortType
                                        )
                                    )
                                }
                            }, key, NotificationCenterContractKeys.ChangeSortType)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    // default comment sort type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_comment_sort_type),
                        value = uiState.defaultCommentSortType.toReadableName(),
                        onTap = {
                            val sheet = SortBottomSheet(
                                values = listOf(
                                    SortType.Hot,
                                    SortType.Top.Generic,
                                    SortType.New,
                                    SortType.Old,
                                    SortType.Controversial,
                                ),
                            )
                            notificationCenter.addObserver({
                                (it as? SortType)?.also { sortType ->
                                    model.reduce(
                                        SettingsMviModel.Intent.ChangeDefaultCommentSortType(
                                            sortType
                                        )
                                    )
                                }
                            }, key, NotificationCenterContractKeys.ChangeCommentSortType)
                            bottomSheetNavigator.show(sheet)
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.SettingsApplications,
                        title = stringResource(MR.strings.settings_section_behaviour),
                    )
                    // swipe actions
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_enable_swipe_actions),
                        value = uiState.enableSwipeActions,
                        onValueChanged = { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeEnableSwipeActions(
                                    value
                                )
                            )
                        }
                    )

                    // URL open
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_open_url_external),
                        value = uiState.openUrlsInExternalBrowser,
                        onValueChanged = { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeOpenUrlsInExternalBrowser(
                                    value
                                )
                            )
                        }
                    )

                    SettingsHeader(
                        icon = Icons.Default.Shield,
                        title = stringResource(MR.strings.settings_section_nsfw),
                    )
                    // NSFW options
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_include_nsfw),
                        value = uiState.includeNsfw,
                        onValueChanged = { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeIncludeNsfw(value))
                        }
                    )
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_blur_nsfw),
                        value = uiState.blurNsfw,
                        onValueChanged = { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeBlurNsfw(value))
                        }
                    )

                    SettingsHeader(
                        icon = Icons.Default.BugReport,
                        title = stringResource(MR.strings.settings_section_debug),
                    )
                    // enable crash report
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_enable_crash_report),
                        value = uiState.crashReportEnabled,
                        onValueChanged = { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeCrashReportEnabled(value))
                        }
                    )

                    // app version
                    SettingsRow(
                        title = stringResource(MR.strings.settings_app_version),
                        value = uiState.appVersion,
                    )

                    Spacer(modifier = Modifier.height(Spacing.xxxl))
                }
            }
        }
    }
}
