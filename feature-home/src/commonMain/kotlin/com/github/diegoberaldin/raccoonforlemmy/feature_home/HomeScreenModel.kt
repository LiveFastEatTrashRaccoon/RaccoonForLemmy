package com.github.diegoberaldin.raccoonforlemmy.feature_home

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val mvi: DefaultMviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect>,
    private val postsRepository: PostsRepository,
) : ScreenModel,
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun reduce(intent: HomeScreenMviModel.Intent) {
        when (intent) {

            HomeScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            HomeScreenMviModel.Intent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        currentPage = 1
        mvi.updateState { it.copy(canFetchMore = true) }
        loadNextPage()
    }

    private fun loadNextPage() {
        if (!mvi.uiState.value.canFetchMore || mvi.uiState.value.loading) {
            return
        }

        mvi.scope.launch(Dispatchers.IO) {
            mvi.updateState { it.copy(loading = true) }
            println("Fetching page: $currentPage")
            val postList = postsRepository.getPosts(
                page = currentPage,
            )
            currentPage++
            val canFetchMore = postList.size >= PostsRepository.DEFAULT_PAGE_SIZE
            println("Can fetch more: $canFetchMore")
            mvi.updateState {
                it.copy(
                    posts = it.posts + postList,
                    loading = false,
                    canFetchMore = canFetchMore,
                )
            }
        }
    }
}