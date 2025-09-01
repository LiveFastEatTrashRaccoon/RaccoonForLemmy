package com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectLanguageDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectNumberBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SliderBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconVariant
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.getPrettyDuration
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getFileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val SETTINGS_MIME_TYPE = "application/json"
private const val SETTINGS_FILE_NAME = "raccoon4lemmy_settings.json"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsScreen(modifier: Modifier = Modifier) {
    val model: AdvancedSettingsMviModel = getViewModel<AdvancedSettingsViewModel>()
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val mainRouter = remember { getMainRouter() }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    var screenWidth by remember { mutableFloatStateOf(0f) }
    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = LocalStrings.current.messageOperationSuccessful
    val errorMessage = LocalStrings.current.messageGenericError
    val scope = rememberCoroutineScope()
    val fileSystemManager = remember { getFileSystemManager() }
    val coreResources = remember { getCoreResources() }
    var languageDialogOpened by remember { mutableStateOf(false) }
    var fileInputOpened by remember { mutableStateOf(false) }
    var settingsContent by remember { mutableStateOf<String?>(null) }
    var appIconBottomSheetOpened by remember { mutableStateOf(false) }
    var barThemeBottomSheetOpened by remember { mutableStateOf(false) }
    var zombieModeDurationBottomSheetOpened by remember { mutableStateOf(false) }
    var inboxCheckDurationBottomSheetOpened by remember { mutableStateOf(false) }
    var inboxTypeBottomSheetOpened by remember { mutableStateOf(false) }
    var exploreListingTypeBottomSheetOpened by remember { mutableStateOf(false) }
    var exploreResultTypeBottomSheetOpened by remember { mutableStateOf(false) }
    var selectInboxPreviewMaxLinesBottomSheetOpened by remember { mutableStateOf(false) }
    var selectZombieModeAmount by remember { mutableStateOf(false) }

    LaunchedEffect(model) {
        model.effects
            .onEach { evt ->
                when (evt) {
                    is AdvancedSettingsMviModel.Effect.SaveSettings -> {
                        settingsContent = evt.content
                    }
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier =
        modifier
            .onGloballyPositioned {
                screenWidth = it.size.toSize().width
            },
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = LocalStrings.current.settingsAdvanced,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    if (navigationCoordinator.canPop.value) {
                        IconButton(
                            onClick = {
                                navigationCoordinator.pop()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = LocalStrings.current.actionGoBack,
                            )
                        }
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    snackbarData = data,
                )
            }
        },
    ) { padding ->
        Box(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ).nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            ) {
                SettingsHeader(
                    title = LocalStrings.current.settingsTitleDisplay,
                    icon = Icons.Default.DisplaySettings,
                )
                // navigation bar titles
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsNavigationBarTitlesVisible,
                    value = uiState.navBarTitlesVisible,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeNavBarTitlesVisible(value),
                        )
                    },
                )

                // system bar theme
                if (uiState.isBarThemeSupported) {
                    SettingsRow(
                        title = LocalStrings.current.settingsBarTheme,
                        value = uiState.systemBarTheme.toReadableName(),
                        onTap = {
                            barThemeBottomSheetOpened = true
                        },
                    )
                }

                // bottom navigation hiding
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsHideNavigationBar,
                    value = uiState.hideNavigationBarWhileScrolling,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeHideNavigationBarWhileScrolling(
                                value,
                            ),
                        )
                    },
                )

                SettingsHeader(
                    title = LocalStrings.current.settingsTitleReading,
                    icon = Icons.AutoMirrored.Default.Article,
                )
                if (uiState.isLogged) {
                    // visually differentiate read posts
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsFadeReadPosts,
                        value = uiState.fadeReadPosts,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeFadeReadPosts(value),
                            )
                        },
                    )

                    // show unread comment number
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsShowUnreadComments,
                        value = uiState.showUnreadComments,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeShowUnreadComments(value),
                            )
                        },
                    )
                }

                // default explore result type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultExploreResultType,
                    value = uiState.defaultExploreResultType.toReadableName(),
                    onTap = {
                        exploreResultTypeBottomSheetOpened = true
                    },
                )

                // default explore listing type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultExploreType,
                    value = uiState.defaultExploreType.toReadableName(),
                    onTap = {
                        exploreListingTypeBottomSheetOpened = true
                    },
                )
                if (uiState.isLogged) {
                    // default inbox type
                    SettingsRow(
                        title = LocalStrings.current.settingsDefaultInboxType,
                        value =
                        if (uiState.defaultInboxUnreadOnly) {
                            LocalStrings.current.inboxListingTypeUnread
                        } else {
                            LocalStrings.current.inboxListingTypeAll
                        },
                        onTap = {
                            inboxTypeBottomSheetOpened = true
                        },
                    )

                    // inbox preview max lines
                    SettingsRow(
                        title = LocalStrings.current.settingsInboxPreviewMaxLines,
                        value =
                        if (uiState.inboxPreviewMaxLines == null) {
                            LocalStrings.current.settingsPostBodyMaxLinesUnlimited
                        } else {
                            uiState.inboxPreviewMaxLines.toString()
                        },
                        onTap = {
                            selectInboxPreviewMaxLinesBottomSheetOpened = true
                        },
                    )
                }

                // default language
                val languageValue =
                    uiState.availableLanguages
                        .firstOrNull { l ->
                            l.id == uiState.defaultLanguageId
                        }?.takeIf { l ->
                            l.id > 0 // undetermined language
                        }?.name ?: LocalStrings.current.undetermined
                SettingsRow(
                    title = LocalStrings.current.advancedSettingsDefaultLanguage,
                    value = languageValue,
                    onTap = {
                        languageDialogOpened = true
                    },
                )

                // open post web page on image click
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsItemOpenPostWebPageOnImageClick,
                    subtitle = LocalStrings.current.settingsSubtitleOpenPostWebPageOnImageClick,
                    value = uiState.openPostWebPageOnImageClick,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeOpenPostWebPageOnImageClick(
                                value,
                            ),
                        )
                    },
                )

                // infinite scrolling
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsInfiniteScrollDisabled,
                    value = uiState.infiniteScrollDisabled,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeInfiniteScrollDisabled(value),
                        )
                    },
                )

                // auto-expand comments
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsAutoExpandComments,
                    value = uiState.autoExpandComments,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeAutoExpandComments(value),
                        )
                    },
                )

                if (uiState.isLogged) {
                    // mark as read while scrolling
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsMarkAsReadWhileScrolling,
                        value = uiState.markAsReadWhileScrolling,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeMarkAsReadWhileScrolling(
                                    value,
                                ),
                            )
                        },
                    )
                    // mark as read on vote or save
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsMarkAsReadOnInteraction,
                        subtitle = LocalStrings.current.settingsMarkAsReadOnInteractionSubtitle,
                        value = uiState.markAsReadOnInteraction,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeMarkAsReadOnInteraction(
                                    value,
                                ),
                            )
                        },
                    )
                }

                // zombie mode interval
                SettingsRow(
                    title = LocalStrings.current.settingsZombieModeInterval,
                    value =
                    uiState.zombieModeInterval.getPrettyDuration(
                        secondsLabel = LocalStrings.current.postSecondShort,
                        minutesLabel = LocalStrings.current.postMinuteShort,
                        hoursLabel = LocalStrings.current.homeSortTypeTop6Hours,
                        daysLabel = LocalStrings.current.profileDayShort,
                    ),
                    onTap = {
                        zombieModeDurationBottomSheetOpened = true
                    },
                )

                // zombie scroll amount
                SettingsRow(
                    title = LocalStrings.current.settingsZombieModeScrollAmount,
                    value =
                    buildString {
                        val pt =
                            uiState.zombieModeScrollAmount
                                .toLocalDp()
                                .value
                                .roundToInt()
                        append(pt)
                        append(LocalStrings.current.settingsPointsShort)
                    },
                    onTap = {
                        selectZombieModeAmount = true
                    },
                )

                // enable buttons to scroll between comments
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsEnableButtonsToScrollBetweenComments,
                    value = uiState.enableButtonsToScrollBetweenComments,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeEnableButtonsToScrollBetweenComments(
                                value,
                            ),
                        )
                    },
                )

                SettingsHeader(
                    title = LocalStrings.current.settingsTitlePictures,
                    icon = Icons.Default.Photo,
                )
                // image loading
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsAutoLoadImages,
                    value = uiState.autoLoadImages,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeAutoLoadImages(value),
                        )
                    },
                )

                if (uiState.imageSourceSupported) {
                    // image source path
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsItemImageSourcePath,
                        subtitle = LocalStrings.current.settingsSubtitleImageSourcePath,
                        value = uiState.imageSourcePath,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeImageSourcePath(value),
                            )
                        },
                    )
                }

                SettingsHeader(
                    title = LocalStrings.current.settingsTitleExperimental,
                    icon = Icons.Default.Science,
                )
                SettingsRow(
                    title = LocalStrings.current.settingsItemConfigureBottomNavigationBar,
                    disclosureIndicator = true,
                    onTap = {
                        mainRouter.openConfigureNavBar()
                    },
                )
                if (uiState.alternateMarkdownRenderingItemVisible) {
                    // alternate Markdown rendering
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsItemAlternateMarkdownRendering,
                        value = uiState.enableAlternateMarkdownRendering,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeEnableAlternateMarkdownRendering(
                                    value,
                                ),
                            )
                        },
                    )
                }
                if (uiState.isLogged) {
                    // edit favorites in navigation drawer
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsEnableToggleFavoriteInNavDrawer,
                        value = uiState.enableToggleFavoriteInNavDrawer,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeEnableToggleFavoriteInNavDrawer(
                                    value,
                                ),
                            )
                        },
                    )

                    // double tap
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsEnableDoubleTap,
                        value = uiState.enableDoubleTapAction,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeEnableDoubleTapAction(
                                    value,
                                ),
                            )
                        },
                    )
                }
                // search posts only in title
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsSearchPostsTitleOnly,
                    subtitle = LocalStrings.current.settingsSearchPostsTitleOnlySubtitle,
                    value = uiState.searchPostTitleOnly,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeSearchPostTitleOnly(value),
                        )
                    },
                )
                // restrict local user search to results
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsSearchRestrictLocalUserSearch,
                    subtitle = LocalStrings.current.settingsSearchRestrictLocalUserSearchSubtitle,
                    value = uiState.restrictLocalUserSearch,
                    onChangeValue = { value ->
                        model.reduce(
                            AdvancedSettingsMviModel.Intent.ChangeRestrictLocalUserSearch(value),
                        )
                    },
                )

                if (uiState.isLogged) {
                    // check inbox unread items
                    SettingsRow(
                        title =
                        buildString {
                            append(LocalStrings.current.settingsInboxBackgroundCheckPeriod)
                        },
                        value =
                        uiState.inboxBackgroundCheckPeriod.let { value ->
                            value?.getPrettyDuration(
                                secondsLabel = LocalStrings.current.postSecondShort,
                                minutesLabel = LocalStrings.current.postMinuteShort,
                                hoursLabel = LocalStrings.current.postHourShort,
                                daysLabel = LocalStrings.current.profileDayShort,
                            ) ?: LocalStrings.current.never
                        },
                        onTap = {
                            inboxCheckDurationBottomSheetOpened = true
                        },
                    )
                }

                // custom app icon
                if (uiState.appIconChangeSupported) {
                    SettingsRow(
                        title =
                        buildString {
                            append(LocalStrings.current.settingsAppIcon)
                            append(" ")
                            append(LocalStrings.current.requiresRestart)
                        },
                        onTap = {
                            appIconBottomSheetOpened = true
                        },
                    )
                }

                if (uiState.supportSettingsImportExport) {
                    SettingsRow(
                        title = LocalStrings.current.settingsExport,
                        onTap = {
                            model.reduce(AdvancedSettingsMviModel.Intent.ExportSettings)
                        },
                    )
                    SettingsRow(
                        title = LocalStrings.current.settingsImport,
                        onTap = {
                            fileInputOpened = true
                        },
                    )
                }

                if (uiState.isLogged) {
                    // use avatar as profile navigation icon
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsUseAvatarAsProfileNavigationIcon,
                        value = uiState.useAvatarAsProfileNavigationIcon,
                        onChangeValue = { value ->
                            model.reduce(
                                AdvancedSettingsMviModel.Intent.ChangeUseAvatarAsProfileNavigationIcon(
                                    value,
                                ),
                            )
                        },
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xxxl))
            }
        }
    }

    if (uiState.loading) {
        ProgressHud()
    }

    if (languageDialogOpened) {
        SelectLanguageDialog(
            currentLanguageId = uiState.defaultLanguageId,
            languages = uiState.availableLanguages,
            onSelect = { languageId ->
                model.reduce(AdvancedSettingsMviModel.Intent.ChangeDefaultLanguage(languageId))
            },
            onDismiss = {
                languageDialogOpened = false
            },
        )
    }

    if (fileInputOpened) {
        fileSystemManager.readFromFile(mimeTypes = arrayOf(SETTINGS_MIME_TYPE)) { content ->
            if (content != null) {
                model.reduce(AdvancedSettingsMviModel.Intent.ImportSettings(content))
            }
            fileInputOpened = false
        }
    }

    if (appIconBottomSheetOpened) {
        val values =
            listOf(
                AppIconVariant.Default,
                AppIconVariant.Classical,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.settingsAppIcon,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    leadingContent = {
                        val painter =
                            when (value) {
                                AppIconVariant.Classical -> coreResources.appIconClassical
                                else -> coreResources.appIconDefault
                            }
                        Image(
                            modifier = Modifier.size(IconSize.m),
                            painter = painter,
                            contentDescription = null,
                        )
                    },
                    label = value.toReadableName(),
                )
            },
            onSelect = { index ->
                appIconBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeAppIconVariant(values[index]))
                }
            },
        )
    }

    if (barThemeBottomSheetOpened) {
        val values =
            buildList {
                this += UiBarTheme.Transparent
                if (uiState.isBarOpaqueThemeSupported) {
                    this += UiBarTheme.Opaque
                }
                this += UiBarTheme.Solid
            }
        CustomModalBottomSheet(
            title = LocalStrings.current.settingsBarTheme,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value.toReadableName())
            },
            onSelect = { index ->
                barThemeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeSystemBarTheme(values[index]))
                }
            },
        )
    }

    if (zombieModeDurationBottomSheetOpened) {
        val values =
            listOf(
                1.seconds,
                2.seconds,
                3.seconds,
                5.seconds,
                10.seconds,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.settingsZombieModeInterval,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label =
                    value.getPrettyDuration(
                        secondsLabel = LocalStrings.current.postSecondShort,
                        minutesLabel = LocalStrings.current.postMinuteShort,
                        hoursLabel = LocalStrings.current.postHourShort,
                        daysLabel = LocalStrings.current.profileDayShort,
                    ),
                )
            },
            onSelect = { index ->
                zombieModeDurationBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeZombieInterval(values[index]))
                }
            },
        )
    }

    if (inboxCheckDurationBottomSheetOpened) {
        val values =
            listOf(
                null,
                15.minutes,
                30.minutes,
                1.hours,
                2.hours,
                5.hours,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.settingsInboxBackgroundCheckPeriod,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label =
                    value?.getPrettyDuration(
                        secondsLabel = LocalStrings.current.postSecondShort,
                        minutesLabel = LocalStrings.current.postMinuteShort,
                        hoursLabel = LocalStrings.current.postHourShort,
                        daysLabel = LocalStrings.current.profileDayShort,
                    ) ?: LocalStrings.current.never,
                )
            },
            onSelect = { index ->
                inboxCheckDurationBottomSheetOpened = false
                if (index != null) {
                    val value = values[index]
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeInboxBackgroundCheckPeriod(value))
                }
            },
        )
    }

    if (inboxTypeBottomSheetOpened) {
        val values =
            listOf(
                LocalStrings.current.inboxListingTypeUnread,
                LocalStrings.current.inboxListingTypeAll,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.inboxListingTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value)
            },
            onSelect = { index ->
                inboxTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeInboxType(unreadOnly = index == 0))
                }
            },
        )
    }

    if (exploreListingTypeBottomSheetOpened) {
        val values =
            buildList {
                if (uiState.isLogged) {
                    this += ListingType.Subscribed
                }
                this += ListingType.All
                this += ListingType.Local
            }
        CustomModalBottomSheet(
            title = LocalStrings.current.inboxListingTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label = value.toReadableName(),
                    trailingContent = {
                        Icon(
                            modifier = Modifier.size(IconSize.m),
                            imageVector = value.toIcon(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                )
            },
            onSelect = { index ->
                exploreListingTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeExploreFeedType(value = values[index]))
                }
            },
        )
    }

    if (exploreResultTypeBottomSheetOpened) {
        val values =
            listOf(
                SearchResultType.Posts,
                SearchResultType.Communities,
                SearchResultType.Comments,
                SearchResultType.Users,
                SearchResultType.Urls,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.inboxListingTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label = value.toReadableName(),
                    trailingContent = {
                        Icon(
                            modifier = Modifier.size(IconSize.m),
                            imageVector = value.toIcon(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                )
            },
            onSelect = { index ->
                exploreResultTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeExploreResultType(value = values[index]))
                }
            },
        )
    }

    settingsContent?.also { content ->
        fileSystemManager.writeToFile(
            mimeType = SETTINGS_MIME_TYPE,
            name = SETTINGS_FILE_NAME,
            data = content,
        ) { success ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    if (success) {
                        successMessage
                    } else {
                        errorMessage
                    },
                )
            }
            settingsContent = null
        }
    }

    if (selectInboxPreviewMaxLinesBottomSheetOpened) {
        SelectNumberBottomSheet(
            title = LocalStrings.current.settingsInboxPreviewMaxLines,
            initialValue = uiState.inboxPreviewMaxLines,
            onSelect = { value ->
                selectInboxPreviewMaxLinesBottomSheetOpened = false
                if (value != null) {
                    model.reduce(
                        AdvancedSettingsMviModel.Intent.SelectInboxPreviewMaxLines(value = value.takeIf { it > 0 }),
                    )
                }
            },
        )
    }

    if (selectZombieModeAmount) {
        SliderBottomSheet(
            title = LocalStrings.current.settingsZombieModeScrollAmount,
            min = 0f,
            max = screenWidth,
            initial = uiState.zombieModeScrollAmount,
            onSelect = { value ->
                selectZombieModeAmount = false
                if (value != null) {
                    model.reduce(AdvancedSettingsMviModel.Intent.ChangeZombieScrollAmount(value))
                }
            },
        )
    }
}
