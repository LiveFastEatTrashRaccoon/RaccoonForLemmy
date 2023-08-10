package com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.racconforlemmy.core.utils.HapticFeedback
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

class CommunityDetailViewModel(
    private val mvi: DefaultMviModel<CommunityDetailMviModel.Intent, CommunityDetailMviModel.UiState, CommunityDetailMviModel.Effect>,
    private val community: CommunityModel,
    private val identityRepository: IdentityRepository,
    private val postsRepository: PostsRepository,
    private val keyStore: TemporaryKeyStore,
    private val hapticFeedback: HapticFeedback,
) : MviModel<CommunityDetailMviModel.Intent, CommunityDetailMviModel.UiState, CommunityDetailMviModel.Effect> by mvi,
    ScreenModel {
    private var currentPage: Int = 1
    override fun onStarted() {
        mvi.onStarted()
        mvi.updateState { it.copy(community = community) }

        if (mvi.uiState.value.posts.isEmpty()) {
            refresh()
        }
    }

    override fun reduce(intent: CommunityDetailMviModel.Intent) {
        when (intent) {
            CommunityDetailMviModel.Intent.LoadNextPage -> loadNextPage()
            CommunityDetailMviModel.Intent.Refresh -> refresh()

            is CommunityDetailMviModel.Intent.DownVotePost -> downVotePost(
                intent.post,
                intent.value,
            )

            is CommunityDetailMviModel.Intent.SavePost -> savePost(
                intent.post,
                intent.value,
            )

            is CommunityDetailMviModel.Intent.UpVotePost -> upVotePost(
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
        hapticFeedback.vibrate()
        val newPost = postsRepository.asUpVoted(post, value)
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
        mvi.scope.launch(Dispatchers.IO) {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postsRepository.upVote(
                    auth = auth,
                    post = post,
                    voted = value,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
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
        }
    }

    private fun downVotePost(post: PostModel, value: Boolean) {
        hapticFeedback.vibrate()
        val newPost = postsRepository.asDownVoted(post, value)
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
        mvi.scope.launch(Dispatchers.IO) {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postsRepository.downVote(
                    auth = auth,
                    post = post,
                    downVoted = value,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
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
        }
    }

    private fun savePost(post: PostModel, value: Boolean) {
        hapticFeedback.vibrate()
        val newPost = postsRepository.asSaved(post, value)
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
        mvi.scope.launch(Dispatchers.IO) {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postsRepository.save(
                    auth = auth,
                    post = post,
                    saved = value,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
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
        }
    }
}
