package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterContractKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileContentViewModel(
    private val mvi: DefaultMviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val keyStore: TemporaryKeyStore,
    private val themeRepository: ThemeRepository,
    private val notificationCenter: NotificationCenter,
) : ScreenModel,
    MviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect> by mvi {

    @OptIn(FlowPreview::class)
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
        keyStore.apply {
            remove(KeyStoreKeys.UiTheme)
            remove(KeyStoreKeys.ContentFontScale)
            remove(KeyStoreKeys.Locale)
            remove(KeyStoreKeys.LastIntance)
            remove(KeyStoreKeys.DefaultListingType)
            remove(KeyStoreKeys.DefaultPostSortType)
            remove(KeyStoreKeys.DefaultCommentSortType)
            remove(KeyStoreKeys.IncludeNsfw)
            remove(KeyStoreKeys.BlurNsfw)
            remove(KeyStoreKeys.NavItemTitlesVisible)
            remove(KeyStoreKeys.DynamicColors)
            remove(KeyStoreKeys.OpenUrlsInExternalBrowser)
            remove(KeyStoreKeys.EnableSwipeActions)
            remove(KeyStoreKeys.CustomSeedColor)
            remove(KeyStoreKeys.PostLayout)
        }

        identityRepository.clearToken()
        themeRepository.apply {
            changePostLayout(PostLayout.Card)
            changeCustomSeedColor(null)
            changeDynamicColors(false)
            changeNavItemTitles(true)
        }

        notificationCenter.getAllObservers(NotificationCenterContractKeys.Logout).forEach {
            it.invoke(Unit)
        }
    }
}
