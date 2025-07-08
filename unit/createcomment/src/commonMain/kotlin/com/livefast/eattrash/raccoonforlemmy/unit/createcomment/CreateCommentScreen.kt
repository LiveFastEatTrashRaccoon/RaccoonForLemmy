package com.livefast.eattrash.raccoonforlemmy.unit.createcomment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CreatePostSection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.TextFormattingBar
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectLanguageDialog
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.safeImePadding
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.di.CreateCommentMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommentScreen(
    modifier: Modifier = Modifier,
    draftId: Long? = null,
    originalPostId: Long? = null,
    originalCommentId: Long? = null,
    editedCommentId: Long? = null,
    initialText: String? = null,
) {
    val model: CreateCommentMviModel =
        getViewModel<CreateCommentViewModel>(
            CreateCommentMviModelParams(
                postId = originalPostId ?: 0L,
                parentId = originalCommentId ?: 0L,
                editedCommentId = editedCommentId ?: 0L,
                draftId = draftId ?: 0L,
            ),
        )
    val uiState by model.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val genericError = LocalStrings.current.messageGenericError
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val notificationCenter = remember { getNotificationCenter() }
    val galleryHelper = remember { getGalleryHelper() }
    var openImagePicker by remember { mutableStateOf(false) }
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val themeRepository = remember { getThemeRepository() }
    val contentFontFamily by themeRepository.contentFontFamily.collectAsState()
    val typography = contentFontFamily.toTypography()
    var selectLanguageDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.editedComment) {
        uiState.editedComment?.also { editedComment ->
            model.reduce(CreateCommentMviModel.Intent.ChangeLanguage(editedComment.languageId))
            val newValue = TextFieldValue(text = editedComment.text.orEmpty())
            model.reduce(CreateCommentMviModel.Intent.ChangeTextValue(newValue))
        }
    }
    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    is CreateCommentMviModel.Effect.Failure -> {
                        snackbarHostState.showSnackbar(effect.message ?: genericError)
                    }

                    is CreateCommentMviModel.Effect.Success -> {
                        notificationCenter.send(event = NotificationCenterEvent.CommentCreated)
                        uiState.originalPost?.also { originalPost ->
                            notificationCenter.send(
                                event =
                                NotificationCenterEvent.PostUpdated(
                                    originalPost.copy(
                                        comments =
                                        if (effect.new) {
                                            originalPost.comments + 1
                                        } else {
                                            originalPost.comments
                                        },
                                    ),
                                ),
                            )
                        }
                        navigationCoordinator.pop()
                    }

                    CreateCommentMviModel.Effect.DraftSaved -> navigationCoordinator.pop()
                }
            }.launchIn(this)
    }
    LaunchedEffect(initialText) {
        if (uiState.textValue.text.isEmpty() && !initialText.isNullOrEmpty()) {
            model.reduce(CreateCommentMviModel.Intent.ChangeTextValue(TextFieldValue(initialText)))
        }
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
                            uiState.editedComment != null -> {
                                LocalStrings.current.editCommentTitle
                            }

                            else -> {
                                LocalStrings.current.createCommentTitle
                            }
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    if (uiState.editedComment == null) {
                        IconButton(
                            onClick = {
                                model.reduce(CreateCommentMviModel.Intent.SaveDraft)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = LocalStrings.current.actionSave,
                            )
                        }
                    }
                    IconButton(
                        modifier = Modifier.padding(horizontal = Spacing.xs),
                        onClick = {
                            model.reduce(CreateCommentMviModel.Intent.Send)
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
        bottomBar = {
            // bottom part with user name and toolbar
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
                            .padding(
                                start = Spacing.m,
                                end = Spacing.m,
                                bottom = Spacing.s,
                            ),
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

                TextFormattingBar(
                    modifier =
                    Modifier
                        .padding(
                            top = Spacing.s,
                            start = Spacing.s,
                            end = Spacing.s,
                        ),
                    textFieldValue = uiState.textValue,
                    onChangeTextFieldValue = { value ->
                        model.reduce(CreateCommentMviModel.Intent.ChangeTextValue(value))
                    },
                    onSelectImage = {
                        openImagePicker = true
                    },
                    currentLanguageId = uiState.currentLanguageId,
                    availableLanguages = uiState.availableLanguages,
                    onSelectLanguage = {
                        selectLanguageDialogOpen = true
                    },
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
                ).consumeWindowInsets(padding),
        ) {
            // reference post or comment
            Box(
                modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                val referenceModifier =
                    Modifier.padding(
                        horizontal = Spacing.s,
                        vertical = Spacing.xxs,
                    )
                val originalComment = uiState.originalComment
                val originalPost = uiState.originalPost
                when {
                    originalComment != null -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                        ) {
                            CommentCard(
                                modifier = referenceModifier,
                                comment = originalComment,
                                preferNicknames = uiState.preferNicknames,
                                indentAmount = 0,
                                voteFormat = uiState.voteFormat,
                                autoLoadImages = uiState.autoLoadImages,
                                showScores = uiState.showScores,
                                downVoteEnabled = uiState.downVoteEnabled,
                                options =
                                buildList {
                                    add(
                                        Option(
                                            OptionId.SeeRaw,
                                            LocalStrings.current.postActionSeeRaw,
                                        ),
                                    )
                                },
                                onSelectOption = {
                                    rawContent = originalComment
                                },
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                        }
                    }

                    originalPost != null -> {
                        PostCard(
                            modifier = referenceModifier,
                            postLayout =
                            if (uiState.postLayout == PostLayout.Card) {
                                uiState.postLayout
                            } else {
                                PostLayout.Full
                            },
                            fullHeightImage = uiState.fullHeightImages,
                            fullWidthImage = uiState.fullWidthImages,
                            post = originalPost,
                            blurNsfw = false,
                            includeFullBody = true,
                            voteFormat = uiState.voteFormat,
                            autoLoadImages = uiState.autoLoadImages,
                            preferNicknames = uiState.preferNicknames,
                            showScores = uiState.showScores,
                            downVoteEnabled = uiState.downVoteEnabled,
                            options =
                            buildList {
                                add(
                                    Option(
                                        OptionId.SeeRaw,
                                        LocalStrings.current.postActionSeeRaw,
                                    ),
                                )
                            },
                            onSelectOption = {
                                rawContent = originalPost
                            },
                        )
                    }
                }
            }

            // form fields
            Column(
                modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
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
                        model.reduce(CreateCommentMviModel.Intent.ChangeSection(section))
                    },
                )

                if (uiState.section == CreatePostSection.Edit) {
                    TextField(
                        modifier =
                        Modifier
                            .height(400.dp)
                            .fillMaxWidth(),
                        colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                        label = {
                            Text(
                                text = LocalStrings.current.createCommentBody,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        textStyle = typography.bodyMedium,
                        value = uiState.textValue,
                        keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrectEnabled = true,
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                        onValueChange = { value ->
                            model.reduce(CreateCommentMviModel.Intent.ChangeTextValue(value))
                        },
                        isError = uiState.textError != null,
                        supportingText = {
                            val error = uiState.textError
                            if (error != null) {
                                Text(
                                    text = error.toReadableMessage(),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )
                } else {
                    Box(
                        modifier =
                        Modifier
                            .height(400.dp)
                            .fillMaxWidth()
                            .padding(Spacing.s)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        PostCardBody(
                            text = uiState.textValue.text,
                            autoLoadImages = uiState.autoLoadImages,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xxxl))
            }
        }

        if (uiState.loading) {
            ProgressHud()
        }

        if (openImagePicker) {
            galleryHelper.getImageFromGallery { bytes ->
                openImagePicker = false
                if (bytes.isNotEmpty()) {
                    model.reduce(CreateCommentMviModel.Intent.ImageSelected(bytes))
                }
            }
        }

        if (rawContent != null) {
            when (val content = rawContent) {
                is PostModel -> {
                    RawContentDialog(
                        title = content.title,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        url = content.url,
                        text = content.text,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        onDismiss = {
                            rawContent = null
                        },
                    )
                }

                is CommentModel -> {
                    RawContentDialog(
                        text = content.text,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        onDismiss = {
                            rawContent = null
                        },
                    )
                }
            }
        }

        if (selectLanguageDialogOpen) {
            SelectLanguageDialog(
                languages = uiState.availableLanguages,
                currentLanguageId = uiState.currentLanguageId,
                onSelect = { langId ->
                    model.reduce(CreateCommentMviModel.Intent.ChangeLanguage(langId))
                    selectLanguageDialogOpen = false
                },
                onDismiss = {
                    selectLanguageDialogOpen = false
                },
            )
        }
    }
}
