package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.PredictiveBackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsFormattedInfo
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsImageInfo
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsTextualInfo
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.EditFormattedInfoDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.EditTextualInfoDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.components.DeleteAccountDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountSettingsScreen(modifier: Modifier = Modifier) {
    val model: AccountSettingsMviModel = getViewModel<AccountSettingsViewModel>()
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    val themeRepository = remember { getThemeRepository() }
    val contentFontFamily by themeRepository.contentFontFamily.collectAsState()
    val contentTypography = contentFontFamily.toTypography()
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    var openDisplayNameEditDialog by remember { mutableStateOf(false) }
    var openEmailEditDialog by remember { mutableStateOf(false) }
    var openMatrixUserIdEditDialog by remember { mutableStateOf(false) }
    var openBioEditDialog by remember { mutableStateOf(false) }
    val successMessage = LocalStrings.current.messageOperationSuccessful
    val errorMessage = LocalStrings.current.messageGenericError
    val snackbarHostState = remember { SnackbarHostState() }
    val galleryHelper = remember { getGalleryHelper() }
    var openAvatarPicker by remember { mutableStateOf(false) }
    var openBannerPicker by remember { mutableStateOf(false) }
    var confirmBackWithUnsavedChangesDialog by remember { mutableStateOf(false) }
    var defaultListingTypeBottomSheetOpened by remember { mutableStateOf(false) }
    var sortBottomSheetOpened by remember { mutableStateOf(false) }
    var deleteAccountDialogOpen by remember { mutableStateOf(false) }
    var deleteAccountValidationError by remember { mutableStateOf<ValidationError?>(null) }

    LaunchedEffect(model) {
        model.effects
            .onEach { evt ->
                when (evt) {
                    AccountSettingsMviModel.Effect.Failure ->
                        snackbarHostState.showSnackbar(errorMessage)

                    AccountSettingsMviModel.Effect.Success ->
                        snackbarHostState.showSnackbar(successMessage)

                    AccountSettingsMviModel.Effect.CloseDeleteAccountDialog ->
                        deleteAccountDialogOpen = false

                    is AccountSettingsMviModel.Effect.SetDeleteAccountValidationError ->
                        deleteAccountValidationError = evt.error

                    AccountSettingsMviModel.Effect.Close -> navigationCoordinator.pop()
                }
            }.launchIn(this)
    }

    PredictiveBackHandler(uiState.hasUnsavedChanges) {
        confirmBackWithUnsavedChangesDialog = true
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = LocalStrings.current.settingsWebPreferences,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    if (navigationCoordinator.canPop.value) {
                        IconButton(
                            onClick = {
                                if (uiState.hasUnsavedChanges) {
                                    confirmBackWithUnsavedChangesDialog = true
                                } else {
                                    navigationCoordinator.pop()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = LocalStrings.current.actionGoBack,
                            )
                        }
                    }
                },
                actions = {
                    val transition = rememberInfiniteTransition()
                    val iconRotate by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec =
                        InfiniteRepeatableSpec(
                            animation = tween(1000),
                        ),
                    )
                    Icon(
                        modifier =
                        Modifier
                            .padding(horizontal = Spacing.xs)
                            .then(
                                if (!uiState.loading) {
                                    Modifier
                                } else {
                                    Modifier.rotate(iconRotate)
                                },
                            ),
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    enabled = uiState.hasUnsavedChanges,
                    onClick = {
                        model.reduce(AccountSettingsMviModel.Intent.Submit)
                    },
                ) {
                    Text(text = LocalStrings.current.actionSave)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
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
        Column(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                ).then(
                    if (settings.hideNavigationBarWhileScrolling) {
                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        Modifier
                    },
                ),
        ) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(scrollState),
            ) {
                SettingsHeader(
                    icon = Icons.Default.AccountCircle,
                    title = LocalStrings.current.settingsWebHeaderPersonal,
                )

                // avatar
                val avatarSize = IconSize.xxl
                SettingsImageInfo(
                    title = LocalStrings.current.settingsWebAvatar,
                    imageModifier =
                    Modifier
                        .size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2)),
                    url = uiState.avatar,
                    onEdit = {
                        openAvatarPicker = true
                    },
                )

                // banner
                SettingsImageInfo(
                    title = LocalStrings.current.settingsWebBanner,
                    imageModifier = Modifier.fillMaxWidth().aspectRatio(3.5f),
                    contentScale = ContentScale.Crop,
                    url = uiState.banner,
                    onEdit = {
                        openBannerPicker = true
                    },
                )

                // display name
                SettingsTextualInfo(
                    title = LocalStrings.current.settingsWebDisplayName,
                    value = uiState.displayName,
                    valueStyle = contentTypography.bodyMedium,
                    onEdit = {
                        openDisplayNameEditDialog = true
                    },
                )

                // email
                SettingsTextualInfo(
                    title = LocalStrings.current.settingsWebEmail,
                    value = uiState.email,
                    valueStyle = contentTypography.bodyMedium,
                    onEdit = {
                        openEmailEditDialog = true
                    },
                )

                // Matrix user ID
                SettingsTextualInfo(
                    title = LocalStrings.current.settingsWebMatrix,
                    value = uiState.matrixUserId,
                    valueStyle =
                    contentTypography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                    ),
                    onEdit = {
                        openMatrixUserIdEditDialog = true
                    },
                )

                // bio
                SettingsFormattedInfo(
                    title = LocalStrings.current.settingsWebBio,
                    value = uiState.bio,
                    onEdit = {
                        openBioEditDialog = true
                    },
                )

                // bots account
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsWebBot,
                    value = uiState.bot,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeBot(value))
                    },
                )

                SettingsHeader(
                    icon = Icons.AutoMirrored.Default.Article,
                    title = LocalStrings.current.settingsWebHeaderContents,
                )

                // default listing type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultListingType,
                    value = uiState.defaultListingType.toReadableName(),
                    onTap = {
                        defaultListingTypeBottomSheetOpened = true
                    },
                )

                // default sort type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultPostSortType,
                    value = uiState.defaultSortType.toReadableName(),
                    onTap = {
                        sortBottomSheetOpened = true
                    },
                )

                // show bots
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsWebShowBot,
                    value = uiState.showBotAccounts,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowBotAccounts(value))
                    },
                )

                // show NSFW
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsWebShowNsfw,
                    value = uiState.showNsfw,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowNsfw(value))
                    },
                )

                // show read posts
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsWebShowRead,
                    value = uiState.showReadPosts,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowReadPosts(value))
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.ThumbsUpDown,
                    title = LocalStrings.current.settingsVoteFormat,
                )

                // show scores
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsShowScores,
                    value = uiState.showScores,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowScores(value))
                    },
                )

                // show positive votes
                SettingsSwitchRow(
                    title = LocalStrings.current.actionUpvote,
                    value = uiState.showUpVotes,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowUpVotes(value))
                    },
                )

                // show negative votes
                SettingsSwitchRow(
                    title = LocalStrings.current.actionDownvote,
                    value = uiState.showDownVotes,
                    onChangeValue = { value ->
                        model.reduce(AccountSettingsMviModel.Intent.ChangeShowDownVotes(value))
                    },
                )

                // show vote percentage
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsVoteFormatPercentage,
                    value = uiState.showUpVotePercentage,
                    onChangeValue = { value ->
                        model.reduce(
                            AccountSettingsMviModel.Intent.ChangeShowUpVotePercentage(
                                value,
                            ),
                        )
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.Notifications,
                    title = LocalStrings.current.settingsWebHeaderNotifications,
                )

                // email notifications
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsWebEmailNotifications,
                    value = uiState.sendNotificationsToEmail,
                    onChangeValue = { value ->
                        model.reduce(
                            AccountSettingsMviModel.Intent.ChangeSendNotificationsToEmail(
                                value,
                            ),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(Spacing.m))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(
                        onClick = {
                            deleteAccountDialogOpen = true
                        },
                    ) {
                        Text(text = LocalStrings.current.actionDeleteAccount)
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.m))
            }
        }
    }

    if (uiState.operationInProgress) {
        ProgressHud()
    }

    if (openDisplayNameEditDialog) {
        EditTextualInfoDialog(
            title = LocalStrings.current.postActionEdit,
            label = LocalStrings.current.settingsWebDisplayName,
            value = uiState.displayName,
            onClose = { newValue ->
                openDisplayNameEditDialog = false
                newValue?.also {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeDisplayName(it))
                }
            },
        )
    }

    if (openEmailEditDialog) {
        EditTextualInfoDialog(
            title = LocalStrings.current.postActionEdit,
            label = LocalStrings.current.settingsWebEmail,
            value = uiState.email,
            onClose = { newValue ->
                openEmailEditDialog = false
                newValue?.also {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeEmail(it))
                }
            },
        )
    }

    if (openMatrixUserIdEditDialog) {
        EditTextualInfoDialog(
            title = LocalStrings.current.postActionEdit,
            label = LocalStrings.current.settingsWebMatrix,
            value = uiState.matrixUserId,
            onClose = { newValue ->
                openMatrixUserIdEditDialog = false
                newValue?.also {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeMatrixUserId(it))
                }
            },
        )
    }

    if (openBioEditDialog) {
        EditFormattedInfoDialog(
            title = LocalStrings.current.settingsWebBio,
            value = uiState.bio,
            onClose = { newValue ->
                openBioEditDialog = false
                newValue?.also {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeBio(it))
                }
            },
        )
    }

    if (openAvatarPicker) {
        galleryHelper.getImageFromGallery { bytes ->
            openAvatarPicker = false
            if (bytes.isNotEmpty()) {
                model.reduce(AccountSettingsMviModel.Intent.AvatarSelected(bytes))
            }
        }
    }
    if (openBannerPicker) {
        galleryHelper.getImageFromGallery { bytes ->
            openBannerPicker = false
            if (bytes.isNotEmpty()) {
                model.reduce(AccountSettingsMviModel.Intent.BannerSelected(bytes))
            }
        }
    }

    if (confirmBackWithUnsavedChangesDialog) {
        AlertDialog(
            onDismissRequest = {
                confirmBackWithUnsavedChangesDialog = false
            },
            dismissButton = {
                Button(
                    onClick = {
                        confirmBackWithUnsavedChangesDialog = false
                    },
                ) {
                    Text(text = LocalStrings.current.buttonNoStay)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmBackWithUnsavedChangesDialog = false
                        navigationCoordinator.pop()
                    },
                ) {
                    Text(text = LocalStrings.current.buttonYesQuit)
                }
            },
            text = {
                Text(text = LocalStrings.current.messageUnsavedChanges)
            },
        )
    }

    if (defaultListingTypeBottomSheetOpened) {
        val values =
            buildList {
                this += ListingType.Subscribed
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
                defaultListingTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeFeedType(value = values[index]))
                }
            },
        )
    }

    if (sortBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypes,
            expandTop = true,
            onSelect = { value ->
                sortBottomSheetOpened = false
                if (value != null) {
                    model.reduce(AccountSettingsMviModel.Intent.ChangeSortType(value = value))
                }
            },
        )
    }

    if (deleteAccountDialogOpen) {
        DeleteAccountDialog(
            validationError = deleteAccountValidationError,
            onDismiss = {
                deleteAccountDialogOpen = false
            },
            onConfirm = { text, deleteContent ->
                model.reduce(
                    AccountSettingsMviModel.Intent.DeleteAccount(
                        deleteContent = deleteContent,
                        password = text,
                    ),
                )
            },
        )
    }
}
