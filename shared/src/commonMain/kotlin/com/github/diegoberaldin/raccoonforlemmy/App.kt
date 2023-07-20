package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.AppTheme
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.di.getTemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.feature_inbox.InboxTab
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.ProfileTab
import com.github.diegoberaldin.raccoonforlemmy.feature_search.SearchTab
import com.github.diegoberaldin.raccoonforlemmy.ui.navigation.TabNavigationItem
import com.github.diegoberaldin.racoonforlemmy.feature_home.HomeTab
import com.github.diegoberaldin.racoonforlemmy.feature_settings.SettingsTab
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val keyStore = remember { getTemporaryKeyStore() }
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = runBlocking {
        keyStore.get(KeyStoreKeys.EnableDarkTheme, systemDarkTheme)
    }
    AppTheme(
        darkTheme = darkTheme
    ) {
        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    BottomAppBar {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(InboxTab)
                        TabNavigationItem(ProfileTab)
                        TabNavigationItem(SearchTab)
                        TabNavigationItem(SettingsTab)
                    }
                }
            )
        }
    }
}
