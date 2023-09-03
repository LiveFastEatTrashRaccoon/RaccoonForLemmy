package com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface PostListMviModel :
    MviModel<PostListMviModel.Intent, PostListMviModel.UiState, PostListMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class ChangeSort(val value: SortType) : Intent
        data class ChangeListing(val value: ListingType) : Intent
        data class UpVotePost(val post: PostModel, val feedback: Boolean = false) : Intent
        data class DownVotePost(val post: PostModel, val feedback: Boolean = false) : Intent
        data class SavePost(val post: PostModel, val feedback: Boolean = false) : Intent
        object HapticIndication : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val instance: String = "",
        val isLogged: Boolean = false,
        val listingType: ListingType = ListingType.Local,
        val sortType: SortType = SortType.Active,
        val posts: List<PostModel> = emptyList(),
        val blurNsfw: Boolean = true,
    )

    sealed interface Effect
}
