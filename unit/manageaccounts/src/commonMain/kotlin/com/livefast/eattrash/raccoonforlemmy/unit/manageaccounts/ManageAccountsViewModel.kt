package com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DeleteAccountUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.SwitchAccountUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageAccountsViewModel(
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
    private val switchAccount: SwitchAccountUseCase,
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<ManageAccountsMviModel.Intent, ManageAccountsMviModel.UiState, ManageAccountsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ManageAccountsMviModel.UiState()),
    ManageAccountsMviModel {
    init {
        if (uiState.value.accounts.isEmpty()) {
            viewModelScope.launch {
                settingsRepository.currentSettings
                    .onEach { settings ->
                        updateState {
                            it.copy(
                                autoLoadImages = settings.autoLoadImages,
                                preferNicknames = settings.preferUserNicknames,
                            )
                        }
                    }.launchIn(this)

                accountRepository
                    .observeAll()
                    .onEach { accounts ->
                        updateState { it.copy(accounts = accounts) }
                    }.launchIn(this)
            }
        }
    }

    override fun reduce(intent: ManageAccountsMviModel.Intent) {
        when (intent) {
            is ManageAccountsMviModel.Intent.SwitchAccount -> {
                uiState.value.accounts.getOrNull(intent.index)?.also { account ->
                    handleSwitchAccount(account)
                }
            }

            is ManageAccountsMviModel.Intent.DeleteAccount -> {
                uiState.value.accounts.getOrNull(intent.index)?.also { account ->
                    if (account.active) {
                        viewModelScope.launch {
                            logout()
                            deleteAccount(account)
                            close()
                        }
                    } else {
                        viewModelScope.launch {
                            deleteAccount(account)
                            updateState {
                                it.copy(accounts = it.accounts.filter { a -> a.id != account.id })
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleSwitchAccount(account: AccountModel) {
        if (account.active) {
            return
        }
        viewModelScope.launch {
            switchAccount(account)
            notificationCenter.send(NotificationCenterEvent.ResetHome)
            notificationCenter.send(NotificationCenterEvent.ResetExplore)
            notificationCenter.send(NotificationCenterEvent.ResetInbox)

            close()
        }
    }

    private suspend fun close() {
        withContext(Dispatchers.Main) {
            emitEffect(ManageAccountsMviModel.Effect.Close)
        }
    }
}
