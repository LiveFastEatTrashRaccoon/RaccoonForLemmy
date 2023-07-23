package com.github.diegoberaldin.raccoonforlemmy.feature_home

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.PostsRepository
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val mvi: DefaultMviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect>,
    private val postsRepository: PostsRepository,
) : ScreenModel,
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        mvi.scope.launch {
            mvi.updateState { it.copy(loading = true) }
            val postList = postsRepository.getPosts()
            mvi.updateState {
                it.copy(
                    posts = postList,
                    loading = false
                )
            }
        }
    }
}