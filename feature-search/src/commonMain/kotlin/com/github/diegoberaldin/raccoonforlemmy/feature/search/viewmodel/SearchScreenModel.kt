package com.github.diegoberaldin.raccoonforlemmy.feature.search.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchScreenModel(
    private val mvi: DefaultMviModel<SearchScreenMviModel.Intent, SearchScreenMviModel.UiState, SearchScreenMviModel.Effect>,
    private val apiConfigRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
) : ScreenModel,
    MviModel<SearchScreenMviModel.Intent, SearchScreenMviModel.UiState, SearchScreenMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun onStarted() {
        mvi.onStarted()
        mvi.updateState {
            it.copy(
                instance = apiConfigRepository.getInstance(),
            )
        }
        mvi.scope.launch {
            identityRepository.authToken.map { !it.isNullOrEmpty() }.onEach { isLogged ->
                mvi.updateState {
                    it.copy(isLogged = isLogged)
                }
            }.launchIn(this)
        }

        if (mvi.uiState.value.communities.isEmpty()) {
            refresh()
        }
    }

    override fun reduce(intent: SearchScreenMviModel.Intent) {
        when (intent) {
            SearchScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            SearchScreenMviModel.Intent.Refresh -> refresh()
            SearchScreenMviModel.Intent.SearchFired -> refresh()
            is SearchScreenMviModel.Intent.SetSearch -> setSearch(intent.value)
            is SearchScreenMviModel.Intent.SetSubscribedOnly -> applySubscribedOnly(intent.value)
        }
    }

    private fun setSearch(value: String) {
        mvi.updateState { it.copy(searchText = value) }
    }

    private fun applySubscribedOnly(value: Boolean) {
        mvi.updateState { it.copy(subscribedOnly = value) }
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
            val searchText = mvi.uiState.value.searchText
            val auth = identityRepository.authToken.value
            val refreshing = currentState.refreshing
            val subscribedOnly = currentState.subscribedOnly
            if (subscribedOnly) {
                val items = communityRepository.getSubscribed(
                    auth = auth,
                ).filter {
                    it.name.contains(searchText)
                }
                currentPage++
                mvi.updateState {
                    val newItems = if (refreshing) {
                        items
                    } else {
                        it.communities + items
                    }
                    it.copy(
                        communities = newItems,
                        loading = false,
                        canFetchMore = false,
                        refreshing = false,
                    )
                }
            } else {
                val items = communityRepository.getAll(
                    query = searchText,
                    auth = auth,
                    page = currentPage,
                )
                currentPage++
                val canFetchMore = items.size >= PostsRepository.DEFAULT_PAGE_SIZE
                mvi.updateState {
                    val newItems = if (refreshing) {
                        items
                    } else {
                        it.communities + items
                    }
                    it.copy(
                        communities = newItems,
                        loading = false,
                        canFetchMore = canFetchMore,
                        refreshing = false,
                    )
                }
            }
        }
    }
}
