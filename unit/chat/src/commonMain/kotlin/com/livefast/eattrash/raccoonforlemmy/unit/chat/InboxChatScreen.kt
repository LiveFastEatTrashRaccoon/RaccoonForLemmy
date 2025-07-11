package com.livefast.eattrash.raccoonforlemmy.unit.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.TextFormattingBar
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.safeImePadding
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.unit.chat.components.MessageCard
import com.livefast.eattrash.raccoonforlemmy.unit.chat.components.MessageCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.unit.chat.di.InboxChatMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxChatScreen(otherUserId: Long, modifier: Modifier = Modifier) {
    val model: InboxChatMviModel = getViewModel<InboxChatViewModel>(InboxChatMviModelParams(otherUserId))
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val mainRouter = remember { getMainRouter() }
    val galleryHelper = remember { getGalleryHelper() }
    var openImagePicker by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(text = ""),
        )
    }
    val themeRepository = remember { getThemeRepository() }
    val contentFontFamily by themeRepository.contentFontFamily.collectAsState()
    val typography = contentFontFamily.toTypography()
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val lazyListState = rememberLazyListState()
    var itemIdToDelete by remember { mutableStateOf<Long?>(null) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    is InboxChatMviModel.Effect.AddImageToText -> {
                        textFieldValue =
                            textFieldValue.let {
                                it.copy(text = it.text + "\n![](${effect.url})")
                            }
                    }

                    InboxChatMviModel.Effect.ScrollToBottom -> {
                        runCatching {
                            lazyListState.scrollToItem(0)
                        }
                    }
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.navigationBarsPadding().safeImePadding(),
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val avatar = uiState.otherUserAvatar.orEmpty()
                        if (avatar.isNotEmpty()) {
                            CustomImage(
                                modifier =
                                Modifier
                                    .padding(Spacing.xxxs)
                                    .size(IconSize.s)
                                    .clip(RoundedCornerShape(IconSize.s / 2)),
                                url = avatar,
                                autoload = uiState.autoLoadImages,
                                quality = FilterQuality.Low,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = uiState.otherUserName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
                navigationIcon = {
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
                },
            )
        },
        bottomBar = {
            Column(
                modifier =
                Modifier
                    .navigationBarsPadding()
                    .safeImePadding()
                    .fillMaxWidth()
                    .padding(bottom = Spacing.s)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                OutlinedTextField(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .heightIn(
                            min = 80.dp,
                            max = 360.dp,
                        ),
                    colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    label = {
                        Text(
                            text =
                            buildString {
                                if (uiState.editedMessageId != null) {
                                    append(LocalStrings.current.inboxChatMessage)
                                    append(" (")
                                    append(LocalStrings.current.postActionEdit)
                                    append(")")
                                } else {
                                    append(LocalStrings.current.actionChat)
                                }
                            },
                            style = typography.bodyMedium,
                        )
                    },
                    textStyle = typography.bodyMedium,
                    value = textFieldValue,
                    keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        autoCorrectEnabled = true,
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    onValueChange = { value ->
                        textFieldValue = value
                    },
                )

                TextFormattingBar(
                    modifier =
                    Modifier.padding(
                        top = Spacing.xs,
                        start = Spacing.s,
                        end = Spacing.s,
                    ),
                    textFieldValue = textFieldValue,
                    onChangeTextFieldValue = {
                        textFieldValue = it
                    },
                    onSelectImage = {
                        openImagePicker = true
                    },
                    lastActionIcon = Icons.AutoMirrored.Default.Send,
                    lastActionDescription = LocalStrings.current.actionSend,
                    onLastAction = {
                        model.reduce(
                            InboxChatMviModel.Intent.SubmitNewMessage(
                                textFieldValue.text,
                            ),
                        )
                        textFieldValue = TextFieldValue(text = "")
                    },
                )
            }
        },
    ) { padding ->
        if (uiState.currentUserId != null) {
            Box(
                modifier =
                Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                    ).consumeWindowInsets(padding),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true,
                    state = lazyListState,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(Spacing.s))
                    }
                    if (uiState.messages.isEmpty() && uiState.initial) {
                        items(5) {
                            MessageCardPlaceholder()
                        }
                    }
                    items(
                        items = uiState.messages,
                        key = {
                            it.id.toString() + (it.updateDate ?: it.publishDate)
                        },
                    ) { message ->
                        val isMyMessage = message.creator?.id == uiState.currentUserId
                        val content = message.content.orEmpty()
                        val date = message.publishDate.orEmpty()
                        MessageCard(
                            isMyMessage = isMyMessage,
                            content = content,
                            date = date,
                            onOpenImage = { url ->
                                mainRouter.openImage(
                                    url = url,
                                    source = message.creator?.readableHandle.orEmpty(),
                                )
                            },
                            options =
                            buildList {
                                this +=
                                    Option(
                                        OptionId.SeeRaw,
                                        LocalStrings.current.postActionSeeRaw,
                                    )
                                if (isMyMessage) {
                                    this +=
                                        Option(
                                            OptionId.Edit,
                                            LocalStrings.current.postActionEdit,
                                        )
                                    this +=
                                        Option(
                                            OptionId.Delete,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                }
                            },
                            onSelectOption = { optionId ->
                                when (optionId) {
                                    OptionId.Edit -> {
                                        model.reduce(
                                            InboxChatMviModel.Intent.EditMessage(message.id),
                                        )
                                        message.content?.also {
                                            textFieldValue = TextFieldValue(text = it)
                                        }
                                    }

                                    OptionId.SeeRaw -> {
                                        rawContent = message
                                    }

                                    OptionId.Delete -> {
                                        itemIdToDelete = message.id
                                    }

                                    else -> Unit
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(Spacing.s))
                    }
                    item {
                        if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(InboxChatMviModel.Intent.LoadNextPage)
                        }
                        if (!uiState.initial && uiState.loading && !uiState.refreshing) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(25.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(Spacing.xxxl))
                    }
                }
            }
        }

        if (openImagePicker) {
            galleryHelper.getImageFromGallery { bytes ->
                openImagePicker = false
                if (bytes.isNotEmpty()) {
                    model.reduce(InboxChatMviModel.Intent.ImageSelected(bytes))
                }
            }
        }
    }

    if (rawContent != null) {
        when (val content = rawContent) {
            is PrivateMessageModel -> {
                RawContentDialog(
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    text = content.content,
                    onDismiss = {
                        rawContent = null
                    },
                )
            }
        }
    }

    itemIdToDelete?.also { itemId ->
        AlertDialog(
            onDismissRequest = {
                itemIdToDelete = null
            },
            dismissButton = {
                Button(
                    onClick = {
                        itemIdToDelete = null
                    },
                ) {
                    Text(text = LocalStrings.current.buttonCancel)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        model.reduce(InboxChatMviModel.Intent.DeleteMessage(itemId))
                        itemIdToDelete = null
                    },
                ) {
                    Text(text = LocalStrings.current.buttonConfirm)
                }
            },
            text = {
                Text(text = LocalStrings.current.messageAreYouSure)
            },
        )
    }
}
