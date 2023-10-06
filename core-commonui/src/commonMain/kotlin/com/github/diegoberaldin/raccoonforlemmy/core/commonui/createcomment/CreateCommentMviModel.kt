package com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost.CreatePostSection
import dev.icerock.moko.resources.desc.StringDesc

interface CreateCommentMviModel :
    MviModel<CreateCommentMviModel.Intent, CreateCommentMviModel.UiState, CreateCommentMviModel.Effect> {

    sealed interface Intent {
        data class SetText(val value: String) : Intent
        data class ChangeSection(val value: CreatePostSection) : Intent
        data object Send : Intent
    }

    data class UiState(
        val postLayout: PostLayout = PostLayout.Card,
        val separateUpAndDownVotes: Boolean = false,
        val text: String = "",
        val textError: StringDesc? = null,
        val loading: Boolean = false,
        val section: CreatePostSection = CreatePostSection.Edit,
    )

    sealed interface Effect {
        data object Success : Effect

        data class Failure(val message: String?) : Effect
    }
}
