package com.livefast.eattrash.raccoonforlemmy.unit.createpost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CreatePostSection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.TextFormattingBar
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectLanguageDialog
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.safeImePadding
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.di.CreatePostMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    draftId: Long? = null,
    communityId: Long? = null,
    editedPostId: Long? = null,
    crossPostId: Long? = null,
    initialText: String? = null,
    initialTitle: String? = null,
    initialUrl: String? = null,
    initialNsfw: Boolean? = null,
    forceCommunitySelection: Boolean = false,
) {
    val model: CreatePostMviModel =
        getViewModel<CreatePostViewModel>(
            CreatePostMviModelParams(
                editedPostId = editedPostId ?: 0L,
                crossPostId = crossPostId ?: 0L,
                draftId = draftId ?: 0L,
            ),
        )
    val uiState by model.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val genericError = LocalStrings.current.messageGenericError
    val autofillEmpty = LocalStrings.current.messageNoResult
    val notificationCenter = remember { getNotificationCenter() }
    val galleryHelper = remember { getGalleryHelper() }
    val crossPostText = LocalStrings.current.createPostCrossPostText
    val crossPost = uiState.crossPost
    val editedPost = uiState.editedPost
    val bodyFocusRequester = remember { FocusRequester() }
    val urlFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val navigationCoordinator = remember { getNavigationCoordinator() }
    var openImagePicker by remember { mutableStateOf(false) }
    var openImagePickerInBody by remember { mutableStateOf(false) }
    if (openImagePicker) {
        galleryHelper.getImageFromGallery { bytes ->
            openImagePicker = false
            if (bytes.isNotEmpty()) {
                model.reduce(CreatePostMviModel.Intent.ImageSelected(bytes))
            }
        }
    }
    if (openImagePickerInBody) {
        galleryHelper.getImageFromGallery { bytes ->
            openImagePickerInBody = false
            if (bytes.isNotEmpty()) {
                model.reduce(CreatePostMviModel.Intent.InsertImageInBody(bytes))
            }
        }
    }

    var openSelectCommunity by remember { mutableStateOf(false) }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val themeRepository = remember { getThemeRepository() }
    val contentFontFamily by themeRepository.contentFontFamily.collectAsState()
    val typography = contentFontFamily.toTypography()
    var selectLanguageDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(editedPost, crossPost) {
        editedPost?.community?.id?.also { communityId ->
            model.reduce(
                CreatePostMviModel.Intent.SetCommunity(
                    CommunityModel(id = communityId),
                ),
            )
        }
        val referencePost = uiState.editedPost ?: uiState.crossPost
        if (referencePost != null) {
            model.reduce(CreatePostMviModel.Intent.SetTitle(referencePost.title))
            model.reduce(CreatePostMviModel.Intent.SetUrl(referencePost.url.orEmpty()))
            model.reduce(CreatePostMviModel.Intent.ChangeBodyValue(TextFieldValue(referencePost.text)))
            model.reduce(CreatePostMviModel.Intent.ChangeLanguage(referencePost.languageId))
        }

        if (uiState.bodyValue.text.isEmpty() && uiState.title.isEmpty()) {
            val text =
                buildString {
                    when {
                        crossPost != null -> {
                            append(crossPostText)
                            append(" ")
                            append(crossPost.originalUrl)
                        }

                        editedPost != null -> {
                            append(editedPost.text)
                        }

                        !initialText.isNullOrEmpty() -> {
                            append(initialText)
                        }
                    }
                }
            model.reduce(CreatePostMviModel.Intent.ChangeBodyValue(TextFieldValue(text)))

            initialTitle?.also { title ->
                model.reduce(CreatePostMviModel.Intent.SetTitle(title))
            }
            initialUrl?.also { url ->
                model.reduce(CreatePostMviModel.Intent.SetUrl(url))
            }
            initialNsfw?.also { nsfw ->
                model.reduce(CreatePostMviModel.Intent.ChangeNsfw(nsfw))
            }
        }
    }

    LaunchedEffect(model, communityId) {
        communityId?.also { communityId ->
            model.reduce(CreatePostMviModel.Intent.SetCommunity(CommunityModel(id = communityId)))
        }
    }
    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    is CreatePostMviModel.Effect.Failure ->
                        snackbarHostState.showSnackbar(effect.message ?: genericError)

                    CreatePostMviModel.Effect.Success -> {
                        notificationCenter.send(
                            event = NotificationCenterEvent.PostCreated,
                        )
                        navigationCoordinator.pop()
                    }

                    CreatePostMviModel.Effect.DraftSaved -> navigationCoordinator.pop()

                    CreatePostMviModel.Effect.AutoFillError ->
                        snackbarHostState.showSnackbar(genericError)

                    CreatePostMviModel.Effect.AutoFillEmpty ->
                        snackbarHostState.showSnackbar(autofillEmpty)
                }
            }.launchIn(this)
    }
    LaunchedEffect(notificationCenter) {
        notificationCenter
            .subscribe(NotificationCenterEvent.SelectCommunity::class)
            .onEach { evt ->
                model.reduce(CreatePostMviModel.Intent.SetCommunity(evt.model))
                focusManager.clearFocus()
            }.launchIn(this)
        notificationCenter
            .subscribe(NotificationCenterEvent.CloseDialog::class)
            .onEach {
                if (openSelectCommunity) {
                    openSelectCommunity = false
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier =
        modifier
            .navigationBarsPadding()
            .safeImePadding(),
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationCoordinator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = LocalStrings.current.buttonClose,
                        )
                    }
                },
                title = {
                    Text(
                        text =
                        when {
                            editedPost != null -> LocalStrings.current.editPostTitle
                            else -> LocalStrings.current.createPostTitle
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    if (uiState.editedPost == null) {
                        IconButton(
                            onClick = {
                                model.reduce(CreatePostMviModel.Intent.SaveDraft)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = LocalStrings.current.actionSave,
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            model.reduce(CreatePostMviModel.Intent.Send)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Send,
                            contentDescription = LocalStrings.current.actionSend,
                        )
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
        bottomBar = {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = Spacing.xs),
            ) {
                if (uiState.currentUser.isNotEmpty()) {
                    Text(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.m),
                        text =
                        buildString {
                            append(LocalStrings.current.postReplySourceAccount)
                            append(" ")
                            append(uiState.currentUser)
                            if (uiState.currentInstance.isNotEmpty()) {
                                append("@")
                                append(uiState.currentInstance)
                            }
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.End,
                    )
                }

                if (uiState.section == CreatePostSection.Edit) {
                    TextFormattingBar(
                        modifier =
                        Modifier.padding(
                            top = Spacing.s,
                            start = Spacing.s,
                            end = Spacing.s,
                            bottom = Spacing.xs,
                        ),
                        textFieldValue = uiState.bodyValue,
                        onChangeTextFieldValue = { value ->
                            model.reduce(CreatePostMviModel.Intent.ChangeBodyValue(value))
                        },
                        onSelectImage = {
                            openImagePickerInBody = true
                        },
                        currentLanguageId = uiState.currentLanguageId,
                        availableLanguages = uiState.availableLanguages,
                        onSelectLanguage = {
                            selectLanguageDialogOpen = true
                        },
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ).consumeWindowInsets(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // community
            if (forceCommunitySelection) {
                TextField(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged { state ->
                            if (state.hasFocus) {
                                openSelectCommunity = true
                            }
                        },
                    colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    label = {
                        Text(
                            text = LocalStrings.current.createPostCommunity,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                        )
                    },
                    textStyle = typography.bodyMedium,
                    value = uiState.communityInfo,
                    readOnly = true,
                    singleLine = true,
                    onValueChange = {},
                    isError = uiState.communityError != null,
                    supportingText = {
                        val error = uiState.communityError
                        if (error != null) {
                            Text(
                                text = error.toReadableMessage(),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )
            }

            // title
            TextField(
                modifier = Modifier.fillMaxWidth(),
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                label = {
                    Text(
                        text = LocalStrings.current.createPostName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                textStyle = typography.titleMedium,
                value = uiState.title,
                singleLine = true,
                trailingIcon = {
                    if (uiState.url.isNotBlank()) {
                        Text(
                            modifier =
                            Modifier
                                .padding(horizontal = Spacing.s)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(CornerSize.m),
                                ).padding(Spacing.xs)
                                .onClick(
                                    onClick = {
                                        model.reduce(CreatePostMviModel.Intent.AutoFillTitle)
                                    },
                                ),
                            text = "auto".uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                keyboardActions =
                KeyboardActions(
                    onNext = {
                        urlFocusRequester.requestFocus()
                    },
                ),
                onValueChange = { value ->
                    model.reduce(CreatePostMviModel.Intent.SetTitle(value))
                },
                isError = uiState.titleError != null,
                supportingText = {
                    val error = uiState.titleError
                    if (error != null) {
                        Text(
                            text = error.toReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )

            // image
            TextField(
                modifier = Modifier.fillMaxWidth().focusRequester(urlFocusRequester),
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                label = {
                    Text(
                        text = LocalStrings.current.createPostUrl,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier =
                        Modifier.onClick(
                            onClick = {
                                openImagePicker = true
                            },
                        ),
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                    )
                },
                textStyle =
                typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                ),
                value = uiState.url,
                singleLine = true,
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions =
                KeyboardActions(
                    onNext = {
                        bodyFocusRequester.requestFocus()
                    },
                ),
                onValueChange = { value ->
                    model.reduce(CreatePostMviModel.Intent.SetUrl(value))
                },
                isError = uiState.urlError != null,
                supportingText = {
                    val error = uiState.urlError
                    if (error != null) {
                        Text(
                            text = error.toReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )

            // NSFW
            Row(
                modifier =
                Modifier.fillMaxWidth().padding(
                    vertical = Spacing.s,
                    horizontal = Spacing.m,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = LocalStrings.current.createPostNsfw,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.nsfw,
                    onCheckedChange = { value ->
                        model.reduce(CreatePostMviModel.Intent.ChangeNsfw(value))
                    },
                )
            }

            SectionSelector(
                titles =
                listOf(
                    LocalStrings.current.createPostTabEditor,
                    LocalStrings.current.createPostTabPreview,
                ),
                currentSection =
                when (uiState.section) {
                    CreatePostSection.Preview -> 1
                    else -> 0
                },
                onSectionSelected = { id ->
                    val section =
                        when (id) {
                            1 -> CreatePostSection.Preview
                            else -> CreatePostSection.Edit
                        }
                    model.reduce(CreatePostMviModel.Intent.ChangeSection(section))
                },
            )

            if (uiState.section == CreatePostSection.Edit) {
                TextField(
                    modifier =
                    Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .focusRequester(bodyFocusRequester),
                    colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    label = {
                        Text(
                            text = LocalStrings.current.createPostBody,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    textStyle = typography.bodyMedium,
                    value = uiState.bodyValue,
                    keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        autoCorrectEnabled = true,
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    onValueChange = { value ->
                        model.reduce(CreatePostMviModel.Intent.ChangeBodyValue(value))
                    },
                    isError = uiState.bodyError != null,
                    supportingText = {
                        val error = uiState.bodyError
                        if (error != null) {
                            Text(
                                text = error.toReadableMessage(),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )
            } else {
                val post =
                    PostModel(
                        text = uiState.bodyValue.text,
                        title = uiState.title,
                        url = uiState.url,
                        thumbnailUrl = uiState.url,
                    )

                PostCard(
                    post = post,
                    postLayout = uiState.postLayout,
                    fullHeightImage = uiState.fullHeightImages,
                    fullWidthImage = uiState.fullWidthImages,
                    includeFullBody = true,
                    voteFormat = uiState.voteFormat,
                    autoLoadImages = uiState.autoLoadImages,
                    preferNicknames = uiState.preferNicknames,
                    showScores = uiState.showScores,
                    downVoteEnabled = uiState.downVoteEnabled,
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xxxl))
        }

        if (selectLanguageDialogOpen) {
            SelectLanguageDialog(
                languages = uiState.availableLanguages,
                currentLanguageId = uiState.currentLanguageId,
                onSelect = { langId ->
                    model.reduce(CreatePostMviModel.Intent.ChangeLanguage(langId))
                    selectLanguageDialogOpen = false
                },
                onDismiss = {
                    selectLanguageDialogOpen = false
                },
            )
        }

        if (uiState.loading) {
            ProgressHud()
        }

        if (openSelectCommunity) {
            SelectCommunityDialog()
        }
    }
}
