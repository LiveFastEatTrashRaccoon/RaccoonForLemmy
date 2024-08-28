package com.livefast.eattrash.raccoonforlemmy.feature.settings.colors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toDownVoteColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toFontScale
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toReplyColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toSaveColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUpVoteColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getAppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.choosecolor.CustomColorPickerDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.rememberCallback
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.rememberCallbackArgs
import com.livefast.eattrash.raccoonforlemmy.feature.settings.ui.components.SettingsColorRow
import com.livefast.eattrash.raccoonforlemmy.feature.settings.ui.components.SettingsMultiColorRow
import com.livefast.eattrash.raccoonforlemmy.unit.choosecolor.CommentBarThemeBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.choosefont.FontFamilyBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.choosefont.FontScaleBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal enum class CustomColorType {
    UpvoteColor,
    DownvoteColor,
    ReplyColor,
    SaveColor,
    None,
}

class SettingsColorAndFontScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<SettingsColorAndFontMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val settingsRepository = remember { getSettingsRepository() }
        val themeRepository = remember { getThemeRepository() }
        val appColorRepository = remember { getAppColorRepository() }
        val scrollState = rememberScrollState()
        val colorSchemeProvider = remember { getColorSchemeProvider() }
        val defaultTheme =
            if (isSystemInDarkTheme()) {
                UiTheme.Dark
            } else {
                UiTheme.Light
            }
        var uiFontSizeWorkaround by remember { mutableStateOf(true) }
        var uiThemeBottomSheetOpened by remember { mutableStateOf(false) }
        var customColorBottomSheetOpened by remember { mutableStateOf(false) }
        var voteThemeBottomSheetOpened by remember { mutableStateOf(false) }
        var customColorTypeSelection by remember { mutableStateOf(CustomColorType.None) }
        var commentBarColorsBottomSheetOpened by remember { mutableStateOf(false) }
        var fontFamilyBottomSheetOpened by remember { mutableStateOf(false) }
        var fontScaleBottomSheetOpened by remember { mutableStateOf(false) }
        var customColorPickerDialogOpened by remember { mutableStateOf(false) }

        LaunchedEffect(themeRepository) {
            themeRepository.uiFontScale
                .drop(1)
                .onEach {
                    uiFontSizeWorkaround = false
                    delay(50)
                    uiFontSizeWorkaround = true
                }.launchIn(this)
        }

        if (!uiFontSizeWorkaround) {
            return
        }

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.settingsColorsAndFonts,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
                            Image(
                                modifier =
                                    Modifier.onClick(
                                        onClick = {
                                            navigationCoordinator.popScreen()
                                        },
                                    ),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                    },
                )
            },
        ) { padding ->
            Box(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    // theme
                    SettingsRow(
                        title = LocalStrings.current.settingsUiTheme,
                        value = uiState.uiTheme.toReadableName(),
                        onTap = {
                            uiThemeBottomSheetOpened = true
                        }
                    )

                    // dynamic colors
                    if (uiState.supportsDynamicColors) {
                        SettingsSwitchRow(
                            title = LocalStrings.current.settingsDynamicColors,
                            value = uiState.dynamicColors,
                            onValueChanged =
                                rememberCallbackArgs(model) { value ->
                                    model.reduce(
                                        SettingsColorAndFontMviModel.Intent.ChangeDynamicColors(value),
                                    )
                                },
                        )
                    }
                    // random color
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsItemRandomThemeColor,
                        subtitle = LocalStrings.current.settingsSubtitleRandomThemeColor,
                        value = uiState.randomColor,
                        onValueChanged =
                            rememberCallbackArgs(model) { value ->
                                model.reduce(
                                    SettingsColorAndFontMviModel.Intent.ChangeRandomColor(value),
                                )
                            },
                    )

                    // custom scheme seed color
                    SettingsColorRow(
                        title = LocalStrings.current.settingsCustomSeedColor,
                        value =
                            uiState.customSeedColor ?: colorSchemeProvider
                                .getColorScheme(
                                    theme = uiState.uiTheme ?: defaultTheme,
                                    dynamic = uiState.dynamicColors,
                                ).primary,
                        onTap = {
                            customColorBottomSheetOpened = true
                        }
                    )

                    if (uiState.isLogged) {
                        // action colors
                        SettingsColorRow(
                            title = LocalStrings.current.settingsUpvoteColor,
                            value = uiState.upVoteColor ?: MaterialTheme.colorScheme.primary,
                            onTap = {
                                customColorTypeSelection = CustomColorType.UpvoteColor
                                voteThemeBottomSheetOpened = true
                            }
                        )
                        SettingsColorRow(
                            title = LocalStrings.current.settingsDownvoteColor,
                            value = uiState.downVoteColor ?: MaterialTheme.colorScheme.tertiary,
                            onTap = {
                                customColorTypeSelection = CustomColorType.DownvoteColor
                                voteThemeBottomSheetOpened = true
                            }
                        )
                        SettingsColorRow(
                            title = LocalStrings.current.settingsReplyColor,
                            value = uiState.replyColor ?: MaterialTheme.colorScheme.secondary,
                            onTap = {
                                customColorTypeSelection = CustomColorType.ReplyColor
                                voteThemeBottomSheetOpened = true
                            }
                        )
                        SettingsColorRow(
                            title = LocalStrings.current.settingsSaveColor,
                            value =
                                uiState.saveColor
                                    ?: MaterialTheme.colorScheme.secondaryContainer,
                            onTap = {
                                customColorTypeSelection = CustomColorType.SaveColor
                                voteThemeBottomSheetOpened = true
                            }
                        )
                    }

                    // comment bar theme
                    val commentBarColors =
                        themeRepository.getCommentBarColors(uiState.commentBarTheme)
                    SettingsMultiColorRow(
                        title = LocalStrings.current.settingsCommentBarTheme,
                        values = commentBarColors,
                        onTap =
                            rememberCallback {
                                val screen = CommentBarThemeBottomSheet()
                                navigationCoordinator.showBottomSheet(screen)
                            },
                    )

                    // font family
                    SettingsRow(
                        title = LocalStrings.current.settingsUiFontFamily,
                        value = uiState.uiFontFamily.toReadableName(),
                        onTap =
                            rememberCallback {
                                val sheet = FontFamilyBottomSheet()
                                navigationCoordinator.showBottomSheet(sheet)
                            },
                    )

                    // font scale
                    SettingsRow(
                        title = LocalStrings.current.settingsUiFontScale,
                        value = uiState.uiFontScale.toFontScale().toReadableName(),
                        onTap =
                            rememberCallback {
                                val sheet = FontScaleBottomSheet()
                                navigationCoordinator.showBottomSheet(sheet)
                            },
                    )
                }
            }
        }

        if (uiThemeBottomSheetOpened) {
            val items =
                listOf(
                    UiTheme.Light,
                    UiTheme.Dark,
                    UiTheme.Black,
                    null,
                )
            CustomModalBottomSheet(
                title = LocalStrings.current.settingsUiTheme,
                items = items.map {
                    CustomModalBottomSheetItem(
                        label = it.toReadableName(),
                        trailingContent = {
                            if (it != null) {
                                Icon(
                                    modifier = Modifier.size(IconSize.l),
                                    imageVector = it.toIcon(),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    )},
                onSelected = { index ->
                    uiThemeBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeTheme(items[index])
                        )
                    }
                },
            )
        }

        if (customColorBottomSheetOpened) {
            CustomModalBottomSheet(
                title = LocalStrings.current.settingsCustomSeedColor,
                items =
                    buildList {
                        this += appColorRepository.getColors().map { theme ->
                            CustomModalBottomSheetItem(
                                label = theme.toReadableName(),
                                trailingContent = {
                                    Box(
                                        modifier =
                                            Modifier
                                                .padding(start = Spacing.xs)
                                                .size(size = IconSize.l)
                                                .background(
                                                    color = theme.toColor(),
                                                    shape = CircleShape,
                                                ),
                                    )
                                },
                            )
                        }
                        this += CustomModalBottomSheetItem(
                            label = LocalStrings.current.settingsColorCustom,
                            trailingContent = {
                                Image(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                )
                            }
                        )
                    },
                onSelected = { index ->
                    if (index != null) {
                        customColorBottomSheetOpened = false
                        if (index in appColorRepository.getColors().indices) {
                            notificationCenter.send(
                                NotificationCenterEvent.ChangeColor(
                                    appColorRepository.getColors()[index].toColor()
                                )
                            )
                        } else {
                            customColorPickerDialogOpened = true
                        }
                    }
                    else {
                        customColorBottomSheetOpened = false
                    }
                },
            )
            if (customColorPickerDialogOpened) {
                val current =
                    settingsRepository.currentSettings.value.customSeedColor
                        ?.let { Color(it) }
                CustomColorPickerDialog(
                    initialValue = current ?: MaterialTheme.colorScheme.primary,
                    onClose = { newColor ->
                        customColorBottomSheetOpened = false
                        customColorPickerDialogOpened = false
                        if (newColor != null) {
                            notificationCenter.send(NotificationCenterEvent.ChangeColor(newColor))
                        }
                    },
                )
            }
        }

        if (voteThemeBottomSheetOpened) {
            val items =
                listOf(
                    CommentBarTheme.Blue,
                    CommentBarTheme.Green,
                    CommentBarTheme.Red,
                    CommentBarTheme.Rainbow,
                )
            CustomModalBottomSheet(
                title =
                    when(customColorTypeSelection) {
                        CustomColorType.UpvoteColor -> {
                            LocalStrings.current.settingsUpvoteColor
                        }
                        CustomColorType.DownvoteColor -> {
                            LocalStrings.current.settingsDownvoteColor
                        }
                        CustomColorType.ReplyColor -> {
                            LocalStrings.current.settingsReplyColor
                        }
                        CustomColorType.SaveColor -> {
                            LocalStrings.current.settingsSaveColor
                        }
                        else -> {
                            ""
                        }
                    },
                items =
                    buildList {
                        this += items.map { barTheme ->
                            CustomModalBottomSheetItem(
                                label = barTheme.toReadableName(),
                                trailingContent = {
                                    Box(
                                        modifier =
                                            Modifier
                                                .size(IconSize.l)
                                                .background(
                                                    color =
                                                        when(customColorTypeSelection) {
                                                            CustomColorType.UpvoteColor -> {
                                                                barTheme.toUpVoteColor()
                                                            }
                                                            CustomColorType.DownvoteColor -> {
                                                                barTheme.toDownVoteColor()
                                                            }
                                                            CustomColorType.ReplyColor -> {
                                                                barTheme.toReplyColor()
                                                            }
                                                            CustomColorType.SaveColor -> {
                                                                barTheme.toSaveColor()
                                                            }
                                                            else -> {
                                                                Color.Unspecified
                                                            }
                                                        },
                                                    shape = CircleShape,
                                                ),
                                    )
                                },
                            )
                        }
                        this += CustomModalBottomSheetItem(
                            label = LocalStrings.current.settingsColorCustom,
                            trailingContent = {
                                Image(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                )
                            }
                        )
                    },
                onSelected = { index ->
                    if (index != null) {
                        if (index in items.indices) {
                            voteThemeBottomSheetOpened = false
                            notificationCenter.send(
                                NotificationCenterEvent.ChangeActionColor(
                                    color =
                                    when (customColorTypeSelection) {
                                        CustomColorType.UpvoteColor -> {
                                            items.get(index).toUpVoteColor()
                                        }

                                        CustomColorType.DownvoteColor -> {
                                            items.get(index).toDownVoteColor()
                                        }

                                        CustomColorType.ReplyColor -> {
                                            items.get(index).toReplyColor()
                                        }

                                        CustomColorType.SaveColor -> {
                                            items.get(index).toSaveColor()
                                        }

                                        else -> {
                                            Color.Unspecified
                                        }
                                    },
                                    actionType = customColorTypeSelection.ordinal
                                )
                            )
                            customColorTypeSelection = CustomColorType.None
                        }
                        else {
                            customColorPickerDialogOpened = true
                        }
                    }
                },
            )
            if (customColorPickerDialogOpened) {
                val current =
                    settingsRepository.currentSettings.value.customSeedColor
                        ?.let { Color(it) }
                CustomColorPickerDialog(
                    initialValue = current ?: MaterialTheme.colorScheme.primary,
                    onClose = { newColor ->
                        voteThemeBottomSheetOpened = false
                        customColorPickerDialogOpened = false
                        if (newColor != null) {
                            notificationCenter.send(NotificationCenterEvent.ChangeColor(newColor))
                            NotificationCenterEvent.ChangeActionColor(
                                color = newColor,
                                actionType = customColorTypeSelection.ordinal
                            )
                        }
                        customColorTypeSelection = CustomColorType.None
                    },
                )
            }
        }

        if (commentBarColorsBottomSheetOpened) {}
        if (fontFamilyBottomSheetOpened) {}
        if (fontScaleBottomSheetOpened) {}
    }
}
