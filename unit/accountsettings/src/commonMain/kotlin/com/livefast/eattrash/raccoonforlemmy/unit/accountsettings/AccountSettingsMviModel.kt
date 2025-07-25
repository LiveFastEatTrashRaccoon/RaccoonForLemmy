package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

@Stable
interface AccountSettingsMviModel :
    MviModel<AccountSettingsMviModel.Intent, AccountSettingsMviModel.UiState, AccountSettingsMviModel.Effect> {
    sealed interface Intent {
        data class ChangeDisplayName(val value: String) : Intent

        data class ChangeEmail(val value: String) : Intent

        data class ChangeMatrixUserId(val value: String) : Intent

        data class ChangeBio(val value: String) : Intent

        data class ChangeBot(val value: Boolean) : Intent

        data class ChangeSendNotificationsToEmail(val value: Boolean) : Intent

        data class ChangeShowBotAccounts(val value: Boolean) : Intent

        data class ChangeShowReadPosts(val value: Boolean) : Intent

        data class ChangeShowNsfw(val value: Boolean) : Intent

        data class ChangeShowScores(val value: Boolean) : Intent

        data class ChangeShowUpVotes(val value: Boolean) : Intent

        data class ChangeShowDownVotes(val value: Boolean) : Intent

        data class ChangeShowUpVotePercentage(val value: Boolean) : Intent

        data class AvatarSelected(val value: ByteArray) : Intent {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as AvatarSelected

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

        data class DeleteAccount(val deleteContent: Boolean, val password: String) : Intent

        data object Submit : Intent
    }

    data class UiState(
        val loading: Boolean = false,
        val operationInProgress: Boolean = false,
        val hasUnsavedChanges: Boolean = false,
        val avatar: String = "",
        val banner: String = "",
        val bio: String = "",
        val bot: Boolean = false,
        val sendNotificationsToEmail: Boolean = false,
        val displayName: String = "",
        val matrixUserId: String = "",
        val email: String = "",
        val showBotAccounts: Boolean = false,
        val showReadPosts: Boolean = false,
        val showNsfw: Boolean = false,
        val defaultListingType: ListingType = ListingType.All,
        val availableSortTypes: List<SortType> = emptyList(),
        val defaultSortType: SortType = SortType.Active,
        val showScores: Boolean = true,
        val showUpVotes: Boolean = false,
        val showDownVotes: Boolean = false,
        val showUpVotePercentage: Boolean = false,
    )

    sealed interface Effect {
        data object Success : Effect

        data object Failure : Effect

        data class SetDeleteAccountValidationError(val error: ValidationError?) : Effect

        data object CloseDeleteAccountDialog : Effect

        data object Close : Effect
    }
}
