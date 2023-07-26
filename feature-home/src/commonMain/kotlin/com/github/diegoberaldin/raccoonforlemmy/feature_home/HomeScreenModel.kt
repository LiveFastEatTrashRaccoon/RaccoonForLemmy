package com.github.diegoberaldin.raccoonforlemmy.feature_home

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val mvi: DefaultMviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect>,
    private val postsRepository: PostsRepository,
    private val communityRepository: CommunityRepository,
    private val apiConfigRepository: ApiConfigurationRepository,
) : ScreenModel,
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun reduce(intent: HomeScreenMviModel.Intent) {
        when (intent) {
            HomeScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            HomeScreenMviModel.Intent.Refresh -> refresh()
            is HomeScreenMviModel.Intent.ChangeSort -> applySortType(intent.value)
            is HomeScreenMviModel.Intent.ChangeListing -> applyListingType(intent.value)
        }
    }

    override fun onStarted() {
        mvi.onStarted()
        mvi.updateState { it.copy(instance = apiConfigRepository.getInstance()) }
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
            val type = currentState.listingType
            val sort = currentState.sortType
            val refreshing = currentState.refreshing
            val postList = postsRepository.getPosts(
                page = currentPage,
                type = type,
                sort = sort,
            ).map {
                val community = it.community
                if (community?.id != null) {
                    val remoteCommunity = communityRepository.getCommunity(community.id)
                    it.copy(
                        community = it.community?.copy(
                            name = remoteCommunity?.name.orEmpty(),
                            icon = remoteCommunity?.icon,
                        )
                    )
                } else {
                    it
                }
            }
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
}