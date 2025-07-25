package com.livefast.eattrash.raccoonforlemmy.unit.mentions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InboxMentionsViewModel(
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val hapticFeedback: HapticFeedback,
    private val coordinator: InboxCoordinator,
    private val notificationCenter: NotificationCenter,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<InboxMentionsMviModel.Intent, InboxMentionsMviModel.UiState, InboxMentionsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = InboxMentionsMviModel.UiState()),
    InboxMentionsMviModel {
    private var currentPage: Int = 1

    init {
        viewModelScope.launch {
            coordinator.events
                .onEach {
                    when (it) {
                        InboxCoordinator.Event.Refresh -> {
                            refresh()
                            emitEffect(InboxMentionsMviModel.Effect.BackToTop)
                        }
                    }
                }.launchIn(this)
            coordinator.unreadOnly
                .onEach {
                    if (it != uiState.value.unreadOnly) {
                        changeUnreadOnly(it)
                    }
                }.launchIn(this)
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            swipeActionsEnabled = settings.enableSwipeActions,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            voteFormat = settings.voteFormat,
                            actionsOnSwipeToStartInbox = settings.actionsOnSwipeToStartInbox,
                            actionsOnSwipeToEndInbox = settings.actionsOnSwipeToEndInbox,
                            showScores = settings.showScores,
                            previewMaxLines = settings.inboxPreviewMaxLines,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    handleLogout()
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(downVoteEnabled = value)
                    }
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: InboxMentionsMviModel.Intent) {
        when (intent) {
            InboxMentionsMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            InboxMentionsMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                    emitEffect(InboxMentionsMviModel.Effect.BackToTop)
                }

            is InboxMentionsMviModel.Intent.MarkAsRead ->
                viewModelScope.launch {
                    val mention = uiState.value.mentions.first { it.id == intent.id }
                    markAsRead(
                        read = intent.read,
                        mention = mention,
                    )
                }

            InboxMentionsMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            is InboxMentionsMviModel.Intent.DownVoteComment -> {
                val mention = uiState.value.mentions.first { it.id == intent.id }
                toggleDownVoteComment(mention)
            }

            is InboxMentionsMviModel.Intent.UpVoteComment -> {
                val mention = uiState.value.mentions.first { it.id == intent.id }
                toggleUpVoteComment(mention)
            }

            is InboxMentionsMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    uiState.value.mentions.firstOrNull { it.id == intent.id }?.also { mention ->
                        if (!mention.read) {
                            markAsRead(
                                mention = mention,
                                read = true,
                            )
                        }
                        emitEffect(
                            InboxMentionsMviModel.Effect.OpenDetail(
                                post = intent.post,
                                commentId = intent.commentId,
                            ),
                        )
                    }
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        currentPage = 1
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                refreshing = !initial,
                loading = false,
            )
        }
        loadNextPage()
        updateUnreadItems()
    }

    private fun changeUnreadOnly(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(unreadOnly = value) }
            refresh(initial = true)
            emitEffect(InboxMentionsMviModel.Effect.BackToTop)
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }

        updateState { it.copy(loading = true) }
        val auth = identityRepository.authToken.value
        val refreshing = currentState.refreshing
        val unreadOnly = currentState.unreadOnly
        val itemList =
            userRepository
                .getMentions(
                    auth = auth,
                    page = currentPage,
                    unreadOnly = unreadOnly,
                    sort = SortType.New,
                )?.map {
                    it.copy(isCommentReply = it.comment.depth > 0)
                }
        if (!itemList.isNullOrEmpty()) {
            currentPage++
        }
        updateState {
            val newItems =
                if (refreshing) {
                    itemList.orEmpty()
                } else {
                    it.mentions + itemList.orEmpty()
                }.distinctBy { mention -> mention.id }
            it.copy(
                mentions = newItems,
                loading = false,
                canFetchMore = itemList?.isEmpty() != true,
                refreshing = false,
                initial = false,
            )
        }
    }

    private fun handleItemUpdate(item: PersonMentionModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    mentions =
                    it.mentions.map { i ->
                        if (i.id == item.id) {
                            item
                        } else {
                            i
                        }
                    },
                )
            }
        }
    }

    private suspend fun markAsRead(read: Boolean, mention: PersonMentionModel) {
        val auth = identityRepository.authToken.value
        userRepository.setMentionRead(
            read = read,
            mentionId = mention.id,
            auth = auth,
        )
        val currentState = uiState.value
        if (read && currentState.unreadOnly) {
            updateState {
                it.copy(
                    mentions =
                    currentState.mentions.filter { m ->
                        m.id != mention.id
                    },
                )
            }
        } else {
            val newMention = mention.copy(read = read)
            handleItemUpdate(newMention)
        }
        updateUnreadItems()
    }

    private fun toggleUpVoteComment(mention: PersonMentionModel) {
        val newValue = mention.myVote <= 0
        val newMention =
            commentRepository.asUpVoted(
                mention = mention,
                voted = newValue,
            )
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handleItemUpdate(newMention)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.upVote(
                    auth = auth,
                    comment = mention.comment,
                    voted = newValue,
                )
                if (!mention.read && shouldBeMarkedAsRead) {
                    userRepository.setMentionRead(
                        read = true,
                        mentionId = mention.id,
                        auth = auth,
                    )
                    handleItemUpdate(newMention.copy(read = true))
                    updateUnreadItems()
                }
            } catch (e: Throwable) {
                handleItemUpdate(mention)
            }
        }
    }

    private fun toggleDownVoteComment(mention: PersonMentionModel) {
        val newValue = mention.myVote >= 0
        val newMention =
            commentRepository.asDownVoted(
                mention = mention,
                downVoted = newValue,
            )
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handleItemUpdate(newMention)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.downVote(
                    auth = auth,
                    comment = mention.comment,
                    downVoted = newValue,
                )
                if (!mention.read && shouldBeMarkedAsRead) {
                    userRepository.setMentionRead(
                        read = true,
                        mentionId = mention.id,
                        auth = auth,
                    )
                    handleItemUpdate(newMention.copy(read = true))
                    updateUnreadItems()
                }
            } catch (e: Throwable) {
                handleItemUpdate(mention)
            }
        }
    }

    private fun updateUnreadItems() {
        viewModelScope.launch {
            val unreadCount = coordinator.updateUnreadCount()
            emitEffect(InboxMentionsMviModel.Effect.UpdateUnreadItems(unreadCount))
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            updateState { it.copy(mentions = emptyList()) }
            refresh(initial = true)
        }
    }
}
