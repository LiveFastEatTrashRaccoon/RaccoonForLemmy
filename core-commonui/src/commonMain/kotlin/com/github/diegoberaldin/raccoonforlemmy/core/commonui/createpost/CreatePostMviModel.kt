package com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface CreatePostMviModel :
    MviModel<CreatePostMviModel.Intent, CreatePostMviModel.UiState, CreatePostMviModel.Effect> {

    sealed interface Intent {
        data class SetTitle(val value: String) : Intent
        data class SetText(val value: String) : Intent
        data class SetUrl(val value: String) : Intent
        data class ChangeNsfw(val value: Boolean) : Intent

        object Send : Intent
    }

    data class UiState(
        val title: String = "",
        val body: String = "",
        val url: String = "",
        val nsfw: Boolean = false,
        val loading: Boolean = false,
    )

    sealed interface Effect {
        object Success : Effect

        data class Failure(val message: String?) : Effect
    }
}
