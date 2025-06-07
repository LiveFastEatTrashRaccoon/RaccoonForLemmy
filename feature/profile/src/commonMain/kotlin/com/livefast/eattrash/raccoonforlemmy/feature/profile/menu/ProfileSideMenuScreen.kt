package com.livefast.eattrash.raccoonforlemmy.feature.profile.menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator

class ProfileSideMenuScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: ProfileSideMenuMviModel = rememberScreenModel()
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
                        IconButton(
                            onClick = {
                                navigationCoordinator.closeSideMenu()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = LocalStrings.current.buttonClose,
                            )
                        }
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
                canCreateCommunity = uiState.canCreateCommunity,
                isBookmarksVisible = uiState.isBookmarksVisible,
            )
        }
    }
}
