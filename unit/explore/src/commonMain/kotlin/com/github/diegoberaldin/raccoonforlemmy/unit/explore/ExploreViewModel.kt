package com.github.diegoberaldin.raccoonforlemmy.unit.explore

import cafe.adriel.voyager.core.model.screenModelScope
import com.diegoberaldin.raccoonforlemmy.domain.lemmy.pagination.ExplorePaginationManager
import com.diegoberaldin.raccoonforlemmy.domain.lemmy.pagination.ExplorePaginationSpecification
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.utils.imagepreload.ImagePreloadManager
import com.github.diegoberaldin.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSearchResultType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.GetSortTypesUseCase
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ExploreViewModel(
    private val otherInstance: String,
    private val apiConfigRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val paginationManager: ExplorePaginationManager,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationCenter: NotificationCenter,
    private val hapticFeedback: HapticFeedback,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val imagePreloadManager: ImagePreloadManager,
    private val lemmyValueCache: LemmyValueCache,
) : DefaultMviModel<ExploreMviModel.Intent, ExploreMviModel.UiState, ExploreMviModel.Effect>(
        initialState = ExploreMviModel.UiState(),
    ),
    ExploreMviModel {
    private val isOnOtherInstance: Boolean get() = otherInstance.isNotEmpty()
    private val notificationEventKey: String
        get() =
            buildString {
                append("explore")
                if (isOnOtherInstance) {
                    append("-")
                    append(otherInstance)
                }
            }

    init {
        screenModelScope.launch {
            updateState {
                it.copy(
                    instance = apiConfigRepository.instance.value,
                )
            }
            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                    updateAvailableSortTypes()
                }.launchIn(this)
            combine(
                identityRepository.isLogged.map { it == true },
                settingsRepository.currentSettings.map { it.enableSwipeActions },
            ) { logged, swipeActionsEnabled ->
                logged && swipeActionsEnabled && otherInstance.isEmpty()
            }.onEach { value ->
                updateState { it.copy(swipeActionsEnabled = value) }
            }.launchIn(this)
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            blurNsfw = settings.blurNsfw,
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            doubleTapActionEnabled = settings.enableDoubleTapAction,
                            actionsOnSwipeToStartPosts = settings.actionsOnSwipeToStartPosts,
                            actionsOnSwipeToEndPosts = settings.actionsOnSwipeToEndPosts,
                            actionsOnSwipeToStartComments = settings.actionsOnSwipeToStartComments,
                            actionsOnSwipeToEndComments = settings.actionsOnSwipeToEndComments,
                            showScores = settings.showScores,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    handleLogout()
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.PostUpdated::class)
                .onEach { evt ->
                    handlePostUpdate(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.CommentUpdated::class)
                .onEach { evt ->
                    handleCommentUpdate(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeFeedType::class)
                .onEach { evt ->
                    if (evt.screenKey == notificationEventKey) {
                        changeListingType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    if (evt.screenKey == notificationEventKey) {
                        changeSortType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ResetExplore::class)
                .onEach {
                    onFirstLoad()
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSearchResultType::class)
                .onEach { evt ->
                    if (evt.screenKey == notificationEventKey) {
                        changeResultType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.CommunitySubscriptionChanged::class)
                .onEach { evt ->
                    handleCommunityUpdate(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.OpenSearchInExplore::class)
                .onEach {
                    delay(500)
                    emitEffect(ExploreMviModel.Effect.OpenSearch)
                }.launchIn(this)

            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(
                            downVoteEnabled = value,
                        )
                    }
                }.launchIn(this)

            uiState
                .map {
                    it.searchText
                }.distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    if (!uiState.value.initial) {
                        emitEffect(ExploreMviModel.Effect.BackToTop)
                        refresh()
                    }
                }.launchIn(this)
        }

        if (uiState.value.initial) {
            onFirstLoad()
        }
    }

    private fun onFirstLoad() {
        screenModelScope.launch {
            val settings = settingsRepository.currentSettings.value
            val listingType =
                if (isOnOtherInstance) ListingType.Local else settings.defaultExploreType.toListingType()
            val sortType = settings.defaultPostSortType.toSortType()
            updateState {
                it.copy(
                    listingType = listingType,
                    sortType = sortType,
                    resultType = settings.defaultExploreResultType.toSearchResultType(),
                )
            }
            emitEffect(ExploreMviModel.Effect.BackToTop)
            refresh(initial = true)
        }
    }

    private suspend fun updateAvailableSortTypes() {
        val sortTypes = getSortTypesUseCase.getTypesForPosts()
        updateState { it.copy(availableSortTypes = sortTypes) }
    }

    override fun reduce(intent: ExploreMviModel.Intent) {
        when (intent) {
            ExploreMviModel.Intent.LoadNextPage -> {
                screenModelScope.launch {
                    loadNextPage()
                }
            }

            ExploreMviModel.Intent.Refresh -> {
                screenModelScope.launch {
                    refresh()
                }
            }

            ExploreMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            is ExploreMviModel.Intent.SetSearch -> setSearch(intent.value)
            is ExploreMviModel.Intent.DownVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Post && it.model.id == intent.id
                    }?.also { result ->
                        toggleDownVote(
                            post = (result as SearchResult.Post).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.SavePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Post && it.model.id == intent.id
                    }?.also { result ->
                        toggleSave(
                            post = (result as SearchResult.Post).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.UpVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Post && it.model.id == intent.id
                    }?.also { result ->
                        toggleUpVote(
                            post = (result as SearchResult.Post).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.DownVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Comment && it.model.id == intent.id
                    }?.also { result ->
                        toggleDownVoteComment(
                            comment = (result as SearchResult.Comment).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.SaveComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Comment && it.model.id == intent.id
                    }?.also { result ->
                        toggleSaveComment(
                            comment = (result as SearchResult.Comment).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.UpVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.results
                    .firstOrNull {
                        it is SearchResult.Comment && it.model.id == intent.id
                    }?.also { result ->
                        toggleUpVoteComment(
                            comment = (result as SearchResult.Comment).model,
                        )
                    }
            }

            is ExploreMviModel.Intent.ToggleSubscription -> toggleSubscription(intent.communityId)
        }
    }

    private fun setSearch(value: String) {
        screenModelScope.launch {
            updateState {
                it.copy(
                    searchText = value,
                    initial = false,
                )
            }
        }
    }

    private fun changeListingType(value: ListingType) {
        screenModelScope.launch {
            updateState { it.copy(listingType = value) }
            emitEffect(ExploreMviModel.Effect.BackToTop)
            refresh()
        }
    }

    private fun changeSortType(value: SortType) {
        screenModelScope.launch {
            updateState { it.copy(sortType = value) }
            emitEffect(ExploreMviModel.Effect.BackToTop)
            refresh()
        }
    }

    private fun changeResultType(value: SearchResultType) {
        screenModelScope.launch {
            updateState { it.copy(resultType = value) }
            emitEffect(ExploreMviModel.Effect.BackToTop)
            refresh()
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        paginationManager.reset(
            ExplorePaginationSpecification(
                listingType = uiState.value.listingType,
                sortType = uiState.value.sortType,
                query = uiState.value.searchText,
                includeNsfw = settingsRepository.currentSettings.value.includeNsfw,
                searchPostTitleOnly = settingsRepository.currentSettings.value.searchPostTitleOnly,
                otherInstance = otherInstance,
                resultType = uiState.value.resultType,
            ),
        )
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                refreshing = !initial,
                loading = false,
            )
        }
        loadNextPage()
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        updateState { it.copy(loading = true) }

        val results = paginationManager.loadNextPage()
        if (uiState.value.autoLoadImages) {
            results.forEach { res ->
                (res as? SearchResult.Post)?.model?.imageUrl?.takeIf { it.isNotEmpty() }
                    ?.also { url ->
                        imagePreloadManager.preload(url)
                    }
            }
        }
        updateState {
            it.copy(
                results = results,
                loading = false,
                canFetchMore = paginationManager.canFetchMore,
                refreshing = false,
            )
        }
    }

    private fun handleLogout() {
        screenModelScope.launch {
            updateState {
                it.copy(
                    listingType = ListingType.Local,
                    results = emptyList(),
                )
            }
            onFirstLoad()
        }
    }

    private fun handlePostUpdate(post: PostModel) {
        screenModelScope.launch {
            updateState {
                it.copy(
                    results =
                        it.results.map { r ->
                            if (r is SearchResult.Post && r.model.id == post.id) {
                                r.copy(model = post)
                            } else {
                                r
                            }
                        },
                )
            }
        }
    }

    private fun handleCommentUpdate(comment: CommentModel) {
        screenModelScope.launch {
            updateState {
                it.copy(
                    results =
                        it.results.map { r ->
                            if (r is SearchResult.Comment && r.model.id == comment.id) {
                                r.copy(model = comment)
                            } else {
                                r
                            }
                        },
                )
            }
        }
    }

    private fun toggleUpVote(post: PostModel) {
        screenModelScope.launch {
            val newVote = post.myVote <= 0
            val newPost =
                postRepository.asUpVoted(
                    post = post,
                    voted = newVote,
                )
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Post) return@map res
                            if (res.model.id == post.id) {
                                res.copy(model = newPost)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.upVote(
                    post = post,
                    auth = auth,
                    voted = newVote,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Post) return@map res
                                if (res.model.id == post.id) {
                                    res.copy(model = post)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleDownVote(post: PostModel) {
        screenModelScope.launch {
            val newValue = post.myVote >= 0
            val newPost =
                postRepository.asDownVoted(
                    post = post,
                    downVoted = newValue,
                )
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Post) return@map res
                            if (res.model.id == post.id) {
                                res.copy(model = newPost)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.downVote(
                    post = post,
                    auth = auth,
                    downVoted = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Post) return@map res
                                if (res.model.id == post.id) {
                                    res.copy(model = post)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleSave(post: PostModel) {
        screenModelScope.launch {
            val newValue = !post.saved
            val newPost =
                postRepository.asSaved(
                    post = post,
                    saved = newValue,
                )
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Post) return@map res
                            if (res.model.id == post.id) {
                                res.copy(model = newPost)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.save(
                    post = post,
                    auth = auth,
                    saved = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Post) return@map res
                                if (res.model.id == post.id) {
                                    res.copy(model = post)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleUpVoteComment(comment: CommentModel) {
        screenModelScope.launch {
            val newValue = comment.myVote <= 0
            val newComment =
                commentRepository.asUpVoted(
                    comment = comment,
                    voted = newValue,
                )
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Comment) return@map res
                            if (res.model.id == comment.id) {
                                res.copy(model = newComment)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.upVote(
                    auth = auth,
                    comment = comment,
                    voted = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Comment) return@map res
                                if (res.model.id == comment.id) {
                                    res.copy(model = comment)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleDownVoteComment(comment: CommentModel) {
        screenModelScope.launch {
            val newValue = comment.myVote >= 0
            val newComment = commentRepository.asDownVoted(comment, newValue)
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Comment) return@map res
                            if (res.model.id == comment.id) {
                                res.copy(model = newComment)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.downVote(
                    auth = auth,
                    comment = comment,
                    downVoted = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Comment) return@map res
                                if (res.model.id == comment.id) {
                                    res.copy(model = comment)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleSaveComment(comment: CommentModel) {
        screenModelScope.launch {
            val newValue = !comment.saved
            val newComment =
                commentRepository.asSaved(
                    comment = comment,
                    saved = newValue,
                )
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Comment) return@map res
                            if (res.model.id == comment.id) {
                                res.copy(model = newComment)
                            } else {
                                res
                            }
                        },
                )
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.save(
                    auth = auth,
                    comment = comment,
                    saved = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState {
                    it.copy(
                        results =
                            it.results.map { res ->
                                if (res !is SearchResult.Comment) return@map res
                                if (res.model.id == comment.id) {
                                    res.copy(model = comment)
                                } else {
                                    res
                                }
                            },
                    )
                }
            }
        }
    }

    private fun toggleSubscription(communityId: Long) {
        val community =
            uiState.value.results
                .firstOrNull {
                    (it as? SearchResult.Community)?.model?.id == communityId
                }.let { (it as? SearchResult.Community)?.model } ?: return
        screenModelScope.launch {
            val newValue =
                when (community.subscribed) {
                    true -> {
                        hapticFeedback.vibrate()
                        communityRepository.unsubscribe(
                            auth = identityRepository.authToken.value,
                            id = communityId,
                        )
                    }

                    false -> {
                        hapticFeedback.vibrate()
                        communityRepository.subscribe(
                            auth = identityRepository.authToken.value,
                            id = communityId,
                        )
                    }

                    else -> community
                }
            if (newValue == null) {
                emitEffect(ExploreMviModel.Effect.OperationFailure)
            } else {
                handleCommunityUpdate(newValue)
            }
        }
    }

    private fun handleCommunityUpdate(community: CommunityModel) {
        screenModelScope.launch {
            updateState {
                it.copy(
                    results =
                        it.results.map { res ->
                            if (res !is SearchResult.Community) return@map res
                            if (res.model.id == community.id) {
                                res.copy(model = community)
                            } else {
                                res
                            }
                        },
                )
            }
        }
    }
}
