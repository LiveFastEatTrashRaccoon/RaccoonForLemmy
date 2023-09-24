package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterContractKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileContentViewModel(
    private val mvi: DefaultMviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val keyStore: TemporaryKeyStore,
    private val notificationCenter: NotificationCenter,
) : ScreenModel,
    MviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()

        mvi.scope?.launch {
            identityRepository.authToken.debounce(250).onEach { token ->
                mvi.updateState { it.copy(logged = !token.isNullOrEmpty()) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: ProfileContentMviModel.Intent) {
        when (intent) {
            ProfileContentMviModel.Intent.Logout -> logout()
        }
    }

    private fun logout() {
        keyStore.remove(KeyStoreKeys.UiTheme)
        keyStore.remove(KeyStoreKeys.DynamicColors)
        keyStore.remove(KeyStoreKeys.CustomPrimaryColor)
        keyStore.remove(KeyStoreKeys.CustomSecondaryColor)
        keyStore.remove(KeyStoreKeys.CustomTertiaryColor)
        keyStore.remove(KeyStoreKeys.ContentFontScale)
        keyStore.remove(KeyStoreKeys.Locale)
        keyStore.remove(KeyStoreKeys.DefaultListingType)
        keyStore.remove(KeyStoreKeys.DefaultCommentSortType)
        keyStore.remove(KeyStoreKeys.DefaultPostSortType)
        keyStore.remove(KeyStoreKeys.EnableSwipeActions)
        keyStore.remove(KeyStoreKeys.NavItemTitlesVisible)
        keyStore.remove(KeyStoreKeys.OpenUrlsInExternalBrowser)
        keyStore.remove(KeyStoreKeys.IncludeNsfw)
        keyStore.remove(KeyStoreKeys.BlurNsfw)
        identityRepository.clearToken()
        notificationCenter.getAllObservers(NotificationCenterContractKeys.Logout)?.forEach {
            it.invoke(Unit)
        }
    }
}
