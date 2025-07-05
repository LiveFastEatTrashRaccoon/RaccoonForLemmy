package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountSettingsModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
    private val siteRepository: SiteRepository,
    private val identityRepository: IdentityRepository,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<AccountSettingsMviModel.Intent, AccountSettingsMviModel.UiState, AccountSettingsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = AccountSettingsMviModel.UiState()),
    AccountSettingsMviModel {
    private var accountSettings: AccountSettingsModel? = null

    init {
        viewModelScope.launch {
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    if (evt.screenKey == "accountSettings") {
                        updateState { it.copy(defaultSortType = evt.value) }
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeFeedType::class)
                .onEach { evt ->
                    if (evt.screenKey == "accountSettings") {
                        updateState { it.copy(defaultListingType = evt.value) }
                    }
                }.launchIn(this)

            if (accountSettings == null) {
                refreshSettings()
                val availableSortTypes = getSortTypesUseCase.getTypesForPosts()
                updateState { it.copy(availableSortTypes = availableSortTypes) }
            }
        }
    }

    override fun reduce(intent: AccountSettingsMviModel.Intent) {
        when (intent) {
            is AccountSettingsMviModel.Intent.ChangeDisplayName ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            displayName = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeEmail ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            email = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeMatrixUserId ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            matrixUserId = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeBio ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            bio = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeBot ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            bot = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeSendNotificationsToEmail ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            sendNotificationsToEmail = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowBotAccounts ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showBotAccounts = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowNsfw ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showNsfw = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowScores ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showScores = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowDownVotes ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showDownVotes = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowUpVotePercentage ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showUpVotePercentage = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowUpVotes ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showUpVotes = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.ChangeShowReadPosts ->
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showReadPosts = intent.value,
                            hasUnsavedChanges = true,
                        )
                    }
                }

            is AccountSettingsMviModel.Intent.AvatarSelected -> loadImageAvatar(intent.value)

            is AccountSettingsMviModel.Intent.BannerSelected -> loadImageBanner(intent.value)

            is AccountSettingsMviModel.Intent.DeleteAccount ->
                deleteAccount(
                    deleteContent = intent.deleteContent,
                    password = intent.password,
                )

            AccountSettingsMviModel.Intent.Submit -> submit()
        }
    }

    private suspend fun refreshSettings() {
        updateState { it.copy(loading = true) }
        val auth = identityRepository.authToken.value.orEmpty()
        accountSettings = siteRepository.getAccountSettings(auth)
        updateState {
            it.copy(
                loading = false,
                avatar = accountSettings?.avatar.orEmpty(),
                banner = accountSettings?.banner.orEmpty(),
                bio = accountSettings?.bio.orEmpty(),
                bot = accountSettings?.bot ?: false,
                sendNotificationsToEmail = accountSettings?.sendNotificationsToEmail ?: false,
                displayName = accountSettings?.displayName.orEmpty(),
                matrixUserId = accountSettings?.matrixUserId.orEmpty(),
                email = accountSettings?.email.orEmpty(),
                showBotAccounts = accountSettings?.showBotAccounts ?: false,
                showReadPosts = accountSettings?.showReadPosts ?: false,
                showNsfw = accountSettings?.showNsfw ?: false,
                defaultListingType = accountSettings?.defaultListingType ?: ListingType.All,
                defaultSortType = accountSettings?.defaultSortType ?: SortType.Active,
                showScores = accountSettings?.showScores ?: true,
                showUpVotes = accountSettings?.showUpVotes ?: false,
                showDownVotes = accountSettings?.showDownVotes ?: false,
                showUpVotePercentage = accountSettings?.showUpVotePercentage ?: false,
            )
        }
    }

    private fun loadImageAvatar(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            if (url != null) {
                updateState {
                    it.copy(
                        avatar = url,
                        hasUnsavedChanges = true,
                        loading = false,
                    )
                }
            }
        }
    }

    private fun loadImageBanner(bytes: ByteArray) {
        if (bytes.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val url = mediaRepository.uploadImage(auth, bytes)
            if (url != null) {
                updateState {
                    it.copy(
                        banner = url,
                        hasUnsavedChanges = true,
                        loading = false,
                    )
                }
            }
        }
    }

    private fun deleteAccount(deleteContent: Boolean, password: String) {
        viewModelScope.launch {
            if (password.isEmpty()) {
                emitEffect(
                    AccountSettingsMviModel.Effect.SetDeleteAccountValidationError(
                        ValidationError.MissingField,
                    ),
                )
                return@launch
            }

            if (uiState.value.operationInProgress) {
                return@launch
            }

            emitEffect(AccountSettingsMviModel.Effect.SetDeleteAccountValidationError(null))
            updateState { it.copy(operationInProgress = true) }

            val auth = identityRepository.authToken.value
            val success =
                userRepository.deleteAccount(
                    auth = auth,
                    deleteContent = deleteContent,
                    password = password,
                )

            if (success) {
                emitEffect(AccountSettingsMviModel.Effect.CloseDeleteAccountDialog)
                logoutUseCase()
                updateState { it.copy(operationInProgress = false) }
                emitEffect(AccountSettingsMviModel.Effect.Close)
            } else {
                updateState { it.copy(operationInProgress = false) }
                emitEffect(
                    AccountSettingsMviModel.Effect.SetDeleteAccountValidationError(
                        ValidationError.InvalidField,
                    ),
                )
            }
        }
    }

    private fun submit() {
        val currentState = uiState.value
        val settingsToSave =
            accountSettings?.copy(
                avatar = currentState.avatar,
                banner = currentState.banner,
                bio = currentState.bio,
                bot = currentState.bot,
                defaultListingType = currentState.defaultListingType,
                defaultSortType = currentState.defaultSortType,
                displayName = currentState.displayName,
                email = currentState.email,
                matrixUserId = currentState.matrixUserId,
                sendNotificationsToEmail = currentState.sendNotificationsToEmail,
                showBotAccounts = currentState.showBotAccounts,
                showNsfw = currentState.showNsfw,
                showReadPosts = currentState.showReadPosts,
                showScores = currentState.showScores,
                showUpVotes = currentState.showUpVotes,
                showDownVotes = currentState.showDownVotes,
                showUpVotePercentage = currentState.showUpVotePercentage,
            ) ?: return
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(loading = true) }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                siteRepository.updateAccountSettings(
                    auth = auth,
                    value = settingsToSave,
                )
                refreshSettings()
                updateState {
                    it.copy(
                        loading = false,
                        hasUnsavedChanges = false,
                    )
                }
                emitEffect(
                    AccountSettingsMviModel.Effect.Success,
                )
            } catch (e: Exception) {
                updateState { it.copy(loading = false) }
                emitEffect(
                    AccountSettingsMviModel.Effect.Failure,
                )
            }
        }
    }
}
