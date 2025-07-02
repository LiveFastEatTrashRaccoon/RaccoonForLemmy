package com.livefast.eattrash.raccoonforlemmy.unit.medialist

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel

interface MediaListMviModel : MviModel<MediaListMviModel.Intent, MediaListMviModel.State, MediaListMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class Delete(val media: MediaModel) : Intent
    }

    data class State(
        val initial: Boolean = true,
        val canFetchMore: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val media: List<MediaModel> = emptyList(),
        val currentInstance: String = "",
        val postLayout: PostLayout = PostLayout.Card,
        val autoloadImages: Boolean = true,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
    )

    sealed interface Effect {
        data object Success : Effect

        data class Failure(val message: String?) : Effect
    }
}
