package com.github.diegoberaldin.raccoonforlemmy.main

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.diegoberaldin.raccoonforlemmy.home.HomeTab
import com.github.diegoberaldin.raccoonforlemmy.inbox.InboxTab
import com.github.diegoberaldin.raccoonforlemmy.profile.ProfileTab
import com.github.diegoberaldin.raccoonforlemmy.search.SearchTab
import com.github.diegoberaldin.raccoonforlemmy.settings.SettingsTab
import com.github.diegoberaldin.raccoonforlemmy.ui.TabNavigationItem
import com.github.diegoberaldin.raccoonforlemmy.ui.theme.AppTheme

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
