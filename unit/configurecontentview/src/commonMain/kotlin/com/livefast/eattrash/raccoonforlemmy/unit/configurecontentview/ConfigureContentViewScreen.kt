package com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toFontScale
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsIntValueRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.PostLayoutBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectNumberBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectNumberBottomSheetType
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.unit.choosefont.FontFamilyBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.choosefont.FontScaleBottomSheet

class ConfigureContentViewScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ConfigureContentViewMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val scrollState = rememberScrollState()
        var voteFormatBottomSheetOpened by remember { mutableStateOf(false) }

        Scaffold(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.settingsConfigureContent,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
                            IconButton(
                                onClick = {
                                    navigationCoordinator.popScreen()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = null,
                                )
                            }
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
                    SettingsHeader(
                        icon = Icons.Default.TextFormat,
                        title = LocalStrings.current.settingsConfigureText,
                    )

                    // content font family
                    SettingsRow(
                        title = LocalStrings.current.settingsContentFontFamily,
                        value = uiState.contentFontFamily.toReadableName(),
                        onTap = {
                            val sheet = FontFamilyBottomSheet(content = true)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // content font scale
                    SettingsRow(
                        title = LocalStrings.current.settingsTitleFontScale,
                        value =
                            uiState.contentFontScale.title
                                .toFontScale()
                                .toReadableName(),
                        onTap = {
                            val sheet = FontScaleBottomSheet(contentClass = ContentFontClass.Title)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    SettingsRow(
                        title = LocalStrings.current.settingsContentFontScale,
                        value =
                            uiState.contentFontScale.body
                                .toFontScale()
                                .toReadableName(),
                        onTap = {
                            val sheet = FontScaleBottomSheet(contentClass = ContentFontClass.Body)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    SettingsRow(
                        title = LocalStrings.current.settingsCommentFontScale,
                        value =
                            uiState.contentFontScale.comment
                                .toFontScale()
                                .toReadableName(),
                        onTap = {
                            val sheet =
                                FontScaleBottomSheet(contentClass = ContentFontClass.Comment)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    SettingsRow(
                        title = LocalStrings.current.settingsAncillaryFontScale,
                        value =
                            uiState.contentFontScale.ancillary
                                .toFontScale()
                                .toReadableName(),
                        onTap = {
                            val sheet =
                                FontScaleBottomSheet(contentClass = ContentFontClass.AncillaryText)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.Style,
                        title = LocalStrings.current.settingsConfigureCustomizations,
                    )

                    // post layout
                    SettingsRow(
                        title = LocalStrings.current.settingsPostLayout,
                        value = uiState.postLayout.toReadableName(),
                        onTap = {
                            val sheet = PostLayoutBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // body max lines in full layout
                    if (uiState.postLayout == PostLayout.Full) {
                        SettingsRow(
                            title = LocalStrings.current.settingsPostBodyMaxLines,
                            value =
                                if (uiState.postBodyMaxLines == null) {
                                    LocalStrings.current.settingsPostBodyMaxLinesUnlimited
                                } else {
                                    uiState.postBodyMaxLines.toString()
                                },
                            onTap = {
                                val screen =
                                    SelectNumberBottomSheet(
                                        type = SelectNumberBottomSheetType.PostBodyMaxLines,
                                        initialValue = uiState.postBodyMaxLines,
                                    )
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                    }

                    // prefer user nicknames
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsPreferUserNicknames,
                        value = uiState.preferUserNicknames,
                        onValueChanged = { value ->
                            model.reduce(
                                ConfigureContentViewMviModel.Intent.ChangePreferUserNicknames(value),
                            )
                        },
                    )

                    // full height images
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsFullHeightImages,
                        value = uiState.fullHeightImages,
                        onValueChanged = { value ->
                            model.reduce(
                                ConfigureContentViewMviModel.Intent.ChangeFullHeightImages(value),
                            )
                        },
                    )

                    // full width images
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsFullWidthImages,
                        value = uiState.fullWidthImages,
                        onValueChanged = { value ->
                            model.reduce(
                                ConfigureContentViewMviModel.Intent.ChangeFullWidthImages(value),
                            )
                        },
                    )

                    // vote format
                    SettingsRow(
                        title = LocalStrings.current.settingsVoteFormat,
                        value = uiState.voteFormat.toReadableName(),
                        onTap = {
                            voteFormatBottomSheetOpened = true
                        },
                    )

                    // comment bar thickness
                    SettingsIntValueRow(
                        title = LocalStrings.current.settingsCommentBarThickness,
                        value = uiState.commentBarThickness,
                        onIncrement = {
                            model.reduce(ConfigureContentViewMviModel.Intent.IncrementCommentBarThickness)
                        },
                        onDecrement = {
                            model.reduce(ConfigureContentViewMviModel.Intent.DecrementCommentBarThickness)
                        },
                    )

                    // comment indent amount
                    SettingsIntValueRow(
                        title = LocalStrings.current.settingsCommentIndentAmount,
                        value = uiState.commentIndentAmount,
                        onIncrement = {
                            model.reduce(ConfigureContentViewMviModel.Intent.IncrementCommentIndentAmount)
                        },
                        onDecrement = {
                            model.reduce(ConfigureContentViewMviModel.Intent.DecrementCommentIndentAmount)
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.Preview,
                        title = LocalStrings.current.createPostTabPreview,
                    )
                    // preview
                    ContentPreview(
                        modifier = Modifier.padding(top = Spacing.xxs),
                        postLayout = uiState.postLayout,
                        preferNicknames = uiState.preferUserNicknames,
                        showScores = uiState.voteFormat != VoteFormat.Hidden,
                        voteFormat = uiState.voteFormat,
                        fullHeightImage = uiState.fullHeightImages,
                        fullWidthImage = uiState.fullWidthImages,
                        commentBarThickness = uiState.commentBarThickness,
                        commentIndentAmount = uiState.commentIndentAmount,
                        downVoteEnabled = uiState.downVoteEnabled,
                    )

                    Spacer(modifier = Modifier.height(Spacing.xxxl))
                }
            }
        }

        if (voteFormatBottomSheetOpened) {
            val values =
                listOf(
                    VoteFormat.Aggregated,
                    VoteFormat.Separated,
                    VoteFormat.Percentage,
                    VoteFormat.Hidden,
                )
            CustomModalBottomSheet(
                title = LocalStrings.current.inboxListingTypeTitle,
                items =
                    values.map { value ->
                        CustomModalBottomSheetItem(label = value.toReadableName())
                    },
                onSelected = { index ->
                    voteFormatBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeVoteFormat(value = values[index]),
                        )
                    }
                },
            )
        }
    }
}
