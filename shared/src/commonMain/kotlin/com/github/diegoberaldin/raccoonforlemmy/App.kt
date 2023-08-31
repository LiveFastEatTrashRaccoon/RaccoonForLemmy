package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.AppTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_surface
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_dark_surface
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_light_surface
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.di.getTemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.di.getApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.feature.home.ui.HomeTab
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.ui.InboxTab
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.ui.ProfileTab
import com.github.diegoberaldin.raccoonforlemmy.feature.search.ui.SearchTab
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.SettingsTab
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.ui.navigation.TabNavigationItem
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun App() {
    val keyStore = remember { getTemporaryKeyStore() }
    val systemDarkTheme = isSystemInDarkTheme()
    val currentTheme = keyStore[KeyStoreKeys.UiTheme, if (systemDarkTheme) 1 else 0].let {
        it.toThemeState()
    }

    val defaultLocale = stringResource(MR.strings.lang)
    val langCode = keyStore[KeyStoreKeys.Locale, defaultLocale]
    val languageRepository = remember { getLanguageRepository() }
    LaunchedEffect(Unit) {
        delay(100)
        languageRepository.changeLanguage(langCode)
    }
    val scope = rememberCoroutineScope()
    languageRepository.currentLanguage.onEach { lang ->
        StringDesc.localeType = StringDesc.LocaleType.Custom(lang)
    }.launchIn(scope)

    val lastInstance = keyStore[KeyStoreKeys.LastIntance, ""]
    val apiConfigurationRepository = remember { getApiConfigurationRepository() }
    if (lastInstance.isEmpty()) {
        val instance = apiConfigurationRepository.getInstance()
        keyStore.save(KeyStoreKeys.LastIntance, instance)
    } else {
        apiConfigurationRepository.changeInstance(lastInstance)
    }

    AppTheme(
        theme = currentTheme,
    ) {
        val lang by languageRepository.currentLanguage.collectAsState()
        LaunchedEffect(lang) {}

        BottomSheetNavigator {
            TabNavigator(HomeTab) {
                Scaffold(
                    content = {
                        CurrentTab()
                    },
                    bottomBar = {
                        val themeRepository = remember { getThemeRepository() }
                        val themeState by themeRepository.state.collectAsState()
                        BottomAppBar(
                            contentPadding = PaddingValues(0.dp),
                            backgroundColor = when (themeState) {
                                ThemeState.Light -> {
                                    md_theme_light_surface
                                }

                                ThemeState.Dark -> {
                                    md_theme_dark_surface
                                }

                                else -> {
                                    md_theme_black_surface
                                }
                            },
                        ) {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(SearchTab)
                            TabNavigationItem(ProfileTab)
                            TabNavigationItem(InboxTab)
                            TabNavigationItem(SettingsTab)
                        }
                    },
                )
            }
        }
    }
}
