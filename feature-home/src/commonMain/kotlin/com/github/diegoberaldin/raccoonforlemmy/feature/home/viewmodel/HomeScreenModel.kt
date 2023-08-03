package com.github.diegoberaldin.raccoonforlemmy.feature.home.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val mvi: DefaultMviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect>,
    private val postsRepository: PostsRepository,
    private val apiConfigRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val keyStore: TemporaryKeyStore,
    private val notificationCenter: NotificationCenter,
) : ScreenModel,
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun reduce(intent: HomeScreenMviModel.Intent) {
        when (intent) {
            HomeScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            HomeScreenMviModel.Intent.Refresh -> refresh()
            is HomeScreenMviModel.Intent.ChangeSort -> applySortType(intent.value)
            is HomeScreenMviModel.Intent.ChangeListing -> applyListingType(intent.value)
            is HomeScreenMviModel.Intent.DownVotePost -> downVote(intent.post, intent.value)
            is HomeScreenMviModel.Intent.SavePost -> save(intent.post, intent.value)
            is HomeScreenMviModel.Intent.UpVotePost -> upVote(intent.post, intent.value)
        }
    }

    override fun onStarted() {
        mvi.onStarted()
        val listingType = keyStore[KeyStoreKeys.DefaultListingType, 0].toListingType()
        val sortType = keyStore[KeyStoreKeys.DefaultPostSortType, 0].toSortType()
        mvi.updateState {
            it.copy(
                instance = apiConfigRepository.getInstance(),
                listingType = listingType,
                sortType = sortType,
            )
        }

        mvi.scope.launch {
            identityRepository.authToken.map { !it.isNullOrEmpty() }.onEach { isLogged ->
                mvi.updateState {
                    it.copy(isLogged = isLogged)
                }
            }.launchIn(this)
            notificationCenter.events.filterIsInstance<NotificationCenter.Event.PostUpdate>()
                .onEach { evt ->
                    val newPost = evt.post
                    mvi.updateState {
                        it.copy(
                            posts = it.posts.map { p ->
                                if (p.id == newPost.id) {
                                    newPost
                                } else {
                                    p
                                }
                            },
                        )
                    }
                }.launchIn(this)
        }

        refresh()
    }

    private fun refresh() {
        currentPage = 1
        mvi.updateState { it.copy(canFetchMore = true, refreshing = true) }
        loadNextPage()
    }

    private fun loadNextPage() {
        val currentState = mvi.uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            return
        }

        mvi.scope.launch(Dispatchers.IO) {
            mvi.updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value
            val type = currentState.listingType
            val sort = currentState.sortType
            val refreshing = currentState.refreshing
            val postList = postsRepository.getPosts(
                auth = auth,
                page = currentPage,
                type = type,
                sort = sort,
            )
            currentPage++
            val canFetchMore = postList.size >= PostsRepository.DEFAULT_PAGE_SIZE
            mvi.updateState {
                val newPosts = if (refreshing) {
                    postList
                } else {
                    it.posts + postList
                }
                it.copy(
                    posts = newPosts,
                    loading = false,
                    canFetchMore = canFetchMore,
                    refreshing = false,
                )
            }
        }
    }

    private fun applySortType(value: SortType) {
        mvi.updateState { it.copy(sortType = value) }
        refresh()
    }

    private fun applyListingType(value: ListingType) {
        mvi.updateState { it.copy(listingType = value) }
        refresh()
    }

    private fun upVote(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.upVote(
                post = post,
                auth = auth,
                voted = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newPost
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }

    private fun downVote(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.downVote(
                post = post,
                auth = auth,
                downVoted = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newPost
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }

    private fun save(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.save(
                post = post,
                auth = auth,
                saved = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newPost
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }
}
