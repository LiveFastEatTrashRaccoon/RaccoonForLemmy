package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.diegoberaldin.raccoonforlemmy.feature_inbox.InboxTab
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.ProfileTab
import com.github.diegoberaldin.raccoonforlemmy.feature_search.SearchTab
import com.github.diegoberaldin.raccoonforlemmy.ui.navigation.TabNavigationItem
import com.github.diegoberaldin.raccoonforlemmy.ui.theme.AppTheme
import com.github.diegoberaldin.racoonforlemmy.feature_home.HomeTab
import com.github.diegoberaldin.racoonforlemmy.feature_settings.SettingsTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    AppTheme {
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
