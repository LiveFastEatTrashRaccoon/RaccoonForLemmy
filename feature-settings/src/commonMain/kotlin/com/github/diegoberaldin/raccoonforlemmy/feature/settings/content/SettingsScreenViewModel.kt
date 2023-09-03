package com.github.diegoberaldin.raccoonforlemmy.feature.settings.content

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toFontScale
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.resources.LanguageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val mvi: DefaultMviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect>,
    private val themeRepository: ThemeRepository,
    private val languageRepository: LanguageRepository,
    private val identityRepository: IdentityRepository,
    private val keyStore: TemporaryKeyStore,
) : ScreenModel,
    MviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        mvi.scope.launch(Dispatchers.Main) {
            themeRepository.state.onEach { currentTheme ->
                mvi.updateState { it.copy(currentTheme = currentTheme) }
            }.launchIn(this)
            themeRepository.contentFontScale.onEach { value ->
                mvi.updateState { it.copy(currentFontScale = value.toFontScale()) }
            }.launchIn(this)
            languageRepository.currentLanguage.onEach { lang ->
                mvi.updateState { it.copy(lang = lang) }
            }.launchIn(this)
            identityRepository.authToken.onEach { auth ->
                mvi.updateState { it.copy(isLogged = !auth.isNullOrEmpty()) }
            }.launchIn(this)
        }

        val listingType = keyStore[KeyStoreKeys.DefaultListingType, 0].toListingType()
        val postSortType = keyStore[KeyStoreKeys.DefaultPostSortType, 0].toSortType()
        val commentSortType = keyStore[KeyStoreKeys.DefaultCommentSortType, 3].toSortType()
        mvi.updateState {
            it.copy(
                defaultListingType = listingType,
                defaultPostSortType = postSortType,
                defaultCommentSortType = commentSortType,
            )
        }
    }

    override fun reduce(intent: SettingsScreenMviModel.Intent) {
        when (intent) {
            is SettingsScreenMviModel.Intent.ChangeTheme -> applyTheme(intent.value)
            is SettingsScreenMviModel.Intent.ChangeContentFontSize -> applyContentFontScale(intent.value)
            is SettingsScreenMviModel.Intent.ChangeLanguage -> changeLanguage(intent.value)
            is SettingsScreenMviModel.Intent.ChangeDefaultCommentSortType -> changeDefaultCommentSortType(
                intent.value,
            )

            is SettingsScreenMviModel.Intent.ChangeDefaultListingType -> changeDefaultListingType(
                intent.value,
            )

            is SettingsScreenMviModel.Intent.ChangeDefaultPostSortType -> changeDefaultPostSortType(
                intent.value,
            )
        }
    }

    private fun applyTheme(value: ThemeState) {
        themeRepository.changeTheme(value)
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.UiTheme, value.toInt())
        }
    }

    private fun applyContentFontScale(value: Float) {
        themeRepository.changeContentFontScale(value)
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.ContentFontScale, value)
        }
    }

    private fun changeLanguage(value: String) {
        languageRepository.changeLanguage(value)
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.Locale, value)
        }
    }

    private fun changeDefaultListingType(value: ListingType) {
        mvi.updateState { it.copy(defaultListingType = value) }
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.DefaultListingType, value.toInt())
        }
    }

    private fun changeDefaultPostSortType(value: SortType) {
        mvi.updateState { it.copy(defaultPostSortType = value) }
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.DefaultPostSortType, value.toInt())
        }
    }

    private fun changeDefaultCommentSortType(value: SortType) {
        mvi.updateState { it.copy(defaultCommentSortType = value) }
        mvi.scope.launch(Dispatchers.Main) {
            keyStore.save(KeyStoreKeys.DefaultCommentSortType, value.toInt())
        }
    }
}
