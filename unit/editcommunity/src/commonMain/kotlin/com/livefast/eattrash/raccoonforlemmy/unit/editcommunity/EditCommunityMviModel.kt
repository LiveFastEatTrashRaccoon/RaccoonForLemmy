package com.livefast.eattrash.raccoonforlemmy.unit.editcommunity

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityVisibilityType

interface EditCommunityMviModel :
    MviModel<EditCommunityMviModel.Intent, EditCommunityMviModel.UiState, EditCommunityMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data class ChangeName(val value: String) : Intent

        data class ChangeTitle(val value: String) : Intent

        data class ChangeDescription(val value: String) : Intent

        data class IconSelected(val value: ByteArray) : Intent {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as IconSelected

                return value.contentEquals(other.value)
            }

            override fun hashCode(): Int = value.contentHashCode()
        }

        data class BannerSelected(val value: ByteArray) : Intent {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as BannerSelected

                return value.contentEquals(other.value)
            }

            override fun hashCode(): Int = value.contentHashCode()
        }

        data class ChangeNsfw(val value: Boolean) : Intent

        data class ChangePostingRestrictedToMods(val value: Boolean) : Intent

        data object Submit : Intent
    }

    data class UiState(
        val loading: Boolean = false,
        val name: String = "",
        val title: String = "",
        val description: String = "",
        val icon: String = "",
        val banner: String = "",
        val hasUnsavedChanges: Boolean = false,
        val nsfw: Boolean = false,
        val postingRestrictedToMods: Boolean = false,
        val visibilityType: CommunityVisibilityType = CommunityVisibilityType.Public,
    )

    sealed interface Effect {
        data object Success : Effect

        data object Failure : Effect
    }
}
