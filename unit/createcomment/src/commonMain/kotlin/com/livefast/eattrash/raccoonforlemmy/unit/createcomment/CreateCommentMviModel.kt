package com.livefast.eattrash.raccoonforlemmy.unit.createcomment

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CreatePostSection
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel

@Stable
interface CreateCommentMviModel :
    MviModel<CreateCommentMviModel.Intent, CreateCommentMviModel.UiState, CreateCommentMviModel.Effect> {
    sealed interface Intent {
        data class ChangeSection(val value: CreatePostSection) : Intent

        data class ImageSelected(val value: ByteArray) : Intent {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as ImageSelected

                return value.contentEquals(other.value)
            }

            override fun hashCode(): Int = value.contentHashCode()
        }

        data class ChangeLanguage(val value: Long?) : Intent

        data class ChangeTextValue(val value: TextFieldValue) : Intent

        data object Send : Intent

        data object SaveDraft : Intent
    }

    data class UiState(
        val originalPost: PostModel? = null,
        val originalComment: CommentModel? = null,
        val editedComment: CommentModel? = null,
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val textValue: TextFieldValue = TextFieldValue(),
        val textError: ValidationError? = null,
        val loading: Boolean = false,
        val section: CreatePostSection = CreatePostSection.Edit,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val currentInstance: String = "",
        val currentUser: String = "",
        val currentLanguageId: Long? = null,
        val availableLanguages: List<LanguageModel> = emptyList(),
        val downVoteEnabled: Boolean = true,
    )

    sealed interface Effect {
        data class Success(val new: Boolean) : Effect

        data class Failure(val message: String?) : Effect

        data object DraftSaved : Effect
    }
}
