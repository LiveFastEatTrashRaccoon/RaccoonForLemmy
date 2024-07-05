package com.github.diegoberaldin.raccoonforlemmy.feature.profile.menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback

class ProfileSideMenuScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ProfileSideMenuMviModel>()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val uiState by model.uiState.collectAsState()

        Scaffold(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            topBar = {
                TopAppBar(
                    colors =
                        TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        ),
                    title = {},
                    actions = {
                        Icon(
                            modifier =
                                Modifier.padding(horizontal = Spacing.xs).onClick(
                                    onClick =
                                        rememberCallback {
                                            navigationCoordinator.closeSideMenu()
                                        },
                                ),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                )
            },
        ) { padding ->
            ProfileMenuContent(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            start = Spacing.m,
                            end = Spacing.m,
                        ),
                isModerator = uiState.isModerator,
            )
        }
    }
}
