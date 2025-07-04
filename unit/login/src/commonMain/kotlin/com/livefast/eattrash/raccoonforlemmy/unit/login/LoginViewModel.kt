package com.livefast.eattrash.raccoonforlemmy.unit.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LoginUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val siteRepository: SiteRepository,
    private val communityRepository: CommunityRepository,
    private val login: LoginUseCase,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<LoginMviModel.Intent, LoginMviModel.UiState, LoginMviModel.Effect>
    by DefaultMviModelDelegate(initialState = LoginMviModel.UiState()),
    LoginMviModel {
    init {
        val instance = apiConfigurationRepository.instance.value
        viewModelScope.launch {
            updateState {
                it.copy(instanceName = instance)
            }
        }
    }

    override fun reduce(intent: LoginMviModel.Intent) {
        when (intent) {
            LoginMviModel.Intent.Confirm -> submit()
            is LoginMviModel.Intent.SetInstanceName -> setInstanceName(intent.value)
            is LoginMviModel.Intent.SetPassword -> setPassword(intent.value)
            is LoginMviModel.Intent.SetTotp2faToken -> setTotp2faToken(intent.value)
            is LoginMviModel.Intent.SetUsername -> setUsername(intent.value)
        }
    }

    private fun setInstanceName(value: String) {
        viewModelScope.launch {
            updateState { it.copy(instanceName = value) }
        }
    }

    private fun setUsername(value: String) {
        viewModelScope.launch {
            updateState { it.copy(username = value.trim()) }
        }
    }

    private fun setPassword(value: String) {
        viewModelScope.launch {
            updateState { it.copy(password = value) }
        }
    }

    private fun setTotp2faToken(value: String) {
        viewModelScope.launch {
            updateState { it.copy(totp2faToken = value) }
        }
    }

    private fun submit() {
        val currentState = uiState.value
        if (currentState.loading) {
            return
        }

        val instance = currentState.instanceName
        val username = currentState.username
        val password = currentState.password
        val totp2faToken = currentState.totp2faToken
        viewModelScope.launch {
            updateState {
                it.copy(
                    instanceNameError = null,
                    usernameError = null,
                    passwordError = null,
                )
            }
        }

        val valid =
            when {
                instance.isEmpty() -> {
                    viewModelScope.launch {
                        updateState {
                            it.copy(instanceNameError = ValidationError.MissingField)
                        }
                    }
                    false
                }

                username.isEmpty() -> {
                    viewModelScope.launch {
                        updateState {
                            it.copy(usernameError = ValidationError.MissingField)
                        }
                    }
                    false
                }

                password.isEmpty() -> {
                    viewModelScope.launch {
                        updateState {
                            it.copy(passwordError = ValidationError.MissingField)
                        }
                    }
                    false
                }

                else -> true
            }
        if (!valid) {
            return
        }

        viewModelScope.launch {
            updateState { it.copy(loading = true) }

            val res =
                communityRepository.getList(
                    instance = instance,
                    page = 1,
                    limit = 1,
                )
            if (res.isEmpty()) {
                updateState {
                    it.copy(
                        instanceNameError = ValidationError.InvalidField,
                        loading = false,
                    )
                }
                return@launch
            }

            val result =
                login(
                    instance = instance,
                    username = username,
                    password = password,
                    totp2faToken = totp2faToken,
                )
            updateState { it.copy(loading = false) }

            if (result.isFailure) {
                result.exceptionOrNull()?.also {
                    val message = it.message
                    emitEffect(LoginMviModel.Effect.LoginError(message))
                }
                return@launch
            }

            val accountId = accountRepository.getActive()?.id
            if (accountId != null) {
                val auth = identityRepository.authToken.value.orEmpty()
                val avatar = siteRepository.getCurrentUser(auth = auth)?.avatar
                accountRepository.update(
                    id = accountId,
                    avatar = avatar,
                    jwt = auth,
                )
            }
            notificationCenter.send(NotificationCenterEvent.ResetExplore)
            notificationCenter.send(NotificationCenterEvent.ResetHome)
            emitEffect(LoginMviModel.Effect.LoginSuccess)
        }
    }
}
