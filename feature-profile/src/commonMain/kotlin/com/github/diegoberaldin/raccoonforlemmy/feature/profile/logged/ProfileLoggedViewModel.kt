package com.github.diegoberaldin.raccoonforlemmy.feature.profile.logged

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterContractKeys
import com.github.diegoberaldin.raccoonforlemmy.core.utils.ShareHelper
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.shareUrl
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileLoggedViewModel(
    private val mvi: DefaultMviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val siteRepository: SiteRepository,
    private val postsRepository: PostsRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val themeRepository: ThemeRepository,
    private val shareHelper: ShareHelper,
    private val notificationCenter: NotificationCenter,
) : ScreenModel,
    MviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect> by mvi {

    private var currentPage = 1

    init {
        notificationCenter.addObserver({
            (it as? PostModel)?.also { post ->
                handlePostUpdate(post)
            }
        }, this::class.simpleName.orEmpty(), NotificationCenterContractKeys.PostUpdated)
        notificationCenter.addObserver({
            (it as? PostModel)?.also { post ->
                handlePostDelete(post.id)
            }
        }, this::class.simpleName.orEmpty(), NotificationCenterContractKeys.PostDeleted)
    }

    fun finalize() {
        notificationCenter.removeObserver(this::class.simpleName.orEmpty())
    }

    override fun onStarted() {
        mvi.onStarted()
        mvi.scope?.launch(Dispatchers.IO) {
            themeRepository.postLayout.onEach { layout ->
                mvi.updateState { it.copy(postLayout = layout) }
            }.launchIn(this)

            identityRepository.authToken.drop(1).onEach {
                mvi.updateState {
                    it.copy(
                        posts = emptyList(),
                        comments = emptyList(),
                    )
                }
                refresh()
            }.launchIn(this)

            if (uiState.value.posts.isEmpty()) {
                refresh()
            }
        }
    }

    override fun reduce(intent: ProfileLoggedMviModel.Intent) {
        when (intent) {
            is ProfileLoggedMviModel.Intent.ChangeSection -> changeSection(intent.section)
            is ProfileLoggedMviModel.Intent.DeleteComment -> deleteComment(intent.id)
            is ProfileLoggedMviModel.Intent.DeletePost -> deletePost(intent.id)
            ProfileLoggedMviModel.Intent.LoadNextPage -> loadNextPage()
            ProfileLoggedMviModel.Intent.Refresh -> refresh()
            is ProfileLoggedMviModel.Intent.SharePost -> share(
                post = uiState.value.posts[intent.index]
            )
        }
    }

    private fun refresh() {
        currentPage = 1
        mvi.scope?.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val user = siteRepository.getCurrentUser(auth)
            mvi.updateState {
                it.copy(
                    user = user,
                    canFetchMore = true,
                    refreshing = true,
                )
            }
            loadNextPage()
        }
    }

    private fun changeSection(section: ProfileLoggedSection) {
        currentPage = 1
        mvi.updateState {
            it.copy(
                section = section,
                canFetchMore = true,
                refreshing = true,
            )
        }
        loadNextPage()
    }

    private fun loadNextPage() {
        val currentState = mvi.uiState.value
        if (!currentState.canFetchMore || currentState.loading || currentState.user == null) {
            mvi.updateState { it.copy(refreshing = false) }
            return
        }

        mvi.scope?.launch(Dispatchers.IO) {
            mvi.updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value
            val refreshing = currentState.refreshing
            val userId = currentState.user.id
            val section = currentState.section
            if (section == ProfileLoggedSection.Posts) {
                val postList = userRepository.getPosts(
                    auth = auth,
                    id = userId,
                    page = currentPage,
                    sort = SortType.New,
                )
                val comments = if (currentPage == 1 && currentState.comments.isEmpty()) {
                    // this is needed because otherwise on first selector change
                    // the lazy column scrolls back to top (it must have an empty data set)
                    userRepository.getComments(
                        auth = auth,
                        id = userId,
                        page = currentPage,
                        sort = SortType.New,
                    )
                } else {
                    currentState.comments
                }
                val canFetchMore = postList.size >= PostsRepository.DEFAULT_PAGE_SIZE
                mvi.updateState {
                    val newPosts = if (refreshing) {
                        postList
                    } else {
                        it.posts + postList
                    }
                    it.copy(
                        posts = newPosts,
                        comments = comments,
                        loading = false,
                        canFetchMore = canFetchMore,
                        refreshing = false,
                    )
                }
            } else {
                val commentList = userRepository.getComments(
                    auth = auth,
                    id = userId,
                    page = currentPage,
                    sort = SortType.New,
                )
                val canFetchMore = commentList.size >= PostsRepository.DEFAULT_PAGE_SIZE
                mvi.updateState {
                    val newcomments = if (refreshing) {
                        commentList
                    } else {
                        it.comments + commentList
                    }
                    it.copy(
                        comments = newcomments,
                        loading = false,
                        canFetchMore = canFetchMore,
                        refreshing = false,
                    )
                }
            }
            currentPage++
        }
    }

    private fun handlePostUpdate(post: PostModel) {
        mvi.updateState {
            it.copy(
                posts = it.posts.map { p ->
                    if (p.id == post.id) {
                        post
                    } else {
                        p
                    }
                },
            )
        }
    }

    private fun handlePostDelete(id: Int) {
        mvi.updateState { it.copy(posts = it.posts.filter { post -> post.id != id }) }
    }

    private fun deletePost(id: Int) {
        mvi.scope?.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            postsRepository.delete(id = id, auth = auth)
            handlePostDelete(id)
        }
    }

    private fun deleteComment(id: Int) {
        mvi.scope?.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            commentRepository.delete(id, auth)
            refresh()
        }
    }

    private fun share(post: PostModel) {
        val url = post.shareUrl
        if (url.isNotEmpty()) {
            shareHelper.share(url, "text/plain")
        }
    }
}
