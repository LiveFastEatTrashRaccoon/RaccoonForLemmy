package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.AppTheme
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.di.getTemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.feature_home.ui.HomeTab
import com.github.diegoberaldin.raccoonforlemmy.feature_inbox.InboxTab
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.ProfileTab
import com.github.diegoberaldin.raccoonforlemmy.feature_search.SearchTab
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui.SettingsTab
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.ui.navigation.TabNavigationItem
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun App() {
    val keyStore = remember { getTemporaryKeyStore() }
    val systemDarkTheme = isSystemInDarkTheme()
    val currentTheme = runBlocking {
        keyStore.get(KeyStoreKeys.UITheme, if (systemDarkTheme) 1 else 0)
    }.let { ThemeState.fromInt(it) }

    val defaultLocale = stringResource(MR.strings.lang)
    val langCode = runBlocking {
        keyStore.get(KeyStoreKeys.Locale, defaultLocale)
    }
    val languageRepository = remember { getLanguageRepository() }
    LaunchedEffect(Unit) {
        delay(100)
        languageRepository.changeLanguage(langCode)
    }

    val scope = rememberCoroutineScope()
    languageRepository.currentLanguage.onEach { lang ->
        StringDesc.localeType = StringDesc.LocaleType.Custom(lang)
    }.launchIn(scope)

    AppTheme(
        theme = currentTheme
    ) {
        val lang by languageRepository.currentLanguage.collectAsState()
        LaunchedEffect(lang) {}

        val bottomSheetContent = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
        val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        suspend fun handleBottomSheet(content: (@Composable () -> Unit)?) {
            when {
                content != null -> {
                    bottomSheetContent.value = content
                    bottomSheetState.show()
                }

                else -> bottomSheetState.hide()
            }
        }

        LaunchedEffect(HomeTab) {
            HomeTab.bottomSheetFlow.debounce(250).onEach { content ->
                handleBottomSheet(content)
            }.launchIn(this)
        }
        LaunchedEffect(SettingsTab) {
            SettingsTab.bottomSheetFlow.debounce(250).onEach { content ->
                handleBottomSheet(content)
            }.launchIn(this)
        }

        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = {
                bottomSheetContent.value?.also { it() }
            }
        ) {
            TabNavigator(HomeTab) {
                Scaffold(
                    content = {
                        CurrentTab()
                    },
                    bottomBar = {
                        BottomAppBar {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(SearchTab)
                            TabNavigationItem(ProfileTab)
                            TabNavigationItem(InboxTab)
                            TabNavigationItem(SettingsTab)
                        }
                    }
                )
            }
        }
    }
}
