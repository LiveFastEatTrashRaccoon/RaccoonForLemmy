package com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class CommunityDetailScreenViewModel(
    private val mvi: DefaultMviModel<CommunityDetailScreenMviModel.Intent, CommunityDetailScreenMviModel.UiState, CommunityDetailScreenMviModel.Effect>,
    private val community: CommunityModel,
    private val identityRepository: IdentityRepository,
    private val postsRepository: PostsRepository,
    private val keyStore: TemporaryKeyStore,
) : MviModel<CommunityDetailScreenMviModel.Intent, CommunityDetailScreenMviModel.UiState, CommunityDetailScreenMviModel.Effect> by mvi,
    ScreenModel {
    private var currentPage: Int = 1
    override fun onStarted() {
        mvi.onStarted()
        mvi.updateState { it.copy(community = community) }
        refresh()
    }

    override fun reduce(intent: CommunityDetailScreenMviModel.Intent) {
        when (intent) {
            CommunityDetailScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            CommunityDetailScreenMviModel.Intent.Refresh -> refresh()

            is CommunityDetailScreenMviModel.Intent.DownVotePost -> downVotePost(
                intent.post,
                intent.value,
            )

            is CommunityDetailScreenMviModel.Intent.SavePost -> savePost(
                intent.post,
                intent.value,
            )

            is CommunityDetailScreenMviModel.Intent.UpVotePost -> upVotePost(
                intent.post,
                intent.value,
            )
        }
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
            val refreshing = currentState.refreshing
            val sort = keyStore[KeyStoreKeys.DefaultCommentSortType, 3].toSortType()
            val commentList = postsRepository.getAll(
                auth = auth,
                communityId = community.id,
                page = currentPage,
                sort = sort,
            )
            currentPage++
            val canFetchMore = commentList.size >= CommentRepository.DEFAULT_PAGE_SIZE
            mvi.updateState {
                val newItems = if (refreshing) {
                    commentList
                } else {
                    it.posts + commentList
                }
                it.copy(
                    posts = newItems,
                    loading = false,
                    canFetchMore = canFetchMore,
                    refreshing = false,
                )
            }
        }
    }

    private fun upVotePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newItem = postsRepository.upVote(
                auth = auth,
                post = post,
                voted = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newItem
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }

    private fun downVotePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newItem = postsRepository.downVote(
                auth = auth,
                post = post,
                downVoted = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newItem
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }

    private fun savePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newItem = postsRepository.save(
                auth = auth,
                post = post,
                saved = value,
            )
            mvi.updateState {
                it.copy(
                    posts = it.posts.map { p ->
                        if (p.id == post.id) {
                            newItem
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }
}
