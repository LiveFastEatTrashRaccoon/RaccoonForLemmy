package com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

interface CommunityDetailScreenMviModel :
    MviModel<CommunityDetailScreenMviModel.Intent, CommunityDetailScreenMviModel.UiState, CommunityDetailScreenMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class UpVotePost(val value: Boolean, val post: PostModel) : Intent
        data class DownVotePost(val value: Boolean, val post: PostModel) : Intent
        data class SavePost(val value: Boolean, val post: PostModel) : Intent
    }

    data class UiState(
        val community: CommunityModel = CommunityModel(),
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val posts: List<PostModel> = emptyList(),
    )

    sealed interface Effect
}
