package com.github.diegoberaldin.raccoonforlemmy.feature_profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.di.getApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.di.getProfileScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.di.staticString
import dev.icerock.moko.resources.desc.desc

object ProfileTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.AccountCircle)
            val apiConfigurationRepository = remember { getApiConfigurationRepository() }
            val instance by apiConfigurationRepository.instance.collectAsState("")

            return remember(instance) {
                TabOptions(
                    index = 0u, title = instance, icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getProfileScreenModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                val languageRepository = remember { getLanguageRepository() }
                val lang by languageRepository.currentLanguage.collectAsState()
                val title by remember(lang) {
                    mutableStateOf(staticString(MR.strings.navigation_profile.desc()))
                }
                TopAppBar(title = {
                    Text(
                        text = title, style = MaterialTheme.typography.titleLarge
                    )
                })
            },
        ) {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current
            Box(
                modifier = Modifier.padding(it),
            ) {
                val user = uiState.currentUser
                if (user == null) {
                    ProfileNotLoggedContent(onLogin = {
                        bottomSheetNavigator.show(LoginBottomSheet())
                    })
                } else {
                    ProfileLoggedContent(user = user, onLogout = {
                        model.reduce(ProfileScreenMviModel.Intent.Logout)
                    })
                }
            }
        }
    }
}
